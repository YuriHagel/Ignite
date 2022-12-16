package hagel.ignite.config;

import hagel.ignite.domain.UserData;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Configuration
public class IgniteCacheConfig {

    public static final String USER_CACHE = "User";

    @Bean
    public Map<String, CacheConfiguration<?, ?>> cacheConfiguration() {
        return caches().stream()
                .collect(toMap(CacheConfiguration::getName, Function.identity()));
    }

    private List<CacheConfiguration<?, ?>> caches() {
        return Arrays.asList(
                cacheTemplate(USER_CACHE, Integer.class, UserData.class)
                        .setBackups(1));
    }

    private <K, V> CacheConfiguration<K, V> cacheTemplate(String cacheName, Class<K> keyClass, Class<V> valueClass) {
        return new CacheConfiguration<K, V>(cacheName)
                .setSqlSchema("PUBLIC")
                .setDataRegionName("HAGEL")
                .setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC)
                .setQueryEntities(Collections.singletonList(
                        new QueryEntity(keyClass, valueClass).setTableName(cacheName)))
                .setStatisticsEnabled(true)
                .setManagementEnabled(true);
    }
}
