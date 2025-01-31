package cn.ruleengine.core.decisiontable;

import cn.ruleengine.core.DecisionTableEngine;
import cn.ruleengine.core.DefaultInput;
import cn.ruleengine.core.OutPut;
import cn.ruleengine.core.RuleEngineConfiguration;
import cn.ruleengine.core.condition.Operator;
import cn.ruleengine.core.value.Constant;
import cn.ruleengine.core.value.Element;
import cn.ruleengine.core.value.ValueType;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author dingqianwen
 * @date 2020/12/18
 * @since 1.0.0
 */
public class DecisionTableTest {

    /**
     * <pre>
     *     priority  41（eq）     action(default)
     *       4         41         41
     *       3         null       33333
     *       3         sd         3sd
     *       4         null       4
     *       ...
     *
     * </pre>
     */
    public static void main(String[] args) {
        DecisionTable decisionTable = new DecisionTable();
        decisionTable.setCode("test");
        decisionTable.setWorkspaceCode("test");
        decisionTable.setStrategyType(DecisionTableStrategyType.HIGHEST_PRIORITY_SINGLE);
        // 默认值
        decisionTable.setDefaultActionValue(new Constant("default", ValueType.STRING));
        decisionTable.addCollHead(new CollHead(new Element(0, "test", ValueType.STRING), Operator.EQ));
        {
            Row row = new Row();
            row.setPriority(4);
            row.addColl(new Coll(new Constant("41", ValueType.STRING)));
            row.setAction(new Constant("41", ValueType.STRING));
            decisionTable.addRow(row);
        }
        {
            Row row = new Row();
            row.setPriority(4);
            row.addColl(new Coll(new Constant("sd", ValueType.STRING)));
            row.setAction(new Constant("是4sd", ValueType.STRING));
            decisionTable.addRow(row);
        }
        {
            Row row = new Row();
            row.setPriority(3);
            row.addColl(null);
            row.setAction(new Constant("33333", ValueType.STRING));
            decisionTable.addRow(row);
        }
        {
            Row row = new Row();
            row.setPriority(3);
            row.addColl(new Coll(new Constant("sd", ValueType.STRING)));
            row.setAction(new Constant("3sd", ValueType.STRING));
            decisionTable.addRow(row);
        }
        {
            Row row = new Row();
            row.setPriority(3);
            row.addColl(new Coll(new Constant("sd", ValueType.STRING)));
            row.setAction(new Constant("水电费水电费", ValueType.STRING));
            decisionTable.addRow(row);
        }
        {
            Row row = new Row();
            row.setPriority(4);
            row.addColl(new Coll(new Constant(null, ValueType.STRING)));
            row.setAction(new Constant("4", ValueType.STRING));
            decisionTable.addRow(row);
        }

        DecisionTableEngine decisionTableEngine = new DecisionTableEngine(new RuleEngineConfiguration());
        decisionTableEngine.addDecisionTable(decisionTable);

        DefaultInput input = new DefaultInput();
        input.put("test", "sd");
        OutPut outPut = decisionTableEngine.execute(input, "test", "test");
        System.out.println(outPut.getClassType());
        System.out.println(outPut.getValue());
    }

}
