package cn.ruleengine.web.store.entity;

import cn.ruleengine.core.rule.AbnormalAlarm;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author dqw
 * @since 2020-12-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RuleEngineGeneralRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String code;

    private Integer ruleId;

    private String description;

    private Integer workspaceId;

    private String workspaceCode;

    private Integer createUserId;

    private String createUserName;

    /**
     * 当前规则的最新状态
     */
    private Integer status;
    /**
     * 当前最新的版本号
     */
    private String currentVersion;
    /**
     * 当前已发布的版本号
     */
    private String publishVersion;

    private Integer enableDefaultAction;

    private String defaultActionValue;

    private Integer defaultActionType;

    private String defaultActionValueType;

    /**
     * 注意，规则模拟运行不会触发
     * <p>
     * {@link AbnormalAlarm}
     */
    private String abnormalAlarm;

    private String referenceData;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;


}
