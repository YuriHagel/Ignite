package test.ignite.config;

import lombok.RequiredArgsConstructor;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class IgniteCacheHolder {

    private final Map<String, CacheConfiguration<?, ?>> cacheConfigurations;

    @SuppressWarnings("unchecked")
    public <K, V> CacheConfiguration<K, V> config(String cacheName) {
        return Optional.ofNullable((CacheConfiguration<K, V>) cacheConfigurations.get(cacheName))
                .orElseThrow(RuntimeException::new);
    }
}
