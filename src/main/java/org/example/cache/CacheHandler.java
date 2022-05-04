package org.example.cache;

import org.example.cache.storage.FileStorage;
import org.example.cache.storage.JVMStorage;
import org.example.cache.storage.Storage;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CacheHandler<T> implements InvocationHandler {
    private final T delegate;
    private CacheProperties cacheProperties;
    private Path storageRootDirectory;
    Map<Method, Storage> storageMap;


    public CacheHandler(T delegate, Path storageRootDirectory) {
        this.delegate = delegate;
        this.storageRootDirectory = storageRootDirectory;
        this.storageMap = new HashMap<>();
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Storage storage;
        if (method.isAnnotationPresent(Cache.class)) {
            cacheProperties = getCachePropertiesFromAnnotation(method);
            //получения хранилища для метода, либо создание нового в соответствии с параметрами аннотации
            storage = storageMap.containsKey(method) ?
                    storageMap.get(method) : storageMap.put(method, setStorage(method));
            //либо получение значения из кэша, либо вычисление и кэширование
            if (storage.containsCachedValue(method, args)) {
                return storage.getCachedValue(method, args);
            } else {
                Object result = method.invoke(proxy, args);
                storage.cachValue(method, args, result);
                return result;
            }
        }
        //если метод не аннотирован кэшем, то просто вычисление значения
        return method.invoke(proxy, args);
    }

    /**
     * получение параметров кэширования из аннотации метода
     * @param method
     * @return CacheProperties
     */
    private CacheProperties getCachePropertiesFromAnnotation(Method method){
        Cache cache = method.getAnnotation(Cache.class);
        String cacheType = cache.cacheType();
        String fileNamePrefix = cache.fileNamePrefix().equals("") ? method.getName() : cache.fileNamePrefix();
        String key = cache.key().equals("") ? method.getName() : cache.key();
        Class[] identityBy = Arrays.asList(cache.identityBy()).isEmpty() ? method.getParameterTypes() : null;
        return new CacheProperties(cacheType,fileNamePrefix, cache.zip(), identityBy,
                cache.listMaxCacheCount(), key);
    }

    /**
     * Установка типа кэша в зависимости от параметров аннотации
     * @param method
     */
    private Storage setStorage(Method method) throws IOException, ClassNotFoundException {
        if(cacheProperties.getCacheType().equals(CacheProperties.CACHE_TYPE_INFILE)){
            return new FileStorage(storageRootDirectory, cacheProperties.getFileNamePrefix(), cacheProperties.getZip(),
                    cacheProperties.getIdentityBy());
        } else {
            return new JVMStorage(cacheProperties.getKey(),
                                  cacheProperties.getIdentityBy());
        }
    }

}

