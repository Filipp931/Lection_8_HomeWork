package org.example.cache.storage;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Хранилище для кэширования в файл
 * @param <T>
 */
public class FileStorage<T> implements Storage {
    private Map<Object[], T> cache = new HashMap<>();
    String fileNamePrefix;
    boolean zip;
    Class[] identityBy;
    Path storageRootDirectory;
    Path pathToCacheFile;

    public FileStorage(Path storageRootDirectory, String fileNamePrefix, boolean zip, Class[] identityBy) throws IOException, ClassNotFoundException {
        this.fileNamePrefix = fileNamePrefix;
        this.zip = zip;
        this.identityBy = identityBy;
        this.storageRootDirectory = storageRootDirectory;


        //автоматическое создание пути к файлу
        pathToCacheFile = getPathToFile();
        //проверка файла на существование
        if(Files.exists(pathToCacheFile) && Files.isRegularFile(pathToCacheFile)){
            readCacheFromFile(); //читаем кэш
        } else {
            createCacheFile(pathToCacheFile); //создание пустого файла
        }


    }

    /**
     * Считывание кэша из файла(только при инициализации)
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readCacheFromFile() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = getCorrectInputStream(pathToCacheFile)) {
            while (true) {
                try {
                    if(ois == null) break;
                    CacheRow cacheRow = (CacheRow) ois.readObject();
                    cache.put(cacheRow.getArgs(), (T) cacheRow.getValue());
                } catch (EOFException e) {
                    break;
                }
            }
        }
    }

    /**
     * генерирование пути к файлу с кэшем
     * @return
     */
    private Path getPathToFile(){
        StringBuilder filePath = new StringBuilder();
        filePath.append(storageRootDirectory).append("\\").append(fileNamePrefix);
        if(zip) {
            filePath.append(".zip");
        } else {
            filePath.append(".cache");
        }
        return Paths.get(filePath.toString());
    }
    /**
     * Получение параметров, которые учитываются при кэшировании
     * @param args
     * @return
     */
    private Object[] getParamsForCache(Object[] args){
        if(identityBy.length == 0) {
            return args;
        } else {
            List<Object> result = new ArrayList<>();
            for (Object arg: args) {
                if(Arrays.asList(identityBy).contains(arg.getClass())){
                    result.add(arg);
                }
            }
            return result.toArray();
        }
    }
    @Override
    public boolean containsCachedValue(Method method, Object[] parameter) throws IOException {
        Object[] args = getParamsForCache(parameter);
        return cache.containsKey(args);
    }

    @Override
    public Object getCachedValue(Method method, Object[] parameter) {
        Object[] args = getParamsForCache(parameter);
        return cache.get(args);
    }

    @Override
    public void cachValue(Method method, Object[] parameter, Object value) {
        Object[] args = getParamsForCache(parameter);
        CacheRow cacheRow = new CacheRow(args, value);
        try (ObjectOutputStream oos = getCorrectOutputStream(pathToCacheFile)) {
            oos.writeObject(cacheRow);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cache.put(args, (T) value); //TODO
    }


    private void createCacheFile(Path path) throws IOException {
        Files.createFile(path);
    }

    /**
     * открытие потока для записи
     * @param path
     * @return
     * @throws IOException
     */
    private ObjectOutputStream getCorrectOutputStream(Path path) throws IOException {
        if(zip){
            return new ObjectOutputStream(new ZipOutputStream(Files.newOutputStream(path)));
        } else {
            return new ObjectOutputStream(Files.newOutputStream(path));
        }
    }

    /**
     * получание потока для чтения
     * @param path
     * @return
     * @throws IOException
     */
    private ObjectInputStream getCorrectInputStream(Path path) throws IOException {
        if(Files.size(path) == 0) return null;
        if(zip){
            return new ObjectInputStream(new ZipInputStream(Files.newInputStream(path)));
        } else {
            return new ObjectInputStream(Files.newInputStream(path));
        }
    }
}
