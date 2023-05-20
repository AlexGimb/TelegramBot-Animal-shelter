package com.example.telegrambotanimalshelter.service;
import com.example.telegrambotanimalshelter.StringValidation;
import com.example.telegrambotanimalshelter.dto.cat.CatOwnerDTO;
import com.example.telegrambotanimalshelter.entity.Cat;
import com.example.telegrambotanimalshelter.entity.CatOwner;
import com.example.telegrambotanimalshelter.entity.enums.StatusPetOwner;
import com.example.telegrambotanimalshelter.exception.ListOfOwnersIsEmptyException;
import com.example.telegrambotanimalshelter.exception.NoOwnerWithSuchIdException;
import com.example.telegrambotanimalshelter.exception.PetNoFreeException;
import com.example.telegrambotanimalshelter.repository.CatOwnerRepository;
import com.example.telegrambotanimalshelter.repository.CatRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import static com.example.telegrambotanimalshelter.dto.cat.CatOwnerDTO.catOwnerToDTO;
import static com.example.telegrambotanimalshelter.entity.enums.StatusOfPet.BUSY;
import static com.example.telegrambotanimalshelter.entity.enums.StatusOfPet.FREE;
import static com.example.telegrambotanimalshelter.entity.enums.StatusPetOwner.SEARCH;

@Service
public class CatOwnerService {
    private final CatOwnerRepository catOwnerRepository;
    private final CatRepository catRepository;
    private final TelegramBot telegramBot;

    public CatOwnerService(CatOwnerRepository catOwnerRepository, CatRepository catRepository, TelegramBot telegramBot) {
        this.catOwnerRepository = catOwnerRepository;
        this.catRepository = catRepository;
        this.telegramBot = telegramBot;
    }

    public CatOwnerDTO createCatOwner(CatOwnerDTO catOwnerDTO) {
        if (catOwnerDTO.chat_id() == 0 || !(StringValidation.validation(catOwnerDTO.fullName())
                && StringValidation.validation(catOwnerDTO.phone())
                && StringValidation.validation(catOwnerDTO.address()))) {
            throw new IllegalArgumentException("Данные заполнены не корректно.");
        }
        Cat cat = catRepository.findById(catOwnerDTO.idCatOwner()).orElseThrow();
        if (cat.getStatus().equals(FREE)) {
            CatOwner catOwner = new CatOwner(catOwnerDTO.chat_id(), catOwnerDTO.fullName(),
                    catOwnerDTO.phone(), catOwnerDTO.address(), StatusPetOwner.TRIAL, LocalDate.now().plusDays(30), cat);
            cat.setStatus(BUSY);

            return catOwnerToDTO(catOwnerRepository.save(catOwner));
        } else throw new PetNoFreeException("Кошка забронирована другим человеком.");
    }

    /**
     * Метод возвращает лист всех сущностей "посетителей и волонтеров" из базы данных приюта.
     *
     * @return возвращает список DTO данных людей в базе данных приюта, согласно полям конструктора CatOwnerDTO.
     */
    public List<CatOwnerDTO> getAllCatsOwners() {
        List<CatOwner> owners = catOwnerRepository.findAll();
        if (!owners.isEmpty()) {
            return owners.stream().map(CatOwnerDTO::catOwnerToDTO).collect(Collectors.toList());
        } else throw new ListOfOwnersIsEmptyException("Лист посетителей и волонтеров пуст!");
    }

    /**
     * Метод изменения статуса "потенциального хозяина" кошки.
     *
     * @param idCatOwner номер человека в базе данных приюта.
     * @param status     статус человека в приюте по отношению к кошке.
     */
    public void changeStatusOwnerById(Long idCatOwner, StatusPetOwner status) {
        CatOwner catOwner = catOwnerRepository.findById(idCatOwner).orElseThrow(NoOwnerWithSuchIdException::new);
        catOwner.setStatusOwner(status);
        catOwnerRepository.save(catOwner);
        String message = "";
        switch (status) {
            case SEARCH -> message = "Постараемся подобрать Вам питомца";
            case PET_OWNER -> message = "Вы прошли испытательный срок, поздравляем!";
            case REFUSING -> message = "Вам отказано в предоставлении питомца, пожалуйста свяжитесь с волонтером";
            case BLACKLISTED -> message = "Вы внесены в черный список нашего приюта";
            case TRIAL ->
                    message = "Вам будет выдан питомец и назначен испытательный срок до " + catOwner.getFinish() +
                            " В течение испытательного срока Вы должны присылать ежедневный отчет о состоянии питомца";
        }
        telegramBot.execute(new SendMessage(catOwner.getChatId(), message));
    }

    /**
     * Метод добавления животного (или замены) из БД к "усыновителю" по id с проверкой и сменой статуса животного.
     * Если у кота FREE, то можно передавать и статус меняется на BUSY. Если нет, то NoOwnerWithSuchIdException.
     *
     * @param idCatOwner номер человека в базе данных приюта.
     * @param id   номер животного в базе даннных приюта.
     */
    public void changeCatById(Long idCatOwner, Long id) {
        CatOwner catOwner = catOwnerRepository.findById(idCatOwner).orElseThrow(NoOwnerWithSuchIdException::new);
        Cat cat = catRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (cat.getStatus().equals(FREE)) {
            cat.setStatus(BUSY);
            catOwner.setCat(cat);
            cat.setCatOwner(catOwner);
            catOwnerRepository.save(catOwner);
            catRepository.save(cat);
        } else throw new PetNoFreeException("Кошка забронирована другим человеком.");
    }

    /**
     * Метод удаления у человека животного по какой-либо причине со сменой статуса животного.
     * Например, при отказе в усыновлении или при форс-мажоре. Статус человека станет SEARCH.
     * При этом статус человека меняет волонтер (в зависимости от причины) и другим методом.
     *
     * @param idCatOwner номер человека в базе данных приюта.
     */
    public void takeTheCatAwayById(Long idCatOwner) {
        CatOwner catOwner = catOwnerRepository.findById(idCatOwner).orElseThrow(NoOwnerWithSuchIdException::new);
        Cat cat = catOwner.getCat();
        cat.setStatus(FREE);
        cat.setCatOwner(null);
        catRepository.save(cat);
        catOwner.setCat(null);
        catOwner.setStatusOwner(SEARCH);
        catOwnerRepository.save(catOwner);
    }

    /**
     * Метод удаления "потенциального хозяина" животного (или сотрудника приюта) со сменой статуса животного
     * и очистке у него поля "потенциального хозяина".
     *
     * @param idCatOwner номер человека в базе данных приюта.
     */
    public void deleteCatOwnerById(Long idCatOwner) {
        CatOwner catOwner = catOwnerRepository.findById(idCatOwner).orElseThrow(NoOwnerWithSuchIdException::new);
        if (catOwner.getCat() != null) {
            Cat cat = catOwner.getCat();
            cat.setStatus(FREE);
            cat.setCatOwner(null);
            catRepository.save(cat);

        }
        catOwnerRepository.deleteById(idCatOwner);
    }

    /**
     * Метод получения человека по его id.
     *
     * @param idCatOwner номер человека в базе данных приюта.
     * @return возвращает DTO человека.
     */
    public CatOwnerDTO getCatOwner(Long idCatOwner) {
        return catOwnerToDTO(catOwnerRepository.findById(idCatOwner).orElseThrow(NoOwnerWithSuchIdException::new));
    }

    /**
     * Метод изменения даты окончания испытательного срока. Меняет волонтер. Уведомляет "потенциального хозяина" об изменении
     * даты окончания испытательного срока
     *
     * @param idCatOwner номер человека в базе данных приюта.
     * @param plusDays   на сколько дней увеличивается старая дата завершения испытательного периода.
     */
    public void updateFinish(Long idCatOwner, int plusDays) {
        CatOwner catOwner = catOwnerRepository.findById(idCatOwner).orElseThrow(NoOwnerWithSuchIdException::new);
        catOwner.setFinish(catOwner.getFinish().plusDays(plusDays));
        catOwnerRepository.save(catOwner);
        telegramBot.execute(new SendMessage(catOwner.getChatId(), "Вам продлен испытательный срок до "
                + catOwner.getFinish().toString()));
    }

}
