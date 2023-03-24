package com.team4.happydogbot.controller;

import com.team4.happydogbot.entity.AdopterCat;
import com.team4.happydogbot.service.AdopterCatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;

/**
 * Класс - контроллер для объекта AdopterCat, содержащий набор API endpoints
 * для обращения к маршрутам отдельными HTTP методами
 * @see AdopterCat
 * @see AdopterCatService
 * @see AdopterCatController
 */
@RestController
@RequestMapping("/adopter_cat")
@Tag(name = "Усыновители", description = "CRUD-операции и другие эндпоинты для работы с усыновителями")
public class AdopterCatController {
    private final AdopterCatService adopterCatService;

    public AdopterCatController(AdopterCatService adopterCatService) {
        this.adopterCatService = adopterCatService;
    }

    @Operation(
            summary = "Добавление усыновителя",
            description = "Добавление нового усыновителя из тела запроса"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Усыновитель был добавлен",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = AdopterCat.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные параметры усыновителя",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = AdopterCat.class))
                            )
                    }
            )
    }
    )
    @PostMapping
    public ResponseEntity<AdopterCat> add(@RequestBody AdopterCat adopterCat) {
        adopterCatService.add(adopterCat);
        return ResponseEntity.ok(adopterCat);
    }

    @Operation(summary = "Получение усыновителя по chatId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновитель, найденный по chatId",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdopterCat.class)
                            )
                    )
            }
    )
    @Parameters(value = {
            @Parameter(name = "chatId", example = "1234567890")
    }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Усыновитель был найден",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = AdopterCat.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Усыновитель не был найден",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = AdopterCat.class))
                            )
                    }
            )
    }
    )
    @GetMapping("/{chatId}")
    public ResponseEntity<AdopterCat> get(@PathVariable Long chatId) {
        AdopterCat adopterCat = adopterCatService.get(chatId);
        if (adopterCat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adopterCat);
    }

    @Operation(summary = "Удаление усыновителя по chatId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновитель, найденный по chatId",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdopterCat.class)
                            )
                    )
            }
    )
    @Parameters(value = {
            @Parameter(name = "chatId", example = "1234567890")
    }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Усыновитель удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Усыновитель не был удален"
            )
    }
    )
    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> delete(@PathVariable Long chatId) {
        if (adopterCatService.remove(chatId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Изменение данных усыновителя",
            description = "Обновление данных усыновителя из тела запроса"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные усыновителя обновлены",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = AdopterCat.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Данные усыновителя не обновлены",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = AdopterCat.class))
                            )
                    }
            )
    }
    )
    @PutMapping
    public ResponseEntity<AdopterCat> update(@RequestBody AdopterCat adopterCat) {
        return ResponseEntity.ok(adopterCatService.update(adopterCat));
    }

    @Operation(summary = "Просмотр всех усыновителей",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновители найдены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdopterCat.class)
                            )
                    )
            }
    )
    @GetMapping("/all")
    public Collection<AdopterCat> getAll() {
        return this.adopterCatService.getAll();
    }
}

