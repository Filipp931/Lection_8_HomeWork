package org.example.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.List;

/**
 * Аннотация для кэширования
 * String cacheType() - тип хранения кэша ( в пямяти или в файле)
 * String fileNamePrefix() - имя файла с кэшем (по умолчанию - имя метода)
 * boolean zip() - дополнительно сжимать кэш в zip архив
 * Class[] identityBy() - какие аргументы метода учитывать при определении уникальности результата, а какие игнорировать(по умолчанию все аргументы учитываются)
 * String key() - название файла/ключа по которому храниться значение в кэше (по умолчанию - имя метода)
 * int listMaxCacheCount() - Если возвращаемый тип это List – возможность указывать максимальное количество элементов в нем.
 То есть, если нам возвращается List с size = 1млн, мы можем сказать что в кеше достаточно хранить 100т элементов.
 */
@Target(ElementType.METHOD)
public @interface Cache {
    String cacheType() default CacheProperties.CACHE_TYPE_INMEMORY;
    String fileNamePrefix() default "";
    boolean zip() default false;
    Class[] identityBy() default {};
    int listMaxCacheCount() default 10;
    String key() default "";
}
