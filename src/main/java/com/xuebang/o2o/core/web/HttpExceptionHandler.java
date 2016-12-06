package com.xuebang.o2o.core.web;

import com.xuebang.o2o.core.exception.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xuwen on 2015/4/1.
 */
@ControllerAdvice
public class HttpExceptionHandler {

    private static final Logger logger = Logger.getLogger(HttpExceptionHandler.class);

    /**
     * 业务异常
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response handleServiceException(HttpServletRequest request, ServiceException ex) {
        logger.error("发生业务异常", ex);
        Response response = new Response(false, ex.getMessage());
        return response;
    }

    /**
     * 系统级异常
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response handleException(HttpServletRequest request, Exception ex) {
        logger.error("发生系统错误，请联系管理员", ex);
        Response response = new Response(false, "发生系统错误，请联系管理员");
        return response;
    }

}
