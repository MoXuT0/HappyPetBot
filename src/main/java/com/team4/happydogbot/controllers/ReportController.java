package com.team4.happydogbot.controllers;

import com.team4.happydogbot.entity.Report;
import com.team4.happydogbot.service.ReportService;
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
@RequestMapping("/report")
@Tag(name = "Отчеты", description = "CRUD-операции и другие эндпоинты для работы с отчетами")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
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
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Report.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные параметры отчета"
            )
    }
    )
    @PostMapping
    public ResponseEntity<Report> add(@RequestBody Report report) {
        reportService.add(report);
        return ResponseEntity.ok(report);
    }

    @Operation(summary = "Получение отчета по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчет, найденный по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
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
                    description = "Отчет был найден",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Report.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Отчет не был найден"
            )
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Report> get(@PathVariable long id) {
        Report report = reportService.get(id);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(report);
    }
}
