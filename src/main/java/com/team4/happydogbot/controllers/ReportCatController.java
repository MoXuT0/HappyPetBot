package com.team4.happydogbot.controllers;

import com.team4.happydogbot.entity.AdopterCat;
import com.team4.happydogbot.entity.ReportCat;
import com.team4.happydogbot.service.ReportCatService;
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
@RequestMapping("/report_cat")
@Tag(name = "Отчеты", description = "CRUD-операции и другие эндпоинты для работы с отчетами")
public class ReportCatController {

    private final ReportCatService reportCatService;

    public ReportCatController(ReportCatService reportCatService) {
        this.reportCatService = reportCatService;
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
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ReportCat.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные параметры отчета",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportCat.class))
                            )
                    }
            )
    }
    )
    @PostMapping
    public ResponseEntity<ReportCat> add(@RequestBody ReportCat reportCat) {
        reportCatService.add(reportCat);
        return ResponseEntity.ok(reportCat);
    }

    @Operation(summary = "Получение отчета по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет, найденный по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReportCat.class)
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
                                    array = @ArraySchema(schema = @Schema(implementation = ReportCat.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Отчет не был найден",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportCat.class))
                            )
                    }
            )
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ReportCat> get(@PathVariable Long id) {
        ReportCat reportCat = reportCatService.get(id);
        if (reportCat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reportCat);
    }

    @Operation(summary = "Удаление отчета по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет, найденный по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReportCat.class)
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
        if (reportCatService.remove(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Просмотр всех отчетов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчеты найдены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReportCat.class)
                            )
                    )
            }
    )
    @GetMapping("/all")
    public Collection<ReportCat> getAll() {
        return this.reportCatService.getAll();
    }
}
