package org.example.cache;

import javax.sql.rowset.Predicate;
import java.lang.reflect.Proxy;
import java.nio.file.Path;

public class CacheProxy<T> {
    private Path storageRootDirectory;

    public CacheProxy(Path storageRootDirectory) {
        this.storageRootDirectory = storageRootDirectory;
    }

    /**
     * создание прокси
     * @param object
     * @return
     */
    public T cache(T object){
        return (T) Proxy.newProxyInstance( object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                new CacheHandler<T>(object, storageRootDirectory));
    }
}
