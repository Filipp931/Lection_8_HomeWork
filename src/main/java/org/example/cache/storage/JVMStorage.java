package org.example.cache.storage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Хранилище кэша в памяти
 * @param <T>
 */
public class JVMStorage<T> implements Storage<T>{
    private final Map<Object[],T> cache =  new HashMap();
    String key;
    Object[] args;

    public JVMStorage(String key, Object[] args) {
        this.key = key;
        this.args = args;
    }

    /**
     * Проверка наличия кэшированного значения для метода method(parameter)
     * @return true - если значение было сохранено ранее
     */
    @Override
    public boolean containsCachedValue(Object[] parameter) {
        boolean contains = false;
        for (Object[] param : cache.keySet()) {
            if(Arrays.equals(param, parameter)){
                contains = true;
                System.out.println(Thread.currentThread().getName() + " - JVM Storage contains cache value");
            }
        }
        return contains;
    }
    /**
     * Получение кэшированного результата выполнения метода method(parameter)
     * @return значение метода method(parameter) из кэша
     */
    @Override
    public T getCachedValue(Object[] parameter) {
        for (Object[] param : cache.keySet()) {
            if(Arrays.equals(param, parameter))
                System.out.println(Thread.currentThread().getName() +  " - Getting cache value from JVM storage");
                return cache.get(param);
        }
        return null;
    }
    /**
     * Кэширование результирующего значения метода value = method(parameter)
     */
    @Override
    public void cachValue(Object[] parameter, T value) {
        cache.put(parameter, value);
        System.out.println(Thread.currentThread().getName() +  " - Caching value to JVM storage");
    }

}
