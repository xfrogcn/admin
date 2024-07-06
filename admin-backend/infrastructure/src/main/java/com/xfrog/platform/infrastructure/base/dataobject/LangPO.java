package com.xfrog.platform.infrastructure.base.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xfrog.framework.po.AuditPO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 语言PO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@TableName("langs")
public class LangPO extends AuditPO {
    // 所属应用
    private String application;
    // 语言代码
    private String code;
    // 语言名称
    private String name;
    // 本地语言的名称
    private String localName;
    // 是否启用
    private Boolean enabled;
}
