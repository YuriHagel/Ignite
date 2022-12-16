package hagel.ignite.controller;

import hagel.ignite.domain.UserData;
import hagel.ignite.service.CachesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/caches")
public class CachesController {

    private final CachesService service;

    @PutMapping("/create_user_cache")
    @Operation(summary = "Create cache")
    public ResponseEntity<Void> createUserCache() {
        service.createCache();
        return ResponseEntity.notFound().build();
    }

    @GetMapping("{user_name}/get_user_cache")
    @Operation(summary = "Get cache user")
    public ResponseEntity<UserData> getCacheUser(@Parameter(description = "user name", required = true)
                                                 @PathVariable("user_name") @NotNull String userName) {
        return ResponseEntity.of(service.getValue(userName));
    }

    @DeleteMapping("/{cache_name}/drop")
    @Operation(summary = "Delete cache")
    public ResponseEntity<Void> drop(@Parameter(description = "cache name", required = true)
                                     @PathVariable("cache_name") @NotNull String cacheName) {
        service.drop(cacheName);
        return ResponseEntity.notFound().build();
    }
}
