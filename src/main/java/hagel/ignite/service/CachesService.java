package hagel.ignite.service;

import hagel.ignite.config.IgniteFactory;
import hagel.ignite.domain.UserData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static hagel.ignite.config.IgniteCacheConfig.USER_CACHE;

@Service
@RequiredArgsConstructor
@Slf4j
public class CachesService {
    private final IgniteFactory factory;

    public void createCache() {
        UserData userData = new UserData(RandomString.make(), RandomString.make());
        log.info("Creating userName={}", userData.getName());
        factory.getIgnite()
                .getOrCreateCache(USER_CACHE)
                .put(userData.getName(), userData);
    }

    public void drop(String cache) {
        factory.getIgnite()
                .cache(cache)
                .destroy();
    }

    public Optional<UserData> getValue(String userName) {
        return Optional.ofNullable(factory.getIgnite()
                        .cache(USER_CACHE)
                        .get(userName))
                .map(value -> (UserData) value);
    }
}
