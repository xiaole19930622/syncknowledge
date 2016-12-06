package com.xuebang.o2o.utils;

import com.xuebang.o2o.business.entity.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * 代码生成工具类
 * Created by xuwen on 2015/4/9.
 */
public class GenericCodeUtils {

    private static Logger logger = Logger.getLogger(GenericCodeUtils.class);

    public static void genericCode(Class<?> entityClass,Class<?> IDClass) throws IOException {
        String name = entityClass.getSimpleName();
        String classPath = entityClass.getResource("/").getPath();
        String sourceFolder = classPath.replace("target/classes","src/main/java");
        String packagePath = sourceFolder + "/com/xuebang/o2o/business/";
        genericDao(new File(packagePath + "/dao/" + name + "Dao.java"),entityClass,IDClass);
        genericService(new File(packagePath + "/service/" + name + "Service.java"),entityClass,IDClass);
        genericServiceImpl(new File(packagePath + "/service/impl/" + name + "ServiceImpl.java"),entityClass,IDClass);
        genericController(new File(packagePath + "/controller/" + name + "Controller.java"),entityClass,IDClass);
    }

    private static void genericDao(File file,Class<?> entityClass,Class<?> IDClass) throws IOException {
        if(file.createNewFile()){
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            pw.println("package com.xuebang.o2o.business.dao;");
            pw.println("");
            pw.println("import "+ entityClass.getName() +";");
            pw.println("import com.xuebang.o2o.core.repository.GenericDao;");
            pw.println("");
            pw.println("/**");
            pw.println(" * Created by 代码生成工具 on " + new Date().toString());
            pw.println(" */");
            pw.println("public interface "+ file.getName().replace(".java","") +" extends GenericDao<"+ entityClass.getSimpleName() +","+ IDClass.getSimpleName() +"> {");
            pw.println("}");
            pw.flush();
            pw.close();
        }else{
            logger.error("genericDao失败");
        }
    }

    private static void genericService(File file,Class<?> entityClass,Class<?> IDClass) throws IOException {
        if(file.createNewFile()){
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            pw.println("package com.xuebang.o2o.business.service;");
            pw.println("");
            pw.println("import "+ entityClass.getName() +";");
            pw.println("import com.xuebang.o2o.core.service.GenericService;");
            pw.println("");
            pw.println("/**");
            pw.println(" * Created by 代码生成工具 on " + new Date().toLocaleString());
            pw.println(" */");
            pw.println("public interface "+ file.getName().replace(".java","") +" extends GenericService<"+ entityClass.getSimpleName() +","+ IDClass.getSimpleName() +"> {");
            pw.println("}");
            pw.flush();
            pw.close();
        }else{
            logger.error("genericService失败");
        }
    }

    private static void genericServiceImpl(File file,Class<?> entityClass,Class<?> IDClass) throws IOException {
        if(file.createNewFile()){
            String CamerName = entityClass.getSimpleName().substring(0,1).toLowerCase() + entityClass.getSimpleName().substring(1);
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            pw.println("package com.xuebang.o2o.business.service.impl;");
            pw.println("");
            pw.println("import "+ entityClass.getName() +";");
            pw.println("import org.springframework.beans.factory.annotation.Autowired;");
            pw.println("import com.xuebang.o2o.business.service."+ entityClass.getSimpleName() +"Service;");
            pw.println("import com.xuebang.o2o.business.dao."+ entityClass.getSimpleName() +"Dao;");
            pw.println("import com.xuebang.o2o.core.service.GenericServiceImpl;");
            pw.println("import org.springframework.stereotype.Service;");
            pw.println("import org.springframework.transaction.annotation.Transactional;");
            pw.println("");
            pw.println("/**");
            pw.println(" * Created by 代码生成工具 on " + new Date().toLocaleString());
            pw.println(" */");
            pw.println("@Service");
            pw.println("@Transactional");
            pw.println("public class "+ file.getName().replace(".java","") +" extends GenericServiceImpl<"+ entityClass.getSimpleName() +","+ IDClass.getSimpleName() +"> implements "+ entityClass.getSimpleName() +"Service {");
            pw.println("");
            pw.println("    @Autowired");
            pw.println("    private " + entityClass.getSimpleName() + "Dao " + CamerName + "Dao;");
            pw.println("");
            pw.println("}");
            pw.flush();
            pw.close();
        }else{
            logger.error("genericServiceImpl失败");
        }
    }

    private static void genericController(File file,Class<?> entityClass,Class<?> IDClass) throws IOException {
        if(file.createNewFile()){
            String CamerName = entityClass.getSimpleName().substring(0,1).toLowerCase() + entityClass.getSimpleName().substring(1);
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            pw.println("package com.xuebang.o2o.business.controller;");
            pw.println("");
            pw.println("import "+ entityClass.getName() +";");
            pw.println("import org.springframework.beans.factory.annotation.Autowired;");
            pw.println("import com.xuebang.o2o.business.service."+ entityClass.getSimpleName() +"Service;");
            pw.println("import com.xuebang.o2o.core.web.GenericRestController;");
            pw.println("import org.springframework.stereotype.Controller;");
            pw.println("import org.springframework.web.bind.annotation.RequestMapping;");
            pw.println("");
            pw.println("/**");
            pw.println(" * Created by 代码生成工具 on " + new Date().toLocaleString());
            pw.println(" */");
            pw.println("@Controller");
            pw.println("@RequestMapping(\"" + CamerName + "\")");
            pw.println("public class "+ file.getName().replace(".java","") +" extends GenericRestController<"+ entityClass.getSimpleName() +","+ IDClass.getSimpleName() +"> {");
            pw.println("");
            pw.println("    @Autowired");
            pw.println("    private " + entityClass.getSimpleName() + "Service " + CamerName + "Service;");
            pw.println("");
            pw.println("}");
            pw.flush();
            pw.close();
        }else{
            logger.error("genericController失败");
        }
    }



}
