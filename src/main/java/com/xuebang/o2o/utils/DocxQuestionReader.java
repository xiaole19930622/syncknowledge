package com.xuebang.o2o.utils;

import com.aspose.words.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.Arrays;

/**
 * Created by xuwen on 2015-5-21.
 */
public class DocxQuestionReader {
    public static void main(String[] args) throws Exception{
        java.util.List<String> steps = Arrays.asList(new String[]{"【题型】", "【知识点体系】", "【题干】", "【分值】", "【难度】", "【答案】", "【思维启发】", "【答案解析】", "【评价】"});
        java.util.List<String> textSteps = Arrays.asList(new String[]{"【知识点体系】","【分值】","【难度】","【评价】"});
        com.aspose.words.Document doc = new Document("g:/test/1.docx");
        doc.save("g:/test/1.html");
        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(new File("g:/test/1.html"), "utf-8");
        Elements elements = htmlDoc.select("body>div>p");
        int currentStep = -1;
        Object object = null;
        elements:for (int i = 0; i < elements.size(); i++) {
            Element pTag = elements.get(i);
            for (int j = 0; j < steps.size(); j++) {
                if(pTag.html().contains(steps.get(j))){
                    currentStep = j;
                    if(currentStep == 0){
                        if(object != null){
                            System.out.println("一道题结束了--------------------------------" + object.toString());
                        }
                        object = new Object();
                        System.out.println("一道题开始了--------------------------------");
                    }
                    System.out.println(steps.get(j) + "--------------------------------");
                    continue elements;
                }
            }
            if(currentStep > -1){
                if(textSteps.contains(steps.get(currentStep))){
                    System.out.println(pTag.text());
                }else{
                    System.out.println(pTag.outerHtml());
                }
            }
            if(i == elements.size() - 1 && object != null){
                System.out.println("一道题结束了--------------------------------" + object.toString());
            }
        }
    }
}
