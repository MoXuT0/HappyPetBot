package com.team4.happydogbot.controller;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;

/**
 * Класс - контроллер для объекта Cat, содержащий набор API endpoints
 * для обращения к маршрутам отдельными HTTP методами
 * @see ReportCat
 * @see ReportCatService
 * @see ReportCatController
 */
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

    @Operation(summary = "Получение фото отчета по id отчета",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фото по id отчета"
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
                    description = "Фото к отчету было найдено"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Фото к отчету не было найдено"
            )
    }
    )
    @GetMapping("/photo/{id}")
    public ResponseEntity<byte[]> getPhoto(@Parameter (description = "report id") @PathVariable Long id) {
        ReportCat reportCat = this.reportCatService.get(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(reportCat.getFileId().length());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"ReportPhoto.jpg\"")
                .body(reportCatService.getFile(id));
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
                                    array = @ArraySchema(schema = @Schema(implementation = ReportCat.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Данные отчета не обновлены",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReportCat.class))
                            )
                    }
            )
    }
    )
    @PutMapping
    public ResponseEntity<ReportCat> update(@RequestBody ReportCat reportCat) {
        reportCatService.update(reportCat);
        return ResponseEntity.ok(reportCat);
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
