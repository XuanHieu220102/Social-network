package org.example.socialbe.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;

public class LogUtil {
    public static String generateTraceId() {
        String traceId = RandomStringUtils.randomAlphanumeric(20);
        setTraceId(traceId);
        return traceId;
    }

    public static String getTraceId() {
        return MDC.get("traceId");
    }

    public static void setTraceId(String traceId) {
        MDC.put("traceId", traceId);
    }
}
