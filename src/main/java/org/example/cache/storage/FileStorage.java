package org.example.cache.storage;

import java.lang.reflect.Method;

public class FileStorage implements Storage {
    public FileStorage(String fileNamePrefix, boolean zip, String key, Class[] identityBy) {

    }

    @Override
    public boolean containsCachedValue(Method method, Object[] parameter) {
        return false;
    }

    @Override
    public Object getCachedValue(Method method, Object[] parameter) {
        return null;
    }

    @Override
    public void cachValue(Method method, Object[] parameter, Object value) {

    }
}
