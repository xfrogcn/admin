package com.xfrog.platform.infrastructure.base.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xfrog.framework.po.AuditPO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 字典
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@TableName("dics")
public class DicPO extends AuditPO {
    // 字典类型
    private String type;
    // 字典名称
    private String name;
    // 字说明
    private String memo;
}
