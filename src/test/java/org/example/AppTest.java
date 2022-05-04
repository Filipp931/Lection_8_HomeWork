package org.example;

import static org.junit.Assert.assertTrue;

import org.example.cache.CacheProxy;
import org.example.cache.service.Service;
import org.example.cache.service.serviceImpl.ServiceImpl;
import org.example.cache.storage.FileStorage;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void FileStorageCreateZipFileTest(){
        try {
            FileStorage fileStorage = new FileStorage(Paths.get("C:\\Users\\Fil\\Desktop\\JavaSber"), "testZip",
                    true,  null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(Files.exists(Paths.get("C:\\Users\\Fil\\Desktop\\JavaSber\\testZip.zip")));
    }
    @Test
    public void FileStorageWriteZipFileTest() throws IOException, ClassNotFoundException {
        CacheProxy cacheProxy = new CacheProxy(Paths.get("test"));
        Service service = (Service) cacheProxy.cache(new ServiceImpl()); //!!!!!!!!!!
        System.out.println("||||||");
        service.run("testItem", 12, new Date()); //Exception
    }


}

