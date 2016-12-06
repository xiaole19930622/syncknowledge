package com.xuebang.o2o.utils;


import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.*;
import java.net.URLEncoder;


import static org.apache.commons.io.FileUtils.deleteQuietly;

import static com.xuebang.o2o.utils.EncodeUtils.getUUID;

/**
 * Created by Administrator on 2015/4/26.
 */
public class FileUploadUtil {
    /**
     * 文件保存目录
     */
    public static final String FILEDIR = "/upload";

    public static final String PICTUREDIR ="/picture";

    private static final String PATTERN = "yyyy-MM-dd";

    /**
     * 处理上传文件核心方法
     *
     * @param request  servlet请求
     * @param file     多媒体文件
     * @param fileDirs 文件保存目录(支持多级目录,默认"/upload")
     * @return
     */
    public static FileUpload uploadFile(HttpServletRequest request, MultipartFile file, String userId, String... fileDirs) {
        HttpSession session = request.getSession();
        return uploadFile(session, file, userId, fileDirs);
    }

    /**
     * 处理上传文件核心方法
     *
     * @param session
     * @param file
     * @param fileDirs
     * @return
     */
    public static FileUpload uploadFile(HttpSession session, MultipartFile file, String userId, String... fileDirs) {
        ServletContext context = session.getServletContext();
        return uploadFile(context, file, userId, fileDirs);
    }

    /**
     *  缩放后裁剪图片方法
     * @param srcImageFile  源文件
     * @param x  x坐标
     * @param y  y坐标
     * @param destWidth  最终生成的图片宽
     * @param destHeight  最终生成的图片高
     * @param finalWidth  缩放宽度
     * @param finalHeight  缩放高度
     */
    public static void abccut(String srcImageFile,int x,int y,int destWidth,int destHeight,int finalWidth,int finalHeight){

        try {
            Image img;
            ImageFilter cropFilter;
            //读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth=bi.getWidth(); //源图宽度
            int srcHeight =bi.getHeight(); //源图高度

            if(srcWidth>=destWidth && srcHeight>=destHeight){
                Image image=bi.getScaledInstance(finalWidth,finalHeight,Image.SCALE_DEFAULT);//获取缩放后的图片大小
                cropFilter=new CropImageFilter(x,y,destWidth,destHeight);
                img =Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(),cropFilter));
                BufferedImage tag =new BufferedImage(destWidth,destHeight,BufferedImage.TYPE_INT_RGB);
                Graphics g=tag.getGraphics();
                g.drawImage(img,0,0,null);  // 绘制截取后的图
                g.dispose();
                // 输出为文件
                ImageIO.write(tag,"GIF",new File(srcImageFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 处理上传文件核心方法
     *
     * @param context  servlet上下文
     * @param file     多媒体文件
     * @param fileDirs 文件保存目录(支持多级目录,默认"/upload")
     * @return
     */
    public static FileUpload uploadFile(ServletContext context, MultipartFile file, String userId, String... fileDirs) {
        String origFileName = file.getOriginalFilename();
        int index = getLastIndex(origFileName);
        String fileExt = origFileName.substring(index);
        String uuid = getUUID();
        String newFileName = uuid.concat(fileExt);
        String fileDir = null;
        if (fileDirs.length == 0) {
            fileDir = FILEDIR;
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : fileDirs) {
                sb.append(s);
            }
            fileDir = sb.toString();
        }


        fileDir = fileDir.concat("/").concat(userId);
        String relaFilePath = fileDir.concat("/").concat(newFileName);
        String realPath = context.getRealPath(fileDir);
        File dir = new File(realPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String absoFilePath = realPath.concat("/").concat(newFileName);
        File destFile = new File(absoFilePath);
        try {
            file.transferTo(destFile);
        } catch (IllegalStateException | IOException e) {
            throw new RuntimeException(e);
        }
        absoFilePath = destFile.getPath();

        FileUpload fu = new FileUpload(origFileName, newFileName, relaFilePath, absoFilePath, file.getContentType());
        return fu;
    }

    private static int getLastIndex(String fileName) {
        int index = fileName.lastIndexOf(".");
        return index;
    }

    /**
     * 下载文件核心处理方法
     *
     * @param context  servlet上下文
     * @param response servlet响应
     * @param fileName 原始文件名
     * @param filePath 文件保存相对路径
     */
    public static void downloadFile(ServletContext context, HttpServletResponse response, String fileName, String filePath) {
        String realPath = context.getRealPath(filePath);
        downloadFile(response, fileName, realPath);
    }

    private static void downloadFile(HttpServletResponse response, String fileName, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("下载的文件不存在！");
        }

        try (InputStream input = new FileInputStream(file); OutputStream output = response.getOutputStream();) {
            String encoding = new String(fileName.getBytes(), HttpConstants.UTF_8);
            if (getLastIndex(fileName) == -1) {
                int index = getLastIndex(filePath);
                String fileExt = filePath.substring(index);
                encoding = encoding.concat(fileExt);
            }
            String value = HttpConstants.CONT_DISP_ATTA.concat(URLEncoder.encode(encoding,HttpConstants.UTF_8));//此处一定要对文件名进行编码
            response.addHeader(HttpConstants.CONTENT_DISPOSITION,  value);
            response.setContentType(HttpConstants.APPLICATION_OCTET_STREAM);
            byte[] b = new byte[input.available()];
            input.read(b);
            output.write(b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param request  servlet请求
     * @param filePath 文件保存相对路径
     * @return 删除文件（支持目录、文件）
     */
    public static boolean delete(HttpServletRequest request, String filePath,String userId) {
        ServletContext context = request.getServletContext();
        return delete(context, filePath,userId);
    }

    /**
     * 删除文件（支持目录、文件）
     *
     * @param context  servlet上下文
     * @param filePath 文件保存相对路径
     * @return
     */
    public static boolean delete(ServletContext context, String filePath,String userId) {
        if (filePath == null) {
            return false;
        }

        String realPath = context.getRealPath("\\upload").concat("/").concat(userId).concat("\\"+filePath);
        File file = new File(realPath);
        boolean result = deleteQuietly(file);
        File parentFile  = file.getParentFile();
        if(parentFile.exists()){
            parentFile.delete();
        }

        return result;
    }


    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }


    /**
     * 此内部类主要提供：原始文件名、新文件名、相对路径、绝对路径
     */
    public static class FileUpload {
        private String origFileName;// 原始文件名
        private String newFileName;// 新文件名
        private String relaFilePath;// 相对路径
        private String absoFilePath;// 绝对路径
        private String contentType;// 文件类型
        private String extensionName;//文件扩展名
        private Long fileSize;   //文件大小







        private FileUpload(String origFileName, String newFileName, String relaFilePath, String absoFilePath, String contentType) {
            this.origFileName = origFileName;
            this.newFileName = newFileName;
            this.relaFilePath = relaFilePath;
            this.absoFilePath = absoFilePath;
            this.contentType = contentType;
        }

        /**
         * 原始文件名
         *
         * @return
         */
        public String getOrigFileName() {
            return origFileName;
        }


        public void setOrigFileName(String origFileName) {
            this.origFileName = origFileName;
        }

        /**
         * 新文件名
         *
         * @return
         */
        public String getNewFileName() {
            return newFileName;
        }

        public void setNewFileName(String newFileName) {
            this.newFileName = newFileName;
        }

        /**
         * 相对路径
         *
         * @return
         */
        public String getRelaFilePath() {
            return relaFilePath;
        }

        public void setRelaFilePath(String relaFilePath) {
            this.relaFilePath = relaFilePath;
        }

        /**
         * 绝对路径
         *
         * @return
         */
        public String getAbsoFilePath() {
            return absoFilePath;
        }

        public void setAbsoFilePath(String absoFilePath) {
            this.absoFilePath = absoFilePath;
        }


        /**
         * 文件类型
         *
         * @return
         */
        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }




        /**
         * 文件扩展名
         * @return
         */
        public String getExtensionName() {
            return extensionName;
        }

        public void setExtensionName(String extensionName) {
            this.extensionName = extensionName;
        }


        /**
         * 文件大小
         * @return
         */
        public Long getFileSize() {
            return fileSize;
        }

        public void setFileSize(Long fileSize) {
            this.fileSize = fileSize;
        }

        @Override
        public String toString() {
            return String.format("FileUpload [origFileName=%s, newFileName=%s, relaFilePath=%s, absoFilePath=%s, contentType=%s]", origFileName, newFileName, relaFilePath, absoFilePath, contentType);
        }
    }


}
