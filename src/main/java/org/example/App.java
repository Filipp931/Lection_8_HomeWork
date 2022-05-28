package org.example;

import org.example.cache.CacheProxy;
import org.example.cache.service.Service;
import org.example.cache.service.serviceImpl.ServiceImpl;

import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CacheProxy cacheProxy = new CacheProxy(Paths.get("test"));
        Service service =  cacheProxy.cache(new ServiceImpl());
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Runnable runnableTask = () -> {
            System.out.printf(Thread.currentThread().getName()+ service.work("work"));
            System.out.printf(Thread.currentThread().getName()+ service.work("work"));
            System.out.printf(Thread.currentThread().getName()+ service.work("work"));
            System.out.printf(Thread.currentThread().getName()+ service.work("work"));
        };
        for (int i = 0; i < 5; i++) {
            executor.execute(runnableTask);
        }
    }
}
