package com.example.telegrambotanimalshelter.controllers;
import com.example.telegrambotanimalshelter.dto.cat.CatOwnerDTO;
import com.example.telegrambotanimalshelter.entity.enums.StatusPetOwner;
import com.example.telegrambotanimalshelter.service.CatOwnerService;
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
@RequestMapping(value = "/catOwner")
@Tag(name="Посетители и волонтеры приюта кошек", description = "Редактирование данных людей в базе данных приюта животных")
public class CatOwnerController {
    private final CatOwnerService catOwnerService;
    public CatOwnerController(CatOwnerService catOwnerService) {
        this.catOwnerService = catOwnerService;
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
    public ResponseEntity<CatOwnerDTO> addCatOwner(@RequestBody CatOwnerDTO catOwnerDTO) {
        return ResponseEntity.ok().body(catOwnerService.createCatOwner(catOwnerDTO));
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
    public ResponseEntity<List<CatOwnerDTO>> getAllCatOwners(){
        return ResponseEntity.ok().body(catOwnerService.getAllCatsOwners());
    }
    @DeleteMapping("{idCatOwner}")
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
    public void deleteCatOwnerById(@PathVariable ("idCatOwner") Long idCatOwner){
        catOwnerService.deleteCatOwnerById(idCatOwner);
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
    public void changeStatusOwner(@RequestParam Long idCatOwner, @RequestParam StatusPetOwner statusOwner){
        catOwnerService.changeStatusOwnerById(idCatOwner, statusOwner);
    }
    @PutMapping("/add")
    @Operation(summary = "Добавление или замена животного из БД приюта в карте человека по id человека с проверкой и сменой статуса кота.")
    @ApiResponses( {
            @ApiResponse( responseCode = "200",
                    description = "Животное добавлено (заменено) в карту клиента.",
                    content = {  @Content(mediaType = "application/json") } ),
            @ApiResponse( responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат."  ),
            @ApiResponse( responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны."  )
    } )
    public void changePet(@RequestParam Long idCatOwner, @RequestParam Long id){
        catOwnerService.changeCatById(idCatOwner, id);
    }
    @PutMapping("/delete/{idCatOwner}")
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
    public void takeThePetAway(@PathVariable ("idCatOwner") Long idCatOwner){
        catOwnerService.takeTheCatAwayById(idCatOwner);
    }

    @GetMapping("{idCatOwner}")
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
    public ResponseEntity<CatOwnerDTO> getCatOwner(@PathVariable ("idCatOwner") Long idCatOwner) {
        return ResponseEntity.ok().body(catOwnerService.getCatOwner(idCatOwner));
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
    public void updateFinishProbe(@RequestParam Long idCatOwner, @RequestParam ("+N дней") int plusDays){
        catOwnerService.updateFinish(idCatOwner, plusDays);
    }

}
