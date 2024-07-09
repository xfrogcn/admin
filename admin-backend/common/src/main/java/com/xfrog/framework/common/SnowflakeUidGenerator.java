package com.xfrog.framework.common;


import cn.hutool.core.util.IdUtil;

public class SnowflakeUidGenerator implements UidGenerator {

    public static final SnowflakeUidGenerator INSTANCE = new SnowflakeUidGenerator();

    @Override
    public long nextId() {
        return IdUtil.getSnowflakeNextId();
    }
}
