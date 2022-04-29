package org.example.cache;

public class CacheProperties {
    public static final String CACHE_TYPE_INMEMORY = "IN_MEMORY";
    public static final String CACHE_TYPE_INFILE = "IN_FILE";

    private String cacheType;
    private String fileNamePrefix;
    private boolean zip;
    private Class[] identityBy;
    private int listMaxCacheCount;
    private String key;

    public CacheProperties(String cacheType, String fileNamePrefix, boolean zip, Class[] identityBy, int listMaxCacheCount, String key) {
        this.cacheType = cacheType;
        this.fileNamePrefix = fileNamePrefix;
        this.zip = zip;
        this.identityBy = identityBy;
        this.listMaxCacheCount = listMaxCacheCount;
        this.key = key;
    }

    public String getCacheType() {
        return cacheType;
    }

    public String getFileNamePrefix() {
        return fileNamePrefix;
    }

    public boolean getZip() {
        return zip;
    }

    public Class[] getIdentityBy() {
        return identityBy;
    }

    public int getListMaxCacheCount() {
        return listMaxCacheCount;
    }

    public String getKey() {
        return key;
    }







}
