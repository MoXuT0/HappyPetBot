package com.team4.happydogbot.controller;

import com.team4.happydogbot.entity.Dog;
import com.team4.happydogbot.service.DogService;
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
 * Класс - контроллер для объекта Dog, содержащий набор API endpoints
 * для обращения к маршрутам отдельными HTTP методами
 * @see Dog
 * @see DogService
 * @see DogController
 */
@RestController
@RequestMapping("/dog")
@Tag(name = "Собаки", description = "CRUD-операции и другие эндпоинты для работы с собаками")
public class DogController {

    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @Operation(
            summary = "Добавление собаки",
            description = "Добавление новой собаки из тела запроса"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Собака была добавлена",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dog.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные параметры собаки",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dog.class))
                            )
                    }
            )
    }
    )
    @PostMapping
    public ResponseEntity<Dog> add(@RequestBody Dog dog) {
        dogService.add(dog);
        return ResponseEntity.ok(dog);
    }

    @Operation(summary = "Получение собаки по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Собака, найденная по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Dog.class)
                            )
                    )
            }
    )
    @Parameters(value = {
            @Parameter(name = "id", example = "1")
    }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Собака была найдена",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dog.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Собака не была найдена",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dog.class))
                            )
                    }
            )
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Dog> get(@PathVariable Long id) {
        Dog dog = dogService.get(id);
        if (dog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dog);
    }

    @Operation(summary = "Удаление собаки по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Собака, найденная по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Dog.class)
                            )
                    )
            }
    )
    @Parameters(value = {
            @Parameter(name = "id", example = "1")
    }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Собака удалена"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Собака не была удалена"
            )
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (dogService.remove(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Изменение данных собаки",
            description = "Обновление данных собаки из тела запроса"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные собаки обновлены",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dog.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Данные собаки не обновлены",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dog.class))
                            )
                    }
            )
    }
    )
    @PutMapping
    public ResponseEntity<Dog> update(@RequestBody Dog dog) {
        dogService.update(dog);
        return ResponseEntity.ok(dog);
    }

    @Operation(summary = "Просмотр всех собак",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Собаки найдены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Dog.class)
                            )
                    )
            }
    )
    @GetMapping("/all")
    public Collection<Dog> getAll() {
        return this.dogService.getAll();
    }
}
