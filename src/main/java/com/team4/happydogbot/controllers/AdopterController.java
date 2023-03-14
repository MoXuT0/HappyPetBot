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
            description = "Добавление нового усыновителя из тела запроса с присвоением id из генератора"
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
                    description = "Некорректные параметры усыновителя"
            )
    }
    )
    @PostMapping
    public ResponseEntity<Adopter> add(@RequestBody Adopter adopter) {
        adopterService.add(adopter);
        return ResponseEntity.ok(adopter);
    }

    @Operation(summary = "Получение усыновителя по id",
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Усыновитель, найденный по id",
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = Adopter.class)
                                    )
                            )
                    }
            )
    @Parameters( value = {
            @Parameter(name = "id", example = "1")
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
                    description = "Усыновитель не был найден"
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
}
