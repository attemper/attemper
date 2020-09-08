package com.github.attemper.common.param.dispatch.job;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.attemper.common.param.CommonParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@ToString
public class JobNameWithJsonArgParam extends JobNameParam {

    protected JsonNode jsonData;
}
