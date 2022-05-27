package org.example.cache.storage;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Хранилище для кэширования в файл
 */
public class FileStorage implements Storage {
    private Map<Object[], Object> cache = new HashMap<>();;
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
    private void readCacheFromFile(){
        try {
            if(Files.size(pathToCacheFile) == 0) return;
            if(zip){
                try (ZipFile zipFile = new ZipFile(pathToCacheFile.toString());
                     ObjectInputStream ois = new ObjectInputStream(zipFile.getInputStream(zipFile.getEntry(pathToCacheFile.getFileName().toString().replace(".zip", ""))))) {
                    if(ois == null);
                    cache.putAll((Map<? extends Object[], ?>) ois.readObject());
                }
            }
        } catch (Exception e) {
            try {
                Files.delete(pathToCacheFile);
                createCacheFile(pathToCacheFile);
            } catch (IOException ioException) {
                ioException.printStackTrace();
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
        /*cache.keySet().stream().filter(args -> Arrays.equals(args, parameter)).collect(Collectors.toList());*/
        for (Object[] variable: cache.keySet()
             ) {
            if(Arrays.equals(variable, parameter)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getCachedValue(Method method, Object[] parameter) {
        for (Map.Entry entry: cache.entrySet()
        ) {
            if (Arrays.equals((Object[]) entry.getKey(), parameter)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public void cachValue(Method method, Object[] parameter, Object value) {
        cache.put(parameter, value);
        if(!zip){
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(pathToCacheFile));) {
                oos.writeObject(cache);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (Files.exists(pathToCacheFile)) {
                try {
                    Files.delete(pathToCacheFile);
                    Files.createFile(pathToCacheFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ZipEntry entry = new ZipEntry(pathToCacheFile.getFileName().toString().replace(".zip", ""));
            try{
                ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(pathToCacheFile));
                zos.putNextEntry(entry);
                ObjectOutputStream ous = new ObjectOutputStream(zos);
                ous.writeObject(cache);
                zos.closeEntry();
                zos.close();
                System.out.println("Successfully cached to zip file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void createCacheFile(Path path) throws IOException {
        Files.createDirectories(path.getParent());
        Files.createFile(path);
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
            ZipFile zipFile = new ZipFile(path.toString());
            ZipEntry entry = zipFile.getEntry(path.getFileName().toString().replace(".zip", ""));
            return new ObjectInputStream(zipFile.getInputStream(entry));
        } else {
            return new ObjectInputStream(Files.newInputStream(path));
        }
    }
}
