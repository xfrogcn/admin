package com.xfrog.framework.oplog;

import org.slf4j.MDC;

public class OpLogMDC {
    public static final String BIZ_CODE_KEY = "bizCode";

    public static void put(String name, String value) {
        MDC.put(name, value);
    }

    public static void putBizCode(String bizCode) {
        MDC.put(BIZ_CODE_KEY, bizCode);
    }
}
