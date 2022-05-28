package org.example.cache.service.serviceImpl;

import org.example.cache.service.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ServiceImpl implements Service {
    /**
     Cache(cacheType = CacheProperties.CACHE_TYPE_INFILE, fileNamePrefix = "data",
                        zip = true, identityBy = {String.class, Double.class})
    */
    @Override
    public List<String> run(String item, double value, Date date) {
        List<String> result = new ArrayList<>();
        result.add(date.toString());
        for (int i = 0; i < value; i++) {
            result.add(String.format("|%s %d|", item, i));
        }
        try {
            Thread.sleep(new Random().nextInt(400));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Cache(cacheType = CACHE_TYPE_INMEMORY, listMaxCacheCount = 10)
     */
    @Override
    public List<String> work(String item) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            result.add(String.format("|%s %d|", item, i));
        }
        try {
            Thread.sleep(new Random().nextInt(400));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result ;
    }

}
