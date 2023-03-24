package com.team4.happydogbot.controller;

import com.team4.happydogbot.entity.ReportDog;
import com.team4.happydogbot.service.ReportDogService;
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
 * @see ReportDog
 * @see ReportDogService
 * @see ReportDogController
 */
@RestController
@RequestMapping("/report_dog")
@Tag(name = "Отчеты", description = "CRUD-операции и другие эндпоинты для работы с отчетами")
public class ReportDogController {

    private final ReportDogService reportService;

    public ReportDogController(ReportDogService reportService) {
        this.reportService = reportService;
    }

    @Operation(
            summary = "Добавление отчета",
            description = "Добавление нового отчета из тела запроса с присвоением id из генератора"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчет был добавлен",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportDog.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные параметры отчета",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportDog.class))
                            )
                    }
            )
    }
    )
    @PostMapping
    public ResponseEntity<ReportDog> add(@RequestBody ReportDog reportDog) {
        reportService.add(reportDog);
        return ResponseEntity.ok(reportDog);
    }

    @Operation(summary = "Получение отчета по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет, найденный по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReportDog.class)
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
                    description = "Отчет был найден",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportDog.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Отчет не был найден",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportDog.class))
                            )
                    }
            )
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ReportDog> get(@PathVariable Long id) {
        ReportDog reportDog = reportService.get(id);
        if (reportDog == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reportDog);
    }

    @Operation(summary = "Удаление отчета по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет, найденный по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReportDog.class)
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
                    description = "Отчет удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Отчет не был удален"
            )
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (reportService.remove(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Изменение отчета",
            description = "Обновление отчета из тела запроса"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные отчета обновлены",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportDog.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Данные отчета не обновлены",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportDog.class))
                            )
                    }
            )
    }
    )
    @PutMapping
    public ResponseEntity<ReportDog> update(@RequestBody ReportDog reportDog) {
        reportService.update(reportDog);
        return ResponseEntity.ok(reportDog);
    }

    @Operation(summary = "Просмотр всех отчетов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчеты найдены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReportDog.class)
                            )
                    )
            }
    )
    @GetMapping("/all")
    public Collection<ReportDog> getAll() {
        return this.reportService.getAll();
    }
}
