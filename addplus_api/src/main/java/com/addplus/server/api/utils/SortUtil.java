package com.addplus.server.api.utils;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.exception.ErrorException;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Parameter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SortUtil {

    private static Logger logger = LoggerFactory.getLogger(SortUtil.class);

    private static final String NULL = "null";


    public static Object[] sortMapByKey(Map<String, String> map, Parameter[] parameters) throws Exception {
        Object[] res = new Object[parameters.length];
        int i = 0;
        for (Parameter parameter : parameters) {
            String conetxt = map.getOrDefault(parameter.getName(), null);
            if (StringUtils.isBlank(conetxt)) {
                res[i] = null;
            } else {
                res[i] = covert(conetxt, parameter.getType());
            }
            i++;
        }
        return res;
    }

    private static Object covert(String context, Class paramType) throws Exception {
        context = context.trim();
        if (NULL.equals(context)) {
            return null;
        } else {
            try {
                if (String.class.equals(paramType)) {
                    return context;
                } else {
                    return JSON.parseObject(context, paramType);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new ErrorException(ErrorCode.SYS_ERROR_DATA_FORMAT);
            }
        }
    }

}
