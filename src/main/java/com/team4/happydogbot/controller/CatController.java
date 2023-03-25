package com.team4.happydogbot.controller;

import com.team4.happydogbot.entity.Cat;
import com.team4.happydogbot.service.CatService;
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
 * Класс - контроллер для объекта Cat, содержащий набор API endpoints
 * для обращения к маршрутам отдельными HTTP методами
 *
 * @see Cat
 * @see CatService
 * @see CatController
 */
@RestController
@RequestMapping("/cat")
@Tag(name = "Коты", description = "CRUD-операции и другие эндпоинты для работы с собаками")
public class CatController {

    private final CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    @Operation(
            summary = "Добавление кота",
            description = "Добавление нового кота из тела запроса"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Кот был добавлен",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Cat.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные параметры кота",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Cat.class))
                            )
                    }
            )
    }
    )
    @PostMapping
    public ResponseEntity<Cat> add(@RequestBody Cat cat) {
        catService.add(cat);
        return ResponseEntity.ok(cat);
    }

    @Operation(summary = "Получение кота по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Кот, найденная по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Cat.class)
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
                    description = "Кот был найден",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Cat.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Кот не был найден",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Cat.class))
                            )
                    }
            )
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Cat> get(@PathVariable Long id) {
        Cat dog = catService.get(id);
        if (dog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dog);
    }

    @Operation(summary = "Удаление кота по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Кот, найденная по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Cat.class)
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
                    description = "Кот удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Кот не был удален"
            )
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (catService.remove(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Изменение данных кота",
            description = "Обновление данных кота из тела запроса"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные кота обновлены",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Cat.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Данные кота не обновлены",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Cat.class))
                            )
                    }
            )
    }
    )
    @PutMapping
    public ResponseEntity<Cat> update(@RequestBody Cat cat) {
        catService.update(cat);
        return ResponseEntity.ok(cat);
    }

    @Operation(summary = "Просмотр всех котов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Коты найдены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Cat.class)
                            )
                    )
            }
    )
    @GetMapping("/all")
    public Collection<Cat> getAll() {
        return this.catService.getAll();
    }
}
