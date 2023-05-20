package com.example.telegrambotanimalshelter.controllers;
import com.example.telegrambotanimalshelter.dto.cat.CatDTO;
import com.example.telegrambotanimalshelter.entity.Cat;
import com.example.telegrambotanimalshelter.service.CatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/cats")
@Tag(name="Кошки", description = "CRUD-операции для работы с кошками")
public class CatController {
    private final CatService catService;


    public CatController(CatService catService) {
        this.catService = catService;
    }
    @GetMapping("{id}")
    @Operation(
            summary = "Поиск кошки",
            description = "Поиск осуществляется по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Животное найдено",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Животное не найдено",
                    content = {}
            )
    }
    )
    public ResponseEntity<CatDTO> getCat(@PathVariable long id) {
        return ResponseEntity.ok().body(catService.getCat(id));
    }

    @PostMapping("/addCat")
    @Operation(summary = "Добавление новой кошки")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Кошка добавлена",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, запрос не может быть обработан.",
                    content = {}
            )
    }
    )
    public ResponseEntity<CatDTO> createCat(@RequestBody CatDTO catDTO) {
        return ResponseEntity.ok().body(catService.addCat(catDTO));
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Удаление кошки из базы данных",
            description = "Удаление осуществляется по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Животное удалено"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Животное не найдено"
            )
    }
    )
    public void deleteCat(@PathVariable("id") long id) {
        catService.removeCat(id);
        ResponseEntity.ok().build();
    }
    @PutMapping("{id}")
    @Operation(
            summary = "Редактирование сведений о кошке",
            description = "Редактирование осуществляется по id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные отредактированы",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, запрос не может быть обработан.",
                    content = {}
            )
    }
    )
    public ResponseEntity<CatDTO> updateCat(@RequestBody CatDTO catDTO) {
        return ResponseEntity.ok().body(catService.updateCat(catDTO));
    }
    @GetMapping("/getAllCats")
    @Operation(summary = "Получение списка всех кошек, которые есть в приюте")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список кошек получен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Cat.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Кошки не найдены",
                    content = {}
            )
    }
    )
    public ResponseEntity<Collection<CatDTO>> getAllCats() {
        return ResponseEntity.ok().body(catService.getAllCats());
    }
}
