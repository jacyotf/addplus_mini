package com.addplus.server.web.action.oss;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.model.oss.PolicyParam;
import com.addplus.server.api.model.oss.ReturnPolicy;
import com.addplus.server.web.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fuyq
 * @date 2018/8/30
 */
@RestController
@RequestMapping(value = "oss")
public class OssAction {

    @Autowired
    private OssService ossService;

    @RequestMapping(value = "createPolicy", method = RequestMethod.GET)
    public ReturnPolicy createPolicy(String fileName, Boolean randomName) {
        return ossService.createPolicy(fileName, randomName);
    }

    @PostMapping(value = "createPolicyWeb", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ReturnPolicy createPolicyWeb(@RequestBody PolicyParam policyParam) {
        return ossService.createPolicy(policyParam.getFileName(), policyParam.getRandomName());
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ReturnPolicy uploadFile(MultipartHttpServletRequest multipartRequest) {
        MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();
        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (Map.Entry<String, List<MultipartFile>> entry : multiValueMap.entrySet()) {
            multipartFiles.addAll(entry.getValue());
        }
        String randomName = multipartRequest.getParameter("randomName");
        boolean rn = true;
        if (!StringUtils.isEmpty(randomName)) {
            rn = "true".equals(randomName);
        }
        return new ReturnPolicy(ErrorCode.SYS_SUCCESS.getCode(), ossService.uploadFile(multipartFiles, rn, false));
    }

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public ReturnPolicy uploadImageFile(Integer width, Integer height, MultipartHttpServletRequest multipartRequest) {
        MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();
        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (Map.Entry<String, List<MultipartFile>> entry : multiValueMap.entrySet()) {
            multipartFiles.addAll(entry.getValue());
        }
        return new ReturnPolicy(ErrorCode.SYS_SUCCESS.getCode(), ossService.uploadImage(multipartFiles, width, height, false));
    }
}
