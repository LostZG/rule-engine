package cn.ruleengine.web.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.core.decisiontable.CollHead;
import cn.ruleengine.core.decisiontable.DecisionTable;
import cn.ruleengine.core.rule.GeneralRule;
import cn.ruleengine.core.rule.Parameter;
import cn.ruleengine.core.value.Element;
import cn.ruleengine.core.value.Function;
import cn.ruleengine.core.value.Value;
import cn.ruleengine.core.value.Variable;
import cn.ruleengine.web.store.entity.RuleEngineElement;
import cn.ruleengine.web.store.manager.RuleEngineElementManager;
import cn.ruleengine.core.Engine;
import cn.ruleengine.core.condition.Condition;
import cn.ruleengine.core.condition.ConditionGroup;
import cn.ruleengine.core.condition.ConditionSet;
import cn.ruleengine.core.exception.ValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/8/27
 * @since 1.0.0
 */
@Slf4j
@Service
public class ParameterService {

    @Resource
    private Engine engine;
    @Resource
    private RuleEngineElementManager ruleEngineElementManager;

    /**
     * 统计引用此决策表的所有的元素
     *
     * @param decisionTable decisionTable
     * @return Parameter
     */
    public Set<Parameter> getParameters(DecisionTable decisionTable) {
        Set<Integer> elementIds = new HashSet<>();
        List<CollHead> collHeads = decisionTable.getCollHeads();
        for (CollHead collHead : collHeads) {
            Value value = collHead.getLeftValue();
            this.getFromVariableElement(elementIds, value);
        }
        if (decisionTable.getDefaultActionValue() != null) {
            this.getFromVariableElement(elementIds, decisionTable.getDefaultActionValue());
        }
        return this.getParameters(elementIds);
    }

    private Set<Parameter> getParameters(Set<Integer> elementIds) {
        if (CollUtil.isEmpty(elementIds)) {
            return Collections.emptySet();
        }
        List<RuleEngineElement> engineElementList = this.ruleEngineElementManager.lambdaQuery().in(RuleEngineElement::getId, elementIds).list();
        return engineElementList.stream().map(m -> {
            Parameter parameter = new Parameter();
            parameter.setName(m.getName());
            parameter.setCode(m.getCode());
            parameter.setValueType(m.getValueType());
            return parameter;
        }).collect(Collectors.toSet());
    }

    /**
     * 统计引用此规则的所有的元素
     *
     * @param rule rule
     * @return Parameter
     */
    public Set<Parameter> getParameters(GeneralRule rule) {
        Set<Integer> elementIds = new HashSet<>();
        ConditionSet conditionSet = rule.getConditionSet();
        List<ConditionGroup> conditionGroups = conditionSet.getConditionGroups();
        for (ConditionGroup conditionGroup : conditionGroups) {
            List<Condition> conditions = conditionGroup.getConditions();
            for (Condition condition : conditions) {
                this.getFromVariableElement(elementIds, condition.getRightValue());
                this.getFromVariableElement(elementIds, condition.getLeftValue());
            }
        }
        Value value = rule.getActionValue();
        this.getFromVariableElement(elementIds, value);
        Value defaultActionValue = rule.getDefaultActionValue();
        if (defaultActionValue != null) {
            this.getFromVariableElement(elementIds, defaultActionValue);
        }
        return this.getParameters(elementIds);
    }

    /**
     * 统计元素/变量中使用到的元素id
     *
     * @param elementIds 元素id
     * @param value      value
     */
    private void getFromVariableElement(Set<Integer> elementIds, Value value) {
        if (value instanceof Variable) {
            Value val = this.engine.getEngineVariable().getVariable(((Variable) value).getVariableId());
            if (val instanceof Function) {
                Function function = (Function) val;
                Map<String, Value> param = function.getParam();
                Collection<Value> values = param.values();
                for (Value v : values) {
                    if (v instanceof Element) {
                        Element element = (Element) v;
                        elementIds.add(element.getElementId());
                    } else if (v instanceof Variable) {
                        try {
                            this.getFromVariableElement(elementIds, v);
                        } catch (StackOverflowError e) {
                            log.error("堆栈溢出错误", e);
                            throw new ValidException("请检查规则变量是否存在循环引用");
                        }
                    }
                }
            }
        } else if (value instanceof Element) {
            Element element = (Element) value;
            elementIds.add(element.getElementId());
        }
    }

}