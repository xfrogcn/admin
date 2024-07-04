package com.xfrog.platform.infrastructure.base.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xfrog.framework.po.AuditPO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@TableName("dic_items")
public class DicItemPO extends AuditPO {
    // 关联的字典ID
    private Long dicId;
    // 是否启用
    private Boolean enabled;
    // 显示名称
    private String displayText;
    // 多语言语料编码
    private String langCode;
    // 显示顺序
    private Integer displayOrder;
    // 对应值
    private String value;
    // 扩展值1
    private String extValue1;
    // 扩展值2
    private String extValue2;
    // 说明
    private String memo;
}
