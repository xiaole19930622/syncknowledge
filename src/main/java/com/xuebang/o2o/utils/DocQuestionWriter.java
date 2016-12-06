package com.xuebang.o2o.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by xuwen on 2015-6-11.
 */
public class DocQuestionWriter {
    public static void main(String[] args) throws Exception {

        HttpClient client = HttpClientBuilder.create().build();
        for (int pageNumber = 61; pageNumber <= 100; pageNumber++) {
            File docFile = new File("g:/test/" + pageNumber + ".doc");
            String url = "http://localhost:8081/o2o/spiderInterface/spiderZhijiaoQuestion?pageNumber=" + pageNumber;
            HttpUriRequest request = RequestBuilder.create(RequestBuilder.get().getMethod()).setUri(url).build();
            HttpResponse response = client.execute(request);
            System.out.println(response.getStatusLine() + " " + url);
            InputStream is = response.getEntity().getContent();
            POIFSFileSystem poifs = new POIFSFileSystem();
            DirectoryEntry directory = poifs.getRoot();
            DocumentEntry documentEntry = directory.createDocument("WordDocument", is);
            FileOutputStream ostream = new FileOutputStream(docFile);
            poifs.writeFilesystem(ostream);
            ostream.close();
        }
    }
}
