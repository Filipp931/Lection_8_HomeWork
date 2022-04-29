package org.example.cache.storage;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JVMStorage<T> implements Storage{
    private Map<Method, HashMap<Object[],T>> cache =  new HashMap();
    /**
     * Проверка наличия кэшированного значения для метода method(parameter)
     * @param method
     * @param parameter
     * @return true - если значение было сохранено ранее
     */
    @Override
    public boolean containsCachedValue(Method method, Object[] parameter) {
        Boolean contains = false;
        if(!cache.containsKey(method)) return false;
        for (Object[] param : cache.get(method).keySet()) {
            if(Arrays.equals(param, parameter))
                contains = true;
        }
        return contains;
    }
    /**
     * Получение кэшированного результата выполнения метода method(parameter)
     * @param method
     * @param parameter
     * @return значение метода method(parameter) из кэша
     */
    @Override
    public Object getCachedValue(Method method, Object[] parameter) {
        if(!containsCachedValue(method,parameter)) return null;
        Map<Object[],T> temp = cache.get(method);
        for (Object[] param : temp.keySet()) {
            if(Arrays.equals(param, parameter))
                return temp.get(param);
        }
        return null;
    }
    /**
     * Кэширование результирующего значения метода value = method(parameter)
     * @param method
     * @param parameter
     * @param value
     */
    @Override
    public void cachValue(Method method, Object[] parameter, Object value) {
        HashMap<Object[], T> temp = new HashMap<>();
        temp.put(parameter, (T) value);
        cache.put(method, temp);
    }

}