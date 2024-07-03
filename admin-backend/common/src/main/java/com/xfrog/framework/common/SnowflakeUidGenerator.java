package com.xfrog.framework.common;


import cn.hutool.core.util.IdUtil;

public class SnowflakeUidGenerator implements UidGenerator {
    @Override
    public long nextId() {
        return IdUtil.getSnowflakeNextId();
    }
}
