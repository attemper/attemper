package com.sse.attemper.common.result.sys.user;

import com.sse.attemper.common.result.sys.resource.Resource;
import com.sse.attemper.common.result.sys.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ldang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    /** 登录用户的信息 */
    protected User user;

    /** 标签信息*/
    protected List<Tag> tags;

    /** 该用户拥有的资源 */
    protected List<Resource> resources;

}
