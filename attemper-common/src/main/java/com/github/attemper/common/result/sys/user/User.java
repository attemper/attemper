package com.github.attemper.common.result.sys.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * 用户
 * @auth ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User{

	protected String userName;

	protected String displayName;

	protected String password;

	protected String email;

	protected String mobile;

	protected Integer status;

	protected Date createTime;

	protected Date updateTime;

	protected String tenantId;

	@Override
	public boolean equals(Object o) {
	    if (o == null || !(o instanceof User)) {
	        return false;
        }
        User user = (User) o;
    return (StringUtils.equalsIgnoreCase(userName, user.userName)
            || StringUtils.equals(email, user.email)
            || StringUtils.equals(mobile, user.mobile))
        && StringUtils.equals(password, user.password)
        && status == user.status
        && StringUtils.equals(tenantId, user.tenantId);
	}

}
