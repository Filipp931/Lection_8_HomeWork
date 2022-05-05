package org.example.cache.service;

import org.example.cache.Cache;
import org.example.cache.CacheProperties;

import java.util.Date;
import java.util.List;

public interface Service {
    @Cache(cacheType = CacheProperties.CACHE_TYPE_INFILE, fileNamePrefix = "data",
            zip = true, identityBy = {String.class, Double.class})
    List<String> run(String item, double value, Date date);
}
