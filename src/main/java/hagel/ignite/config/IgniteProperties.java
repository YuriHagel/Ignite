package hagel.ignite.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Validated
@Configuration
@ConfigurationProperties("app.ignite")
@Data
public class IgniteProperties {
    private String id;
    //private String name;
    private boolean zookeeperEnabled;

    /* Ignite DiscoverySpi */
    private List<String> addresses;

    /* Zookeeper DiscoverySpi */
    private String zkConnectionString;
    private long sessionTimeout = 30_000L;
    private String zkRootPath = "";
    private long joinTimeout = 10_000L;

    private boolean persistence;
    private boolean clientMode;
    private Long dataRegionMaxSize;
    private int dataNodeCount;

    private Map<String, ?> attributes;
    private long stoppingTimeout = 10_000L;
    private boolean tryRestart;
    private Long tryLockTimeout;
    private boolean autoStart;
    private boolean backgroundStart;
    private Long stateCheckInitialDelay;
    private Long stateCheckInterval;
    private String workDir;

    @NestedConfigurationProperty
    private CommunicationProperties communication = new CommunicationProperties();

    @Data
    public static class CommunicationProperties {
        private int sharedMemoryPort = 48100;
        private int localPort = 47100;
        private int localPortRange = 1;
    }
}
