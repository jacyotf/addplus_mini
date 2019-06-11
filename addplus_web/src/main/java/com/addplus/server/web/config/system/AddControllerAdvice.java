package com.addplus.server.web.config.system;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.model.base.ReturnDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.StringJoiner;


/**
 * Created by 特大碗拉面 on 2017/10/23 0023.
 */
@ControllerAdvice
public class AddControllerAdvice {

    private Logger logger = LoggerFactory.getLogger(AddControllerAdvice.class);

    @Value("${param.encryted}")
    private Boolean encryted;

    @Autowired
    private AddplusContainer addplusContainer;

    private final String ERRORMESSAGEMODEL = "捕捉到系统异常 URI: {0}, exception: {1}";

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object errorHandler(Exception ex, HttpServletRequest request) {
        String errorMesage = getErrorMessage(request.getRequestURI(), ex.getMessage());
        logger.error(errorMesage);
        ReturnDataSet returnDataSet = new ReturnDataSet();
        returnDataSet.setErrorCode(ErrorCode.SYS_ERROR_SERVICE);
        returnDataSet.setErrorInfo(errorMesage);
        return this.encryptAESContent(returnDataSet, request);
    }


    /**
     * 拦截捕捉自定义异常 ErrorException.class
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ErrorException.class)
    public Object myErrorHandler(ErrorException ex, HttpServletRequest request) throws Exception {
        ReturnDataSet returnDataSet = new ReturnDataSet();
        returnDataSet.setErrorCode(ex.getCodeEnum());
        if (ex.getReturnInfo() != null) {
            returnDataSet.setDataSet(ex.getReturnInfo());
        }
        return this.encryptAESContent(returnDataSet, request);
    }

    private String getErrorMessage(String... args) {
        String error = MessageFormat.format(ERRORMESSAGEMODEL, args);
        return error;
    }

    private Object encryptAESContent(ReturnDataSet returnDataSet, HttpServletRequest request) {
        if (encryted) {
            String[] path = request.getServletPath().split("\\/");
            StringJoiner stringJoiner = new StringJoiner(".");
            String methodName = null;
            for (int i = 0; i < path.length; i++) {
                if (path.length - 1 == i) {
                    methodName = path[i];
                } else {
                    if (i > 1) {
                        stringJoiner.add(path[i]);
                    }
                }
            }
            return addplusContainer.encryptAESContent(returnDataSet, stringJoiner.toString(), methodName);
        }
        return returnDataSet;
    }


}
