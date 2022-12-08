package test.ignite.config;

import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.failure.StopNodeFailureHandler;
import org.apache.ignite.spi.discovery.DiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.spi.discovery.zk.ZookeeperDiscoverySpi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toMap;

@Configuration
public class IgniteConfig {

    @Value("${app.ignite.enabled}")
    private boolean igniteEnabled;

    @Bean
    public IgniteFactory igniteFactory(IgniteProperties props) {
        Supplier<IgniteConfiguration> cfgFactory = () -> igniteConfiguration(props);
        return new IgniteFactory(cfgFactory, igniteEnabled);
    }

    @Bean
    public IgniteCacheHolder igniteCacheHolder(List<CacheConfiguration<?, ?>> configurations) {
        return new IgniteCacheHolder(configurations.stream()
                .collect(toMap(CacheConfiguration::getName, Function.identity())));
    }

    private IgniteConfiguration igniteConfiguration(IgniteProperties igniteProperties) {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        return igniteConfiguration
                .setClientMode(true)
                .setConsistentId(igniteProperties.getId())
                .setPeerClassLoadingEnabled(true)
                .setDiscoverySpi(discoverySpi(igniteProperties))
                .setFailureHandler(new StopNodeFailureHandler())
                .setMetricsLogFrequency(0L)
                .setWorkDirectory(igniteProperties.getWorkDir());
    }

    public DiscoverySpi discoverySpi(IgniteProperties igniteProperties) {
        DiscoverySpi discoverySpi;
        if (igniteProperties.isZookeeperEnabled()) {
            discoverySpi = new ZookeeperDiscoverySpi()
                    .setZkConnectionString(igniteProperties.getZkConnectionString())
                    .setSessionTimeout(igniteProperties.getSessionTimeout())
                    .setZkRootPath(igniteProperties.getZkRootPath())
                    .setJoinTimeout(igniteProperties.getJoinTimeout());
        } else {
            TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder()
                    .setAddresses(igniteProperties.getAddresses());
            discoverySpi = new TcpDiscoverySpi()
                    .setIpFinder(ipFinder);
        }
        return discoverySpi;
    }
}
