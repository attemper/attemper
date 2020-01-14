package com.github.attemper.core.ext.condition;

import com.github.attemper.common.enums.ConditionType;
import com.github.attemper.common.result.dispatch.condition.Condition;
import com.github.attemper.common.result.dispatch.condition.sub.LocalFileConditionResult;
import com.github.attemper.config.base.util.BeanUtil;
import com.github.attemper.core.ext.el.ELUtil;
import com.github.attemper.core.util.FileUtil;

import java.io.File;
import java.util.Map;

@ConditionStrategyType(ConditionType.LOCAL_FILE)
public class LocalFileConditionStrategy implements ConditionStrategy<LocalFileConditionResult> {

    @Override
    public boolean validate(Condition condition, Map<String, Object> variableMap) {
        LocalFileConditionResult localFileCondition = parse(condition);
        // because \ was an escape character,so \{xxx} can not be parsed
        String filePath = FileUtil.optimizePath(localFileCondition.getFilePath() + '/' + localFileCondition.getFileName());
        String parsedFilePath = ELUtil.parseExpression(filePath, variableMap);
        File file = new File(parsedFilePath);
        return file.exists();
    }

    @Override
    public LocalFileConditionResult parse(Condition condition) {
        LocalFileConditionResult localFileConditionResult =
                BeanUtil.jsonStr2Bean(condition.getContent(), LocalFileConditionResult.class);
        localFileConditionResult
                .setConditionName(condition.getConditionName())
                .setTenantId(condition.getTenantId());
        return localFileConditionResult;
    }

}
