package com.podigua.visark.core.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author: podigua
 * @create: 2021-03-18 15:51
 **/
public class ExecutorUtils {
    private final static String THREAD_NAME = "kafka-receiver-%d";
    private final static Integer CORE_POOL_SIZE = 1;
    private final static Integer MAX_POOL_SIZE = 1;
    private final static Long KEEP_ALIVE_TIME = 0L;
    private final static Integer CAPACITY = 1024;

    /**
     * 创建单一线程
     *
     * @return
     */
    public static ExecutorService single() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(THREAD_NAME).build();
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(CAPACITY), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }


    public static ExecutorService executorService(Integer size) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(THREAD_NAME).build();
        return new ThreadPoolExecutor(size, size,
                KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(CAPACITY), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }
}
