package com.example.telegrambotanimalshelter.service;
import com.example.telegrambotanimalshelter.StringValidation;
import com.example.telegrambotanimalshelter.dto.cat.CatDTO;
import com.example.telegrambotanimalshelter.entity.Cat;
import com.example.telegrambotanimalshelter.entity.CatOwner;
import com.example.telegrambotanimalshelter.entity.enums.GenderOfPet;
import com.example.telegrambotanimalshelter.exception.NoPetException;
import com.example.telegrambotanimalshelter.repository.CatOwnerRepository;
import com.example.telegrambotanimalshelter.repository.CatRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import static com.example.telegrambotanimalshelter.dto.cat.CatDTO.catToDTO;
import static com.example.telegrambotanimalshelter.entity.enums.GenderOfPet.FEMALE;
import static com.example.telegrambotanimalshelter.entity.enums.StatusOfPet.*;

@Service
public class CatService {
    private final CatRepository catRepository;
    private final CatOwnerRepository catOwnerRepository;

    public CatService(CatRepository catRepository, CatOwnerRepository catOwnerRepository) {
        this.catRepository = catRepository;
        this.catOwnerRepository = catOwnerRepository;

    }

    /**
     * Сохраняет новую кошку в базу данных
     *
     * @param catDTO объект CatDTO
     * @return объект catDTO
     * @throws NoPetException при попытке добавить кошку без имени
     */
    public CatDTO addCat(CatDTO catDTO) {
        if (!StringValidation.validation(catDTO.nickName()) || !StringValidation.validation(catDTO.species()) || !StringValidation.validation(catDTO.description())) {
            throw new NoPetException("Необходимо заполнить следующие поля: имя животного, порода, описание, пол.");
        }
        if (catDTO.birthYear() <= 2000 || catDTO.birthYear() > LocalDate.now().getYear()) {
            throw new NoPetException("Год рождения животного не может быть меньше 2000 и больше текущего!");
        }
        Cat cat = new Cat(catDTO.nickName(), catDTO.birthYear(), catDTO.gender(), catDTO.color(), catDTO.species(), catDTO.description(), FREE);
        return catToDTO(catRepository.save(cat));
    }

    /**
     * Получает кошку из базы данных по идентификатору
     *
     * @param id идентификатор кошки
     * @return объект CatDTO с заданным id
     * @throws NoPetException в случае, если кошки с таким идентификатором нет в базе данных
     */
    public CatDTO getCat(Long id) {
        return catToDTO(catRepository.findById(id).orElseThrow());
    }

    /**
     * Получает из базы данных список всех кошек
     *
     * @return список кошек (List)
     */
    public Collection<CatDTO> getAllCats() {
        List<Cat> catList = catRepository.findAll();
        return catList.stream().map(CatDTO::catToDTO).collect(Collectors.toSet());
    }

    /**
     * Редакирует кошку по идентификатору путем передачи в метод объекта "кошка" с обновленными параметрами (цвет, владелец, статус, порода, описание, год рождения, имя) и сохраняет обновленное животное в базу данных
     *
     * @param catDTO кошка с обновленными параметрами, объект DTO
     * @return обновленное животное в виде объекта CatDTO
     * @throws NoPetException в случае, если кошки с таким идентификатором нет в базе данных
     */
    public CatDTO updateCat(CatDTO catDTO) {
        Cat cat = catRepository.findById(catDTO.id()).orElseThrow();
        if(StringValidation.validation(catDTO.nickName())){
            cat.setNickName(catDTO.nickName());
        }
        if(StringValidation.validation(catDTO.species())){
            cat.setSpecies(catDTO.species());
        }
        if(catDTO.birthYear() > 2000 || catDTO.birthYear() > LocalDate.now().getYear()){
            cat.setBirthYear(catDTO.birthYear());
        }
        if(StringValidation.validation(catDTO.description())){
            cat.setDescription(catDTO.description());
        }
        cat.setColor(catDTO.color());
        if(catDTO.catOwner()!=0){
            cat.setCatOwner(catOwnerRepository.findById(catDTO.catOwner()).orElseThrow());
            CatOwner catOwner = catOwnerRepository.findById(catDTO.catOwner()).orElseThrow();
            catOwner.setCat(cat);
            catOwnerRepository.save(catOwner);
        }
        if(catDTO.status()==FREE||catDTO.status()==BUSY||catDTO.status()==ADOPTIVE){
            cat.setStatus(catDTO.status());
        }
        if(catDTO.gender()== GenderOfPet.MALE||catDTO.gender()==FEMALE){
            cat.setGender(catDTO.gender());
        }

        return catToDTO(catRepository.save(cat));
    }
    /**
     * Удаляет кошку из базы данных по идентификатору
     *
     * @param id идентификатор кошки
     * @throws NoPetException в случае, если кошки с таким идентификатором нет в базе данных
     */
    public void removeCat(long id) {
        Cat cat = catRepository.findById(id).orElseThrow();
        CatOwner catOwner = cat.getCatOwner();
        if(catOwner == null) {
            catRepository.deleteById(id);
        } else {
            catOwner.setCat(null);
            catOwnerRepository.save(catOwner);
            catRepository.deleteById(id);
        }
    }
}

