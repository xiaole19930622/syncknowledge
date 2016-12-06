package com.xuebang.o2o.business.controller;

import com.xuebang.o2o.business.service.CheckKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 检测知识点
 * Created by xiaole on 2016/11/9.
 */
@Controller
@RequestMapping("chechKnow")
public class CheckKnowledgeController {

    @Autowired
    private CheckKnowledgeService checkKnowledgeService;

    @RequestMapping("checkKnow")
    @ResponseBody
    public Boolean checkKnow( String filePath ) throws IOException {
        Boolean result =  checkKnowledgeService.check(filePath);
        return result;
    }

}
