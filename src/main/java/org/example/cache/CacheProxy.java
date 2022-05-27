package org.example.cache;

import org.example.cache.service.Service;

import java.lang.reflect.Proxy;
import java.nio.file.Path;

public class CacheProxy {
    private Path storageRootDirectory;

    public CacheProxy(Path storageRootDirectory) {
        this.storageRootDirectory = storageRootDirectory;
    }

    /**
     * создание прокси
     * @param object
     * @return
     */
    public Service cache(Service object){
        return (Service) Proxy.newProxyInstance( object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                new CacheHandler(object, storageRootDirectory));
    }
}
