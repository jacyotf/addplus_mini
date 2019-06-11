package com.addplus.server.web.config.aspect;

import com.addplus.server.api.constant.ErrorCode;
import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.exception.ErrorException;
import com.addplus.server.web.config.token.TokenService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 类名: TokenRecord
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2019-04-12 17:17
 * @description 类描述: token注解校验
 */
@Aspect
@Component
public class TokenRecord {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private Map<String, Boolean> tokenAnnontationMap;

    @Value("${annotation.package.name}")
    private String packageName;

    private final static String LANYUE = "lanyue";

    private final static String MEMBERID = "memberId";


    @Pointcut("execution(public * com.addplus.server.web.serviceimpl..*.*(..))")
    public void TokenRecord() {
    }

    // 使用前置通知
    @Before(value = "TokenRecord()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        String restType = (String) request.getAttribute(StringConstant.REST);
        if (!StringConstant.REST.equals(restType)) {
            return;
        }
        String mapKey = (String) request.getAttribute(StringConstant.SERVICE);
        boolean tokenAnnotaion = tokenAnnontationMap.getOrDefault(mapKey, false);
        if (!tokenAnnotaion) {
            //获取token
            String token = (String) request.getAttribute(StringConstant.REQ_TOKEN_KEY);
            if (StringConstant.NOT_TOKEN.equals(token)) {
                throw new ErrorException(ErrorCode.SYS_ERROR_NOT_TOKEN);
            } else {
                if (tokenService.isDevProfiles()) {
                    if (LANYUE.equals(token)) {
                        return;
                    }
                }
                //获取token的accessKey获取Redis中serectKey
                if (token.length() < 49) {
                    throw new ErrorException(ErrorCode.SYS_ERROR_TOKEN_ERROR);
                }
                String memberIdAccessKey = token.substring(0, token.length() - 24);
                String memberId = memberIdAccessKey.substring(0, memberIdAccessKey.length() - 24);
                int resultCode = tokenService.checkToken(memberId.toLowerCase(), token);
                switch (resultCode) {
                    case 0:
                        request.setAttribute(MEMBERID, memberId);
                        return;
                    case 1:
                        throw new ErrorException(ErrorCode.SYS_ERROR_TOKEN_EXPIRE);
                    case 2:
                        throw new ErrorException(ErrorCode.SYS_ERROR_TOKEN_ERROR);
                    case 3:
                        throw new ErrorException(ErrorCode.SYS_ERROR_REQUEST_TIMEOUT);
                    default:
                        throw new ErrorException(ErrorCode.SYS_ERROR_TOKEN_ERROR);
                }
            }
        }

    }


}
