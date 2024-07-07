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
    // 字典值标签语料编码 -- 用于配置字典项页面的展示文本
    private String labelLangCodeValue;
    // 字典扩展值1标签语料编码 -- 用于配置字典项页面的展示文本
    private String labelLangCodeExtValue1;
    // 字典扩展值2标签语料编码 -- 用于配置字典项页面的展示文本
    private String labelLangCodeExtValue2;
    // 字说明
    private String memo;
}
