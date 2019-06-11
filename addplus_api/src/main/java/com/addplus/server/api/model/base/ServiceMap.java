package com.addplus.server.api.model.base;


import lombok.Data;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Data
public class ServiceMap {

    private Map<String,Class> serviceClassMap;

    private Map<String, String> decrtipetMap;

    private Map<String, Parameter[]> serviceParameterMap;
}
