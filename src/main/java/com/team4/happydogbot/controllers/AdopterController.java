package com.team4.happydogbot.controllers;

import com.team4.happydogbot.entity.Adopter;
import com.team4.happydogbot.service.AdopterService;
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

@RestController
@RequestMapping("/adopter")
@Tag(name = "Усыновители", description = "CRUD-операции и другие эндпоинты для работы с усыновителями")
public class AdopterController {
    private final AdopterService adopterService;

    public AdopterController(AdopterService adopterService) {
        this.adopterService = adopterService;
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
                                    array = @ArraySchema(schema = @Schema(implementation = Adopter.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные параметры усыновителя",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Adopter.class))
                            )
                    }
            )
    }
    )
    @PostMapping
    public ResponseEntity<Adopter> add(@RequestBody Adopter adopter) {
        adopterService.add(adopter);
        return ResponseEntity.ok(adopter);
    }

    @Operation(summary = "Получение усыновителя по chatId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновитель, найденный по chatId",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adopter.class)
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
                                    array = @ArraySchema(schema = @Schema(implementation = Adopter.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Усыновитель не был найден",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Adopter.class))
                            )
                    }
            )
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Adopter> get(@PathVariable long id) {
        Adopter adopter = adopterService.get(id);
        if (adopter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adopter);
    }

    @Operation(summary = "Удаление усыновителя по chatId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновитель, найденный по chatId",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adopter.class)
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
                    description = "Усыновитель удален",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Adopter.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Усыновитель не был удален",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Adopter.class))
                            )
                    }
            )
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (adopterService.remove(id)) {
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
                                    array = @ArraySchema(schema = @Schema(implementation = Adopter.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Данные усыновителя не обновлены",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Adopter.class))
                            )
                    }
            )
    }
    )
    @PutMapping
    public ResponseEntity<Adopter> update(@RequestBody Adopter adopter) {
        if (adopterService.update(adopter)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }


    @Operation(summary = "Просмотр всех усыновителей",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Усыновители найдены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Adopter.class)
                            )
                    )
            }
    )
    @GetMapping("/all")
    public Collection<Adopter> getAll() {
        return this.adopterService.getAll();
    }
}

