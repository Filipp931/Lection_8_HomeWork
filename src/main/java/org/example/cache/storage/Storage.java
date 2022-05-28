package org.example.cache.storage;

import java.io.IOException;
import java.lang.reflect.Method;

public interface Storage<T> {
    /**
     * Проверка наличия кэшированного значения для метода method(parameter)
     * @param parameter
     * @return true - если значение было сохранено ранее
     */
    boolean containsCachedValue(Object[] parameter) throws IOException;
    /**
     * Получение кэшированного результата выполнения метода method(parameter)
     * @param parameter
     * @return значение метода method(parameter) из кэша
     */
    T getCachedValue(Object[] parameter);
    /**
     * Кэширование результирующего значения метода value = method(parameter)
     * @param parameter
     * @param value
     */
    void cachValue(Object[] parameter, T value );
}
