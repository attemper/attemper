package com.github.attemper.common.param.dispatch.instance;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class InstanceJsonArgParam extends InstanceIdParam {

    protected List<String> beforeActIds;

    protected List<String> afterActIds;

    protected JsonNode jsonData;

    @Override
    public String validate() {
        return super.validate();
    }
}
