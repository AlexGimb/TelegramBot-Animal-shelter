package com.example.telegrambotanimalshelter.controllers;
import com.example.telegrambotanimalshelter.dto.OwnerDTO;
import com.example.telegrambotanimalshelter.entity.enums.StatusPetOwner;
import com.example.telegrambotanimalshelter.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** Класс контроллера для редактирования информации о посетителях и персонале приюта кошек.
 */
@RestController
@RequestMapping(value = "/owner")
@Tag(name="Посетители и волонтеры приюта", description = "Редактирование данных людей в базе данных приюта животных")
public class OwnerController {
    private final OwnerService ownerService;
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping
    @Operation(summary = "Добавление нового человека в базу данных приюта")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Человек добавлен.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, запрос не может быть обработан."  )
    } )
    public ResponseEntity<OwnerDTO> addOwner(@RequestBody OwnerDTO ownerDTO) {
        return ResponseEntity.ok().body(ownerService.createOwner(ownerDTO));
    }


    @GetMapping
    @Operation(summary = "Получение списка данных всех людей из базы данных приюта")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Список людей получен.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, запрос не может быть обработан."  )
    } )
    public ResponseEntity<List<OwnerDTO>> getAllOwners(){
        return ResponseEntity.ok().body(ownerService.getAllOwners());
    }


    @DeleteMapping("{idOwner}")
    @Operation(summary = "Удаление человека из БД по его id")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Человек удалён.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void deleteOwnerById(@PathVariable ("idOwner") Long idOwner){
        ownerService.deleteCatOwnerById(idOwner);

    }
    @PutMapping("/status")
    @Operation(summary = "Изменение статуса человека в БД по его id")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Статус человек изменен.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void changeStatusOwner(@RequestParam Long idOwner, @RequestParam StatusPetOwner statusOwner){
        ownerService.changeStatusOwnerById(idOwner, statusOwner);

    }
    @PutMapping("/addCat")
    @Operation(summary = "Добавление или замена животного из БД приюта в карте человека по id " +
            "человека с проверкой и сменой статуса кота.")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Животное добавлено (заменено) в карту клиента.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void changeСat(@RequestParam Long idOwner, @RequestParam Long id){
        ownerService.changeCatById(idOwner, id);
    }

    @PutMapping("/addDog")
    @Operation(summary = "Добавление или замена животного из БД приюта в карте человека по id " +
            "человека с проверкой и сменой статуса собаки.")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Животное добавлено (заменено) в карту клиента.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void changeDog(@RequestParam Long idOwner, @RequestParam Long id){
        ownerService.changeDogById(idOwner, id);
    }

    @PutMapping("/deleteCat/{idOwner}")
    @Operation(summary = "Удаление животного из карты человека (по id человека) по какой-либо причине со сменой статуса кота.")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Животное стерто в карте клиента.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void takeTheCatAway(@PathVariable ("idOwner") Long idOwner){
        ownerService.takeTheCatAwayById(idOwner);
    }

    @PutMapping("/deleteDog/{idOwner}")
    @Operation(summary = "Удаление животного из карты человека (по id человека) по какой-либо причине со " +
            "сменой статуса собаки.")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Животное стерто в карте клиента.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void takeTheDogAway(@PathVariable ("idOwner") Long idOwner){
        ownerService.takeTheDogAwayById(idOwner);
    }

    @GetMapping("{idOwner}")
    @Operation(summary = "Поиск человека по его id в приюте животных.")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Человек найден.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public ResponseEntity<OwnerDTO> getOwner(@PathVariable ("idOwner") Long idOwner) {
        return ResponseEntity.ok().body(ownerService.getOwner(idOwner));
    }

    @PutMapping("/probe")
    @Operation(summary = "Изменение даты завершения испытательного срока усыновителя животного")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Дата изменена.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void updateFinishProbe(@RequestParam Long idOwner, @RequestParam ("+N дней") int plusDays){
        ownerService.updateFinish(idOwner, plusDays);
    }
}
