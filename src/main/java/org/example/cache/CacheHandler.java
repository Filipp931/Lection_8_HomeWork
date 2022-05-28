package org.example.cache;

import org.example.cache.storage.FileStorage;
import org.example.cache.storage.JVMStorage;
import org.example.cache.storage.Storage;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;

public class CacheHandler<T> implements InvocationHandler {
    private final T delegate;
    private CacheProperties cacheProperties;
    private Path storageRootDirectory;
    private Object[] argsForCache;
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
            argsForCache = getParamsForCache(args);
            //получения хранилища для метода, либо создание нового в соответствии с параметрами аннотации
            if (storageMap.containsKey(method)) {
                storage = storageMap.get(method);
            } else {
                storageMap.put(method, setStorage(method));
                storage = storageMap.get(method);
            }

            //либо получение значения из кэша, либо вычисление и кэширование
            if (storage.containsCachedValue(method, argsForCache)) {
                System.out.println(Thread.currentThread().getName() + " Getting value from cache");
                return storage.getCachedValue(method, argsForCache);
            } else {
                Object result = method.invoke(delegate, args);
                storage.cachValue(method, argsForCache,  trimResult(result));
                return result;
            }
        }
        //если метод не аннотирован кэшем, то просто вычисление значения
        return method.invoke(delegate, args);
    }

    /**
     * Обрезка списка
     */
    private Object trimResult(Object result) {
        if (Arrays.asList(result.getClass().getInterfaces()).contains(List.class)) {
            return new ArrayList<>(((List) result).subList(0, cacheProperties.getListMaxCacheCount()));
        } else return result;
    }
    /**
     * Получение параметров, которые учитываются при кэшировании
     */
    private Object[] getParamsForCache(Object[] args){
        if(cacheProperties.getIdentityBy().length == 0) {
            return args;
        } else {
            List<Object> result = new ArrayList<>();
            for (Object arg: args) {
                if(Arrays.asList(cacheProperties.getIdentityBy()).contains(arg.getClass())){
                    result.add(arg);
                }
            }
            return result.toArray();
        }
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
        Class[] identityBy = Arrays.asList(cache.identityBy()).isEmpty() ? method.getParameterTypes() : cache.identityBy();
        return new CacheProperties(cacheType,fileNamePrefix, cache.zip(), identityBy,
                cache.listMaxCacheCount(), key);
    }


    /**
     * Установка типа кэша в зависимости от параметров аннотации
     */
    private Storage setStorage(Method method) throws IOException, ClassNotFoundException {
        if(cacheProperties.getCacheType().equals(CacheProperties.CACHE_TYPE_INFILE)){
            Storage fileStorage = new FileStorage(storageRootDirectory, cacheProperties.getFileNamePrefix(), cacheProperties.getZip(),
                    argsForCache);
            storageMap.put(method, fileStorage);
            return fileStorage;
        } else {
            Storage JVMStorage = new JVMStorage(cacheProperties.getKey(),
                    cacheProperties.getIdentityBy());
            storageMap.put(method, JVMStorage);
            return JVMStorage;
        }
    }

}

