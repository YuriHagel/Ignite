package hagel.ignite.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.IgniteState;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

@ToString
@Slf4j
public class IgniteFactory implements AutoCloseable {


    private final Supplier<IgniteConfiguration> configFactory;

    private final ExecutorService starter;
    private final Future<?> starterFuture;
    private final boolean igniteEnabled;

    private volatile Ignite ignite;

    private final boolean closed = false;

    public IgniteFactory(@NotNull Supplier<IgniteConfiguration> configFactory, boolean igniteEnabled) {
        this.igniteEnabled = igniteEnabled;
        this.configFactory = configFactory;
        this.starter = initStarter();
        this.starterFuture = this.starter.submit(this::runStarter);
    }

    public Ignite getIgnite() {
        if (closed) {
            throw new IgniteException("Ignite was closed: state=" + Ignition.state());
        }

        if (!isReady()) {
            throw new IgniteException("Ignite is not ready: state=" + Ignition.state());
        }
        return ignite;
    }

    public boolean isReady() {
        return ignite != null && Ignition.state() == IgniteState.STARTED;
    }

    @Override
    public void close() {
        this.starterFuture.cancel(true);
    }

    private void runStarter() {
        if (igniteEnabled) {
            if (!isReady()) {
                ignite = Ignition.start(configFactory.get());
                log.info("Ignite was started: state={}", Ignition.state());
            }
        }
    }


    private ExecutorService initStarter() {
        return Executors.newSingleThreadExecutor(
                new ThreadFactoryBuilder()
                        .setDaemon(true)
                        .setNameFormat("ignite-factory-starter-%d")
                        .setUncaughtExceptionHandler((t, e) -> {
                            log.error("Unhandled error in thread={}: error={}", t, e.getMessage(), e);
                        })
                        .build()
        );
    }
}
