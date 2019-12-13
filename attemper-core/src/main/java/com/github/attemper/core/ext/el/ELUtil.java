package com.github.attemper.core.ext.el;

import org.camunda.bpm.engine.impl.javax.el.ExpressionFactory;
import org.camunda.bpm.engine.impl.javax.el.ValueExpression;
import org.camunda.bpm.engine.impl.juel.ExpressionFactoryImpl;
import org.camunda.bpm.engine.impl.juel.SimpleContext;

import java.util.Map;

/**
 * util for compute expression language(el)
 * http://juel.sourceforge.net
 */
public class ELUtil {

    /**
     * parse expression with map's key and value
     * @param expression like abc${xxx}de${yyy}fg
     * @param variableMap contains key like xxx and yyy
     * @return
     */
    public static String parseExpression(String expression, Map<String, Object> variableMap) {
        if (variableMap == null || variableMap.isEmpty()) {
            return expression;
        }
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        for (Map.Entry<String, Object> entry : variableMap.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                ValueExpression valueExpression = factory.createValueExpression(entry.getValue(), entry.getValue().getClass());
                context.setVariable(entry.getKey(), valueExpression);
            }
        }
        ValueExpression valueExpression = factory.createValueExpression(context, expression, String.class);
        return (String) valueExpression.getValue(context);
    }
}
