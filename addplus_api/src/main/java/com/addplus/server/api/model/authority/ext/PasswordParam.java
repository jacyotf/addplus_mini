package com.addplus.server.api.model.authority.ext;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author fuyq
 * @date 2019/3/12
 */
@Data
@Accessors(chain = true)
public class PasswordParam {

    private String oldPassword;

    private String password;

    private String confirmPassword;
}
