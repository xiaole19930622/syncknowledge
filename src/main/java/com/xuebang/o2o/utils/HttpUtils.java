package com.xuebang.o2o.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/29.
 */
public class HttpUtils {


    public static String sendPost(String url, String message) {
        StringBuffer sb = new StringBuffer();
        try {
            URL urls = new URL(url);
            HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
            uc.setRequestMethod("POST");
            uc.setRequestProperty("content-type",
                    "application/x-www-form-urlencoded");
            uc.setRequestProperty("charset", "UTF-8");
            uc.setDoOutput(true);
            uc.setDoInput(true);
            uc.setReadTimeout(10000);
            uc.setConnectTimeout(10000);
            OutputStream os = uc.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.write(message.getBytes("utf-8"));
            dos.flush();
            os.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc
                    .getInputStream(), "utf-8"));
            String readLine = "";
            while ((readLine = in.readLine()) != null) {
                sb.append(readLine);
            }
            in.close();
        } catch (Exception e) {
//            log.error(e.getMessage(), e);
            System.out.print(e.getMessage());
        }
        return sb.toString();
    }



}
