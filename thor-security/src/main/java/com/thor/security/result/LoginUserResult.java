package com.thor.security.result;

import com.thor.core.entity.Resource;
import com.thor.core.entity.Tag;
import com.thor.core.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /** access token based on jwt*/
    private String token;

    /** 登录用户的信息 */
    private User user;

    /** 标签信息*/
    private List<Tag> tags;

    /** 该用户拥有的资源 */
    private List<Resource> resources;
}
