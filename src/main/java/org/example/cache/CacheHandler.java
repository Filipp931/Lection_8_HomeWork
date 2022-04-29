package org.example.cache;

import org.example.cache.storage.FileStorage;
import org.example.cache.storage.JVMStorage;
import org.example.cache.storage.Storage;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.file.Path;

public class CacheHandler<T> implements InvocationHandler {
    private final T delegate;
    private CacheProperties cacheProperties;
    private Path storageRootDirectory;


    private Storage storage;
    public CacheHandler(T delegate, Path storageRootDirectory) {
        this.delegate = delegate;
        this.storageRootDirectory = storageRootDirectory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        if(method.isAnnotationPresent(Cache.class)){
            cacheProperties = getCachePropertiesFromAnnotation(method);
            //получить параметры из кэша иначе вызвать метод TODO
            if(Strorage. contains(method, args)){
                return  = Strorage.get(method, args);
            } else {
                res = method.invoke(proxy, args);

            }


            setCacheType(method);
        } else {
            method.invoke(proxy, args);
        }
    return
    }

    /**
     * получение параметров кэширования из аннотации метода
     * @param method
     * @return CacheProperties
     */
    private CacheProperties getCachePropertiesFromAnnotation(Method method){
        Cache cache = method.getDeclaredAnnotation(Cache.class);
        return new CacheProperties(cache.cacheType(),cache.fileNamePrefix(),
                cache.zip(), cache.identityBy(), cache.listMaxCacheCount(), cache.key());
    }

    /**
     * Установка типа кэша в зависимости от параметров аннотации
     * @param method
     */
    private void setCacheType(Method method){
        if(cacheProperties.getCacheType().equals(CacheProperties.CACHE_TYPE_INFILE)){
            String fileNamePrefix = cacheProperties.getFileNamePrefix().equals("") ? method.getName() : cacheProperties.getFileNamePrefix();
            String key = cacheProperties.getKey().equals("") ? method.getName() : cacheProperties.getKey();
            storage = new FileStorage(fileNamePrefix, cacheProperties.getZip(), key, cacheProperties.getIdentityBy());
        } else {
            storage = new JVMStorage(cacheProperties.get);
        }
    }

}

