package com.example.telegrambotanimalshelter.service;
import com.example.telegrambotanimalshelter.StringValidation;
import com.example.telegrambotanimalshelter.dto.OwnerDTO;
import com.example.telegrambotanimalshelter.entity.Cat;
import com.example.telegrambotanimalshelter.entity.Dog;
import com.example.telegrambotanimalshelter.entity.Owner;
import com.example.telegrambotanimalshelter.entity.enums.StatusPetOwner;
import com.example.telegrambotanimalshelter.exception.ListOfOwnersIsEmptyException;
import com.example.telegrambotanimalshelter.exception.PetNoFreeException;
import com.example.telegrambotanimalshelter.repository.DogRepository;
import com.example.telegrambotanimalshelter.repository.OwnerRepository;
import com.example.telegrambotanimalshelter.repository.CatRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import static com.example.telegrambotanimalshelter.dto.OwnerDTO.catOwnerToDTO;
import static com.example.telegrambotanimalshelter.entity.enums.StatusOfPet.BUSY;
import static com.example.telegrambotanimalshelter.entity.enums.StatusOfPet.FREE;
import static com.example.telegrambotanimalshelter.entity.enums.StatusPetOwner.SEARCH;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final CatRepository catRepository;

    private final DogRepository dogRepository;
    private final TelegramBot telegramBot;

    public OwnerService(OwnerRepository ownerRepository, CatRepository catRepository,
                        DogRepository dogRepository, TelegramBot telegramBot) {
        this.ownerRepository = ownerRepository;
        this.catRepository = catRepository;
        this.dogRepository = dogRepository;
        this.telegramBot = telegramBot;
    }

    public OwnerDTO createOwner(OwnerDTO ownerDTO) {
        if (ownerDTO.chat_id() == 0 || !(StringValidation.validation(ownerDTO.fullName())
                && StringValidation.validation(ownerDTO.phone())
                && StringValidation.validation(ownerDTO.address()))) {
            throw new IllegalArgumentException("Данные заполнены не корректно.");
        }
        Dog dog = dogRepository.findById(ownerDTO.idOwner()).orElseThrow();
        Cat cat = catRepository.findById(ownerDTO.idOwner()).orElseThrow();
        if (cat.getStatus().equals(FREE)) {
            Owner owner = new Owner(ownerDTO.chat_id(), ownerDTO.fullName(),
                    ownerDTO.phone(), ownerDTO.address(), StatusPetOwner.TRIAL,
                    LocalDate.now().plusDays(30), cat);
            cat.setStatus(BUSY);
            return catOwnerToDTO(ownerRepository.save(owner));
        }else if (dog.getStatus().equals(FREE)){
            Owner owner = new Owner(ownerDTO.chat_id(), ownerDTO.fullName(),
                    ownerDTO.phone(), ownerDTO.address(), StatusPetOwner.TRIAL,
                    LocalDate.now().plusDays(30), dog);
            dog.setStatus(BUSY);
            return catOwnerToDTO(ownerRepository.save(owner));
        } else throw new PetNoFreeException("Животное забронирована другим человеком.");
    }

    /**
     * Метод возвращает лист всех сущностей "посетителей и волонтеров" из базы данных приюта.
     *
     * @return возвращает список DTO данных людей в базе данных приюта, согласно полям конструктора CatOwnerDTO.
     */
    public List<OwnerDTO> getAllOwners() {
        List<Owner> owners = ownerRepository.findAll();
        if (!owners.isEmpty()) {
            return owners.stream().map(OwnerDTO::catOwnerToDTO).collect(Collectors.toList());
        } else throw new ListOfOwnersIsEmptyException("Лист посетителей и волонтеров пуст!");
    }

    /**
     * Метод изменения статуса "потенциального хозяина" кошки.
     *
     * @param idOwner номер человека в базе данных приюта.
     * @param status     статус человека в приюте по отношению к кошке.
     */
    public void changeStatusOwnerById(Long idOwner, StatusPetOwner status) {
        Owner owner = ownerRepository.findById(idOwner).orElseThrow();
        owner.setStatusOwner(status);
        ownerRepository.save(owner);
        String message = "";
        switch (status) {
            case SEARCH -> message = "Постараемся подобрать Вам питомца";
            case PET_OWNER -> message = "Вы прошли испытательный срок, поздравляем!";
            case REFUSING -> message = "Вам отказано в предоставлении питомца, пожалуйста свяжитесь с волонтером";
            case BLACKLISTED -> message = "Вы внесены в черный список нашего приюта";
            case TRIAL ->
                    message = "Вам будет выдан питомец и назначен испытательный срок до " + owner.getFinish() +
                            " В течение испытательного срока Вы должны присылать ежедневный отчет о состоянии питомца";
        }
        telegramBot.execute(new SendMessage(owner.getChatId(), message));
    }

    /**
     * Метод добавления животного (или замены) из БД к "усыновителю" по id с проверкой и сменой статуса животного.
     * Если у кота FREE, то можно передавать и статус меняется на BUSY. Если нет, то NoOwnerWithSuchIdException.
     *
     * @param idOwner номер человека в базе данных приюта.
     * @param id   номер животного в базе даннных приюта.
     */
    public void changeCatById(Long idOwner, Long id) {
        Owner owner = ownerRepository.findById(idOwner).orElseThrow();
        Cat cat = catRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (cat.getStatus().equals(FREE)) {
            cat.setStatus(BUSY);
            owner.setCat(cat);
            cat.setOwner(owner);
            ownerRepository.save(owner);
            catRepository.save(cat);
        } else throw new PetNoFreeException("Кошка забронирована другим человеком.");
    }

    public void changeDogById(Long idOwner, Long id) {
        Owner owner = ownerRepository.findById(idOwner).orElseThrow();
        Dog dog = dogRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (dog.getStatus().equals(FREE)) {
            dog.setStatus(BUSY);
            owner.setDog(dog);
            dog.setOwner(owner);
            ownerRepository.save(owner);
            dogRepository.save(dog);
        } else throw new PetNoFreeException("Собака забронирована другим человеком.");
    }

    /**
     * Метод удаления у человека животного по какой-либо причине со сменой статуса животного.
     * Например, при отказе в усыновлении или при форс-мажоре. Статус человека станет SEARCH.
     * При этом статус человека меняет волонтер (в зависимости от причины) и другим методом.
     *
     * @param idCatOwner номер человека в базе данных приюта.
     */
    public void takeTheCatAwayById(Long idCatOwner) {
        Owner owner = ownerRepository.findById(idCatOwner).orElseThrow();
        Cat cat = owner.getCat();
        cat.setStatus(FREE);
        cat.setOwner(null);
        catRepository.save(cat);
        owner.setCat(null);
        owner.setStatusOwner(SEARCH);
        ownerRepository.save(owner);
    }

    public void takeTheDogAwayById(Long idCatOwner) {
        Owner owner = ownerRepository.findById(idCatOwner).orElseThrow();
        Dog dog = owner.getDog();
        dog.setStatus(FREE);
        dog.setOwner(null);
        dogRepository.save(dog);
        owner.setDog(null);
        owner.setStatusOwner(SEARCH);
        ownerRepository.save(owner);
    }

    /**
     * Метод удаления "потенциального хозяина" животного (или сотрудника приюта) со сменой статуса животного
     * и очистке у него поля "потенциального хозяина".
     *
     * @param idOwner номер человека в базе данных приюта.
     */
    public void deleteCatOwnerById(Long idOwner) {
        Owner owner = ownerRepository.findById(idOwner).orElseThrow();
        if (owner.getCat() != null) {
            Cat cat = owner.getCat();
            cat.setStatus(FREE);
            cat.setOwner(null);
            catRepository.save(cat);

        }
        ownerRepository.deleteById(idOwner);
    }

    public void deleteDogOwnerById(Long idOwner) {
        Owner owner = ownerRepository.findById(idOwner).orElseThrow();
        if (owner.getDog() != null) {
            Dog dog = owner.getDog();
            dog.setStatus(FREE);
            dog.setOwner(null);
            dogRepository.save(dog);

        }
        ownerRepository.deleteById(idOwner);
    }

    /**
     * Метод получения человека по его id.
     *
     * @param idOwner номер человека в базе данных приюта.
     * @return возвращает DTO человека.
     */
    public OwnerDTO getOwner(Long idOwner) {
        return catOwnerToDTO(ownerRepository.findById(idOwner).orElseThrow());
    }

    /**
     * Метод изменения даты окончания испытательного срока. Меняет волонтер. Уведомляет "потенциального хозяина" об изменении
     * даты окончания испытательного срока
     *
     * @param idCatOwner номер человека в базе данных приюта.
     * @param plusDays   на сколько дней увеличивается старая дата завершения испытательного периода.
     */
    public void updateFinish(Long idCatOwner, int plusDays) {
        Owner owner = ownerRepository.findById(idCatOwner).orElseThrow();
        owner.setFinish(owner.getFinish().plusDays(plusDays));
        ownerRepository.save(owner);
        telegramBot.execute(new SendMessage(owner.getChatId(), "Вам продлен испытательный срок до "
                + owner.getFinish().toString()));
    }
}
