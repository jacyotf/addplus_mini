package com.addplus.server.web.config.token;

import com.addplus.server.api.constant.StringConstant;
import com.addplus.server.api.model.base.Token;
import com.addplus.server.api.utils.security.AESUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 类名: TokenService
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/6/22 上午10:53
 * @description 类描述: Token类信息生成
 */
@Service
public class TokenService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${spring.profiles.active}")
    private String actice;

    private Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    /**
     * 方法描述：生成Token码
     *
     * @param
     * @return
     * @throws Exception
     * @author zhangjiehang
     * @date 2018/6/22 上午11:07
     */
    public Token getTokenKey(String memberId) {
        //删除原有的key
        removeTokenKey(memberId);
        Token token = new Token();
        String accessKey = new ObjectId().toString();
        token.setAccessKey(memberId + accessKey);
        token.setSecretKey(new ObjectId().toString());
        token.setMemId(memberId);
        redisTemplate.opsForValue().set(com.addplus.server.api.constant.StringConstant.TOKEN_REDIS_PREFIX + memberId, JSON.toJSONString(token), 24, TimeUnit.HOURS);
        return token;
    }

    public void removeTokenKey(String memberId) {
        if (redisTemplate.hasKey(com.addplus.server.api.constant.StringConstant.TOKEN_REDIS_PREFIX + memberId)) {
            redisTemplate.delete(com.addplus.server.api.constant.StringConstant.TOKEN_REDIS_PREFIX + memberId);
        }
    }

    @Async
    public void refreshToken(String memberId) {
        boolean isExit = redisTemplate.hasKey(StringConstant.TOKEN_REDIS_PREFIX + memberId);
        if (isExit) {
            boolean isUpdateSuccess = redisTemplate.expire(StringConstant.TOKEN_REDIS_PREFIX + memberId, 2, TimeUnit.DAYS);
            if (!isUpdateSuccess) {
                LOGGER.error("更新token失败，memberId:" + memberId);
            }
        }
    }

    /**
     * 方法描述：刷新已经存在的token
     *
     * @param memberId 用户主键id
     * @param token    用户传进来的token
     * @return Integer 0:成功,1:不存在token,2:非法token,3:请求超时
     * @throws Exception
     * @author zhangjiehang
     * @date 2019/3/15 11:31 AM
     */
    public Integer checkToken(String memberId, String token) {
        boolean isExit = redisTemplate.hasKey(StringConstant.TOKEN_REDIS_PREFIX + memberId.toLowerCase());
        if (isExit) {
            Object secretKeyObj = redisTemplate.opsForValue().get(StringConstant.TOKEN_REDIS_PREFIX + memberId.toLowerCase());
            Token tokenOrign = JSON.parseObject(secretKeyObj.toString(), Token.class);
            //解密字符是时间戳
            String signDecrypt = AESUtils.decryptAES(token.substring(token.length() - 24, token.length()), tokenOrign.getSecretKey().substring(8, 24), 0);
            if (StringUtils.isBlank(signDecrypt)) {
                return 2;
            } else {
                //校验当前请求是否还在有效期
                long requestTimeStamp = Long.valueOf(signDecrypt);
                long nowTimeStamp = System.currentTimeMillis();
                long timeDifference = Math.abs(nowTimeStamp - requestTimeStamp);
                //获取后面校验部分,当前设定10s为有效请求时间
                if (100000 > timeDifference) {
                    this.refreshToken(memberId.toLowerCase());
                    return 0;
                } else {
                    return 3;
                }
            }
        } else {
            return 1;
        }
    }

    public Boolean isDevProfiles() {
        if ("dev".equals(actice)) {
            return true;
        }
        return false;
    }

}
