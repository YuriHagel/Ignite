package test.ignite.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/caches")
public class CachesController {

    @PostMapping("/{cache_name}/reload")
    @Operation(summary = "Инициализация кэша")
    public ResponseEntity<Void> reload(@Parameter(description = "Наименование кэша", required = true)
                                 @PathVariable("cache_name") @NotNull String cacheName) {
       return ResponseEntity.notFound().build();
    }
}
