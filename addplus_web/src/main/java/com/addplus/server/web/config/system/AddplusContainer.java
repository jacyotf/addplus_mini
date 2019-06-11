package com.addplus.server.web.config.system;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.api.model.base.ReturnDataSet;
import com.addplus.server.api.model.base.ServiceMap;
import com.addplus.server.api.utils.SortUtil;
import com.addplus.server.api.utils.security.AESUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.fasterxml.jackson.core.io.JsonEOFException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 特大碗拉面 on 2017/10/24 0024.
 */
public class AddplusContainer {

    private static Logger logger = LoggerFactory.getLogger(SortUtil.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private HttpServletRequest request;

    @Value("${log.record.operator}")
    private Boolean logOperator;

    @Value("${param.encryted}")
    private Boolean encryted;

    private static AddplusContainer addplusContainer;

    private Map<String, Class> serviceClassMap;

    private String packageName;

    private Map<String, String> descriptMap;

    private Map<String, Parameter[]> serviceParameterMap;

    private static final String NULL = "null";

    public static AddplusContainer newInstance(ServiceMap serviceMap, String packageName) {
        if (addplusContainer == null) {
            addplusContainer = new AddplusContainer(serviceMap.getServiceClassMap(), serviceMap.getServiceParameterMap(), serviceMap.getDecrtipetMap(), packageName);
        }

        return addplusContainer;
    }

    private AddplusContainer(Map<String, Class> serviceClassMap, Map<String, Parameter[]> serviceParameterMap, Map<String, String> descriptMap, String packageName) {
        this.descriptMap = descriptMap;
        this.serviceClassMap = serviceClassMap;
        this.serviceParameterMap = serviceParameterMap;
        this.packageName = packageName;
    }

    private AddplusContainer() {
    }

    public String encryptAESContent(ReturnDataSet returnDataSet, String className, String method) {
        String pName = packageName.substring(0, packageName.lastIndexOf("."));
        className = pName + "." + className;
        String invokeName = className + "_" + method;
        return AESUtils.encryptAES(JSON.toJSONString(returnDataSet), descriptMap.get(invokeName), 1);
    }


    public Object sortMapToPojo(String context, Parameter[] parameters, String invokeName, Boolean encryted) throws Exception {
        if (encryted) {
            context = AESUtils.decryptAES(context, descriptMap.get(invokeName), 1);
        }
        if (parameters.length > 0) {
            try {
                if (String.class.equals(parameters[0].getType())) {
                    return context;
                } else {
                    return JSON.parseObject(context, parameters[0].getType());
                }
            } catch (JSONException exception) {
                logger.error(exception.getMessage());
                throw new ErrorException(ErrorCode.SYS_ERROR_DATA_FORMAT);
            }
        }
        return null;
    }


    public ReturnDataSet invoke(String module, String serviceName, String methodName, String ipAddress, Object param, Boolean httpType, Object token) throws Exception {
        ReturnDataSet returnDataSet = new ReturnDataSet();
        if (!httpType && null == param) {
            returnDataSet.setErrorCode(ErrorCode.SYS_ERROR_TYPE_LENGTH);
            return returnDataSet;
        }
        String pName = packageName.substring(0, packageName.lastIndexOf(StringConstant.SERVICE) + 7);
        String genrticClass = pName + "." + serviceName;
        String invokeName = genrticClass + "_" + methodName;
        if (!this.serviceClassMap.containsKey(genrticClass)) {
            // 接口不存在
            returnDataSet.setErrorCode(ErrorCode.SYS_ERROR_PATH);
            return returnDataSet;
        }
        Parameter[] parameters = this.serviceParameterMap.get(invokeName);
        Map<String, String> params = null;
        if (httpType) {
            if (parameters.length > 0) {
                Map map = (Map) param;
                if (map.isEmpty()) {
                    returnDataSet.setErrorCode(ErrorCode.SYS_ERROR_TYPE_LENGTH);
                    return returnDataSet;
                } else {
                    params = new HashMap<String, String>(map);
                    if (parameters.length != params.size()) {
                        // 参数不一致
                        returnDataSet.setErrorCode(ErrorCode.SYS_ERROR_TYPE_LENGTH);
                        return returnDataSet;
                    }
                }
            }
        } else {
            if (null == param) {
                returnDataSet.setErrorCode(ErrorCode.SYS_ERROR_TYPE_LENGTH);
                return returnDataSet;
            }
        }
        if ("rest".equals(module)) {
            if (token == null) {
                token = StringConstant.NOT_TOKEN;
            }
            request.setAttribute(StringConstant.SERVICE, invokeName);
            request.setAttribute(StringConstant.IP_ADDRESS, ipAddress);
            request.setAttribute(StringConstant.REQ_TOKEN_KEY, (String) token);
            request.setAttribute(StringConstant.REST, StringConstant.REST);
        }
        if (logOperator) {
            request.setAttribute(StringConstant.MODULE, module);
        }
        Class serviceClass = this.serviceClassMap.get(genrticClass);
        Object beanService = applicationContext.getBean(serviceClass);
        if (beanService == null) {
            returnDataSet.setErrorCode(ErrorCode.SYS_ERROR_PATH);
            return returnDataSet;
        }
        MethodAccess methodAccess = MethodAccess.get(serviceClass);
        int methodIndex = methodAccess.getIndex(methodName);
        if (httpType) {
            if (parameters.length > 0 && !params.isEmpty()) {
                Object[] paramsNew = SortUtil.sortMapByKey(params, parameters);
                returnDataSet.setDataSet(methodAccess.invoke(beanService, methodIndex, paramsNew));
            } else {
                returnDataSet.setDataSet(methodAccess.invoke(beanService, methodIndex));
            }
        } else {
            Object model = this.sortMapToPojo(param.toString(), parameters, invokeName, encryted);
            //http post请求
            returnDataSet.setDataSet(methodAccess.invoke(beanService, methodIndex, model));
        }
        returnDataSet.setErrorCode(ErrorCode.SYS_SUCCESS);
        if (returnDataSet.getDataSet() == null) {
            returnDataSet.setDataSet("");
        }
        return returnDataSet;
    }


}