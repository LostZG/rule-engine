package cn.ruleengine.core.rule.strategy;

import cn.ruleengine.core.Input;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.rule.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/12/29
 * @since 1.0.0
 */
public class WhenARuleExecuteFailsStrategy implements RuleSetStrategy {


    private static WhenARuleExecuteFailsStrategy whenARuleExecuteFailsStrategy = new WhenARuleExecuteFailsStrategy();

    private WhenARuleExecuteFailsStrategy() {
    }

    public static WhenARuleExecuteFailsStrategy getInstance() {
        return whenARuleExecuteFailsStrategy;
    }

    @Override
    public List<Object> compute(List<Rule> rules, Input input, RuleEngineConfiguration configuration) {
        List<Object> actions = new ArrayList<>();
        for (Rule rule : rules) {
            Object action = rule.execute(input, configuration);
            if (action != null) {
                actions.add(action);
            } else {
                // 当有一个规则执行不成立时，终止执行
                return actions;
            }
        }
        return actions;
    }

}