package com.github.attemper.core.ext.condition;

import com.github.attemper.common.exception.RTException;
import org.reflections.Reflections;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class ConditionStrategyService {

    private static Map<Integer, Class<? extends ConditionStrategy>> map;

    /**
     * after init, the map will contain all condition types
     */
    static {
        Reflections reflections = new Reflections(ConditionStrategy.class.getPackage().getName());
        Set<Class<? extends ConditionStrategy>> subTypeSet = reflections.getSubTypesOf(ConditionStrategy.class);
        map = new HashMap<>(subTypeSet.size());
        for (Class<? extends ConditionStrategy> subType : subTypeSet) {
            ConditionStrategyType annotation = subType.getAnnotation(ConditionStrategyType.class);
            if (map.containsKey(annotation.value().getValue())) {
                throw new DuplicateKeyException("type is duplicated:" + annotation.value().getValue());
            }
            map.put(annotation.value().getValue(), subType);
        }
    }

    public ConditionStrategy get(int type) {
        Class<? extends ConditionStrategy> clazz = map.get(type);
        if (clazz == null) {
            throw new RTException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "condition strategy is null");
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RTException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
