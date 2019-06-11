package com.addplus.server.web.action;

import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.model.base.ReturnDataSet;
import com.addplus.server.api.utils.DataUtils;
import com.addplus.server.web.config.system.AddplusContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 类名: CommonAction
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019-04-16 19:16
 * @description 类描述:公共请求控制层
 */
@RestController
public class CommonAction {

    @Autowired
    private AddplusContainer addplusContainer;


    @Value("${param.encryted}")
    private Boolean encryted;


    @GetMapping(value = "/get/{model}/{module}/{service}/{method}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object get(@PathVariable String model, @PathVariable String module, @PathVariable String service, @PathVariable String method, @RequestParam Map map, @RequestHeader Map<String, String> headerMap) throws Exception {
        ReturnDataSet returnDataSet = addplusContainer.invoke(model, model + "." + module + "." + service, method, DataUtils.getIpAddress(headerMap), map, true, headerMap.getOrDefault(StringConstant.TOKEN, null));
        if (encryted && "rest".equals(model)) {
            return addplusContainer.encryptAESContent(returnDataSet, model + "." + module + "." + service, method);
        }
        return returnDataSet;
    }

    @PostMapping(value = "/post/{model}/{module}/{service}/{method}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object post(@PathVariable String model, @PathVariable String module, @PathVariable String service, @PathVariable String method, @RequestBody(required = false) String contentStr, @RequestHeader Map<String, String> headerMap) throws Exception {
        ReturnDataSet returnDataSet = addplusContainer.invoke(model, model + "." + module + "." + service, method, DataUtils.getIpAddress(headerMap), contentStr, false, headerMap.getOrDefault(StringConstant.TOKEN, null));
        if (encryted && "rest".equals(model)) {
            return addplusContainer.encryptAESContent(returnDataSet, model + "." + module + "." + service, method);
        }
        return returnDataSet;
    }
}
