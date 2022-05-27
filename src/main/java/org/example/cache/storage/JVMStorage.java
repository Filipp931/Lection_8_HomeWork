package org.example.cache.storage;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Хранилище кэша в памяти
 * @param <T>
 */
public class JVMStorage<T> implements Storage{
    private final Map<Method, HashMap<Object[],T>> cache =  new HashMap();
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
    public boolean containsCachedValue(Method method, Object[] parameter) {
        Boolean contains = false;
        if(!cache.containsKey(method)) return false;
        for (Object[] param : cache.get(method).keySet()) {
            if(Arrays.equals(param, parameter)){
                contains = true;
                System.out.println("JVM Storage contains cache value");
            }
        }
        return contains;
    }
    /**
     * Получение кэшированного результата выполнения метода method(parameter)
     * @return значение метода method(parameter) из кэша
     */
    @Override
    public Object getCachedValue(Method method, Object[] parameter) {
        Map<Object[],T> temp = cache.get(method);
        for (Object[] param : temp.keySet()) {
            if(Arrays.equals(param, parameter))
                System.out.println("Getting cache value from JVM storage");
                return temp.get(param);
        }
        return null;
    }
    /**
     * Кэширование результирующего значения метода value = method(parameter)
     */
    @Override
    public void cachValue(Method method, Object[] parameter, Object value) {
        if(cache.containsKey(method)){
            cache.get(method).put(parameter, (T) value);
        } else {
            HashMap<Object[], T> temp = new HashMap<>();
            temp.put(parameter, (T) value);
            cache.put(method, temp);
        }
        System.out.println("Caching value to JVM storage");
    }

}
