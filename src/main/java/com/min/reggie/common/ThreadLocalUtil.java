package com.min.reggie.common;

/**
 * 使用ThreadLocal类获取员工的sessionId
 */
public class ThreadLocalUtil {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();



    public static Long getThreadLocal() {
        return threadLocal.get();
    }

    public static void setThreadLocal(Long id) {
        threadLocal.set(id);
    }
}
