package org.example;


import org.example.cache.CacheProxy;
import org.example.cache.service.Service;
import org.example.cache.service.serviceImpl.ServiceImpl;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Unit test for simple App.
 */
public class AppTest 
{



    @Test
    public void JVMStoreTest1() {
        CacheProxy cacheProxy = new CacheProxy(Paths.get("test"));
        Service service =  cacheProxy.cache(new ServiceImpl());
        System.out.println("JVM STORAGE TEST");
        System.out.println("===============================================");
        System.out.println(service.work("work").toString());
        System.out.println(service.work("work").toString());
        System.out.println("===============================================");
        System.out.println(service.work("test").toString());
        System.out.println(service.work("test").toString());
    }
    @Test
    public void FileStorage() {
        CacheProxy cacheProxy = new CacheProxy(Paths.get("test"));
        Service service =  cacheProxy.cache(new ServiceImpl());
        System.out.println("FILE STORAGE TEST");
        System.out.println("===============================================");
        System.out.println(service.run("work", 11.3, new Date()));
        System.out.println(service.run("work", 11.3, new Date()));
        System.out.println(service.run("work", 18.1, new Date()));
        System.out.println("===============================================");
        System.out.println(service.run("test", 11.3, new Date()));
        System.out.println(service.run("test", 11.3, new Date()));
    }

}

