package org.example.cache.storage;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * Вспомогательный класс для записи пары Args|value в файл

 */
public class CacheRow implements Externalizable {
    private Object[] args;
    private Object value;

    public CacheRow(Object[] args, Object value) {
        this.args = args;
        this.value = value;
    }

    public CacheRow() {
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheRow cacheRow = (CacheRow) o;
        return Arrays.equals(args, cacheRow.args) && value.equals(cacheRow.value);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(value);
        result = 31 * result + Arrays.hashCode(args);
        return result;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(args);
        out.writeObject(value);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
       this.args = (Object[]) in.readObject();
       this.value =  in.readObject();
    }
}
