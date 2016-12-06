package com.xuebang.o2o.utils;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import org.apache.log4j.Logger;
import ucar.nc2.util.net.URLencode;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Aliyun oss 工具类
 * see : http://docs.aliyun.com/?spm=5176.383663.9.5.4Hdg6q#/oss/sdk/java-sdk&preface
 * oss-cn-shenzhen
 * 深圳节点外网地址： oss-cn-shenzhen.aliyuncs.com
 * 深圳节点内网地址： oss-cn-shenzhen-internal.aliyuncs.com
 * bucket地址：{bucketName}.oss-cn-shenzhen.aliyuncs.com/
 * Created by xuwen on 2015/3/19.
 */
public class AliyunOSSUtils {

    private static Logger logger = Logger.getLogger(AliyunOSSUtils.class);

    private static final String key = PropertiesUtils.getStringValue("oss.access.key.id");
    private static final String secret = PropertiesUtils.getStringValue("oss.access.key.secret");
    private static final String endpoint = PropertiesUtils.getStringValue("oss.access.endpoint");
    private static final String internalpoint = PropertiesUtils.getStringValue("oss.access.internalpoint");
    private static final String bucketName = PropertiesUtils.getStringValue("oss.access.bucketName");
    private static final int partSize = PropertiesUtils.getIntValue("partSize");

    /**
     * 获取client
     *
     * @return
     */
    public static OSSClient getClient(boolean isLocal) {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setMaxConnections(10);
        conf.setConnectionTimeout(isLocal ? 500 : 5000);
        conf.setMaxErrorRetry(isLocal ? 0 : 3);
        conf.setSocketTimeout(isLocal? 200 : 2000);
        OSSClient client = new OSSClient(isLocal ? internalpoint : endpoint, key, secret);
        logger.debug("Aliyun OSS 客户端已创建");
        return client;
    }
    public static OSSClient getClient() {
        return getClient(false);
    }

    /**
     * 上传文件至阿里云
     *
     * @param bucketName bucket名称
     * @param key  文件标识
     * @param file 文件句柄引用
     * @throws FileNotFoundException
     */
    public static void put(String bucketName, String key, File file) throws FileNotFoundException {
        // 创建上传对象的元数据，必须设置ContentLength
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(file.length());
        // 上传
        PutObjectResult result = null;
        // 先走内网地址上传，发现异常则走外网地址
        try {
            result = getClient(true).putObject(bucketName, key, new FileInputStream(file), meta);
        } catch (com.aliyun.oss.ClientException e) {
            result = getClient(false).putObject(bucketName, key, new FileInputStream(file), meta);
        }
        logger.info("OSS上传完成，" + "key is " + key + "，ContentLength is " + file.length() + "，eTag is " + result.getETag());
    }

    /**
     * 上传文件至阿里云
     *
     * @param key  文件标识
     * @param file 文件句柄引用
     * @throws FileNotFoundException
     */
    public static void put(String key, File file) throws FileNotFoundException {
        put(bucketName, key, file);
    }

    /**
     * 上传文件至阿里云
     *
     * @param key      文件标识
     * @param filePath 文件本地路径
     * @throws FileNotFoundException
     */
    public static void put(String key, String filePath) throws FileNotFoundException {
        put(key, new File(filePath));
    }

    /**
     * 从阿里云获取文件流
     *
     * @param key
     * @return
     */
    public static InputStream get(String key) {
        return getClient().getObject(bucketName, key).getObjectContent();
    }

    /**
     * 从阿里云删除文件
     *
     * @param key 文件标识
     */
    public static void remove(String key) {
        getClient().deleteObject(bucketName, key);
        logger.info("OSS删除完成，key is " + key);
    }

    /**
     * 获取签名URL
     * @param key
     * @param expiration
     * @return
     */
    public static String getSignatureURL(String key,Date expiration){
        return getClient().generatePresignedUrl(bucketName,key,expiration).toString();
    }

    /**
     * 获取签名URL（默认过期时间）
     * @param key
     * @return
     */
    public static String getSignatureURL(String key){
        Date expiration = new Date(new Date().getTime() + 1000 * 60); // 默认60秒后过期
        return getClient().generatePresignedUrl(bucketName,key,expiration).toString();
    }

    /**
     * 通过request获取签名URL
     * @param key
     * @param fileName
     * @return
     */
    public static String getSignatureURLByRequest(String key, String fileName, Date expiration) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key, HttpMethod.GET);
        request.setExpiration(expiration);
        ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
        try {
            responseHeaders.setContentDisposition("attachment;filename=" + URLEncoder.encode(fileName,"UTF-8") );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        responseHeaders.setContentEncoding("UTF-8");
        request.setResponseHeaders(responseHeaders);
        return getClient().generatePresignedUrl(request).toString();
    }

    /**
     * 通过request获取签名URL（默认过期时间）
     * @param key
     * @param fileName
     * @return
     */
    public static String getSignatureURLByRequest(String key, String fileName) {
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * 30); // 默认60秒后过期   目前是30分钟过期
        return getSignatureURLByRequest(key, fileName, expiration);
    }

    /**
     * 获取multipartUpload上传事件
     * @return
     */
    public static InitiateMultipartUploadResult getMultipartUploadResult(){
        OSSClient ossClient = getClient();
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);
        InitiateMultipartUploadResult initiateMultipartUploadResult = ossClient.initiateMultipartUpload(initiateMultipartUploadRequest);
        return initiateMultipartUploadResult;
    }

    /**
     * 分块上传
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<PartETag> uploadPart() throws FileNotFoundException,IOException{
//        //设置每块为5M
//        final int partSize = 1024*1024*5;
        File partFile = new File("");
        //计算分块数目
        int partCount = (int)(partFile.length()/partSize);
        if(partFile.length()%partSize!=0){
            partCount++;
        }
        //新建一个list保存每个分块上传后的ETag和PartNumber
        List<PartETag> partETags = new ArrayList<PartETag>();
        for(int i = 0;i<partCount;i++){
            //获取文件流
            FileInputStream fis = new FileInputStream(partFile);
            //跳到每个分块的开头
            long skipBytes = partSize*i;
            fis.skip(skipBytes);
            //计算每个分块的大小
            long size = partSize<partFile.length()-skipBytes?partSize:partFile.length() - skipBytes;
            //创建UploadPartCopyRequest，上传分块
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(key);
            uploadPartRequest.setUploadId(getMultipartUploadResult().getUploadId());
            uploadPartRequest.setInputStream(fis);
            uploadPartRequest.setPartSize(size);
            uploadPartRequest.setPartNumber(i + 1);
            UploadPartResult uploadPartResult = getClient().uploadPart(uploadPartRequest);
            //将返回的PartETag保存到List中
            partETags.add(uploadPartResult.getPartETag());
            //关闭文件
            fis.close();
        }
        return partETags;
    }

    /**
     * 完成上传
     * @param initiateMultipartUploadResult
     * @param partETags
     */
    public void completeUpload(InitiateMultipartUploadResult initiateMultipartUploadResult,List<PartETag> partETags){
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, key, initiateMultipartUploadResult.getUploadId(),partETags);
        CompleteMultipartUploadResult completeMultipartUploadResult = getClient().completeMultipartUpload(completeMultipartUploadRequest);
    }

    /**
     * 终止上传
     * @param uploadId
     */
    public void abortUpload(String uploadId){
        AbortMultipartUploadRequest abortMultipartUploadRequest = new AbortMultipartUploadRequest(bucketName, key, uploadId);
        getClient().abortMultipartUpload(abortMultipartUploadRequest);
    }









    public static void main(String[] args) throws Exception {
        // 设置URL过期时间为1小时
        Date expiration = new Date(new Date().getTime() + 1000 * 3);
//        URL url = getClient().generatePresignedUrl("test-sts", "广州学邦通讯录及座位分布图.xls", expiration);
//        System.out.println(url.toString());
//        put(key,"C:\\Users\\Administrator\\Desktop\\dd.txt");

    }

}
