package com.sse.attemper.common.param.sys.user;

import com.sse.attemper.common.param.PageSortParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListParam extends PageSortParam {

    protected String userName;

    protected String displayName;

    protected String email;

    protected String mobile;
}
