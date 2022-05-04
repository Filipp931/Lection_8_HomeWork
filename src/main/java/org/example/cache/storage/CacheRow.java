package org.example.cache.storage;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Вспомогательный класс для записи пары Args|value в файл
 * @param <T>
 */
public class CacheRow<T> implements Serializable {
    private Object[] args;
    private T value;

    public CacheRow(Object[] args, T value) {
        this.args = args;
        this.value = value;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheRow<?> cacheRow = (CacheRow<?>) o;
        return Arrays.equals(args, cacheRow.args) && value.equals(cacheRow.value);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(value);
        result = 31 * result + Arrays.hashCode(args);
        return result;
    }
}
