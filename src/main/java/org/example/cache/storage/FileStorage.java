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
    Object[] args;
    Path storageRootDirectory;
    Path pathToCacheFile;

    public FileStorage(Path storageRootDirectory, String fileNamePrefix, boolean zip, Object[] args) throws IOException, ClassNotFoundException {
        this.fileNamePrefix = fileNamePrefix;
        this.zip = zip;
        this.args = args;
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

    @Override
    public boolean containsCachedValue(Method method, Object[] parameter) throws IOException {
        return cache.containsKey(args);
    }

    @Override
    public Object getCachedValue(Method method, Object[] parameter) {
        return cache.get(args);
    }

    @Override
    public void cachValue(Method method, Object[] parameter, Object value) {
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
