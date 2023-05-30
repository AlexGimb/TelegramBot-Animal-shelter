package com.example.telegrambotanimalshelter.controllers;

import com.example.telegrambotanimalshelter.dto.DogDTO;
import com.example.telegrambotanimalshelter.entity.Cat;
import com.example.telegrambotanimalshelter.service.DogService;
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
@RequestMapping("/dogs")
@Tag(name="Собаки", description = "CRUD-операции для работы с собаками")
public class DogController {
    private final DogService dogService;


    public DogController(DogService dogService) {
        this.dogService = dogService;
    }
    @GetMapping("{id}")
    @Operation(
            summary = "Поиск собаки",
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
    public ResponseEntity<DogDTO> getDog(@PathVariable long id) {
        return ResponseEntity.ok().body(dogService.getDog(id));
    }

    @PostMapping("/addDog")
    @Operation(summary = "Добавление новой собаки")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Собака добавлена",
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
    public ResponseEntity<DogDTO> createDog(@RequestBody DogDTO dogDTO) {
        return ResponseEntity.ok().body(dogService.addDog(dogDTO));
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Удаление собаки из базы данных",
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
    public void deleteDog(@PathVariable("id") long id) {
        dogService.removeDog(id);
        ResponseEntity.ok().build();
    }
    @PutMapping("{id}")
    @Operation(
            summary = "Редактирование сведений о собаке",
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
    public ResponseEntity<DogDTO> updateDog(@RequestBody DogDTO dogDTO) {
        return ResponseEntity.ok().body(dogService.updateDog(dogDTO));
    }
    @GetMapping("/getAllDogs")
    @Operation(summary = "Получение списка всех собак, которые есть в приюте")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список собак получен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Cat.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Собаки не найдены",
                    content = {}
            )
    }
    )
    public ResponseEntity<Collection<DogDTO>> getAllDogs() {
        return ResponseEntity.ok().body(dogService.getAllDogs());
    }
}
