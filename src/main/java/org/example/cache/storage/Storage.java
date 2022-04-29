package org.example.cache.storage;

import java.lang.reflect.Method;

public interface Storage<T> {
    /**
     * Проверка наличия кэшированного значения для метода method(parameter)
     * @param method
     * @param parameter
     * @return true - если значение было сохранено ранее
     */
    boolean containsCachedValue(Method method, Object[] parameter);
    /**
     * Получение кэшированного результата выполнения метода method(parameter)
     * @param method
     * @param parameter
     * @return значение метода method(parameter) из кэша
     */
    T getCachedValue(Method method, Object[] parameter);
    /**
     * Кэширование результирующего значения метода value = method(parameter)
     * @param method
     * @param parameter
     * @param value
     */
    void cachValue(Method method, Object[] parameter, T value );
}
