package com.example.telegrambotanimalshelter.service;
import com.example.telegrambotanimalshelter.StringValidation;
import com.example.telegrambotanimalshelter.dto.dog.DogDTO;
import com.example.telegrambotanimalshelter.entity.Dog;
import com.example.telegrambotanimalshelter.entity.DogOwner;
import com.example.telegrambotanimalshelter.entity.enums.GenderOfPet;
import com.example.telegrambotanimalshelter.exception.NoPetException;
import com.example.telegrambotanimalshelter.repository.DogOwnerRepository;
import com.example.telegrambotanimalshelter.repository.DogRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import static com.example.telegrambotanimalshelter.dto.dog.DogDTO.dogToDTO;
import static com.example.telegrambotanimalshelter.entity.enums.GenderOfPet.FEMALE;
import static com.example.telegrambotanimalshelter.entity.enums.StatusOfPet.*;

@Service
public class DogService {
    private final DogRepository dogRepository;
    private final DogOwnerRepository dogOwnerRepository;

    public DogService(DogRepository dogRepository, DogOwnerRepository dogOwnerRepository) {
        this.dogRepository = dogRepository;
        this.dogOwnerRepository = dogOwnerRepository;

    }

    /**
     * Сохраняет новую собаку в базу данных
     *
     * @param dogDTO объект DogDTO
     * @return объект dogDTO
     * @throws com.example.telegrambotanimalshelter.exception.NoPetException при попытке добавить собаку без имени
     */
    public DogDTO addDog(DogDTO dogDTO) {
        if (!StringValidation.validation(dogDTO.nickName()) || !StringValidation.validation(dogDTO.species()) || !StringValidation.validation(dogDTO.description())) {
            throw new NoPetException("Необходимо заполнить следующие поля: имя животного, порода, описание, пол.");
        }
        if (dogDTO.birthYear() <= 2000 || dogDTO.birthYear() > LocalDate.now().getYear()) {
            throw new NoPetException("Год рождения животного не может быть меньше 2000 и больше текущего!");
        }
        Dog dog = new Dog(dogDTO.nickName(), dogDTO.birthYear(), dogDTO.gender(), dogDTO.color(), dogDTO.species(), dogDTO.description(), FREE);
        return dogToDTO(dogRepository.save(dog));
    }

    /**
     * Получает собаку из базы данных по идентификатору
     *
     * @param id идентификатор собаки
     * @return объект DogDTO с заданным id
     * @throws NoPetException в случае, если собаки с таким идентификатором нет в базе данных
     */
    public DogDTO getDog(Long id) {
        return dogToDTO(dogRepository.findById(id).orElseThrow());
    }

    /**
     * Получает из базы данных список всех собак
     *
     * @return список собак (List)
     */
    public Collection<DogDTO> getAllDogs() {
        List<Dog> dogList = dogRepository.findAll();
        return dogList.stream().map(DogDTO::dogToDTO).collect(Collectors.toSet());
    }

    /**
     * Редакирует собаку по идентификатору путем передачи в метод объекта "собака" с обновленными параметрами (цвет, владелец, статус, порода, описание, год рождения, имя) и сохраняет обновленное животное в базу данных
     *
     * @param dogDTO собака с обновленными параметрами, объект DTO
     * @return обновленное животное в виде объекта DogDTO
     * @throws NoPetException в случае, если собаки с таким идентификатором нет в базе данных
     */
    public DogDTO updateDog(DogDTO dogDTO) {
        Dog dog = dogRepository.findById(dogDTO.id()).orElseThrow();
        if(StringValidation.validation(dogDTO.nickName())){
            dog.setNickName(dogDTO.nickName());
        }
        if(StringValidation.validation(dogDTO.species())){
            dog.setSpecies(dogDTO.species());
        }
        if(dogDTO.birthYear() > 2000 || dogDTO.birthYear() > LocalDate.now().getYear()){
            dog.setBirthYear(dogDTO.birthYear());
        }
        if(StringValidation.validation(dogDTO.description())){
            dog.setDescription(dogDTO.description());
        }
        dog.setColor(dogDTO.color());
        if(dogDTO.dogOwner()!=0){
            dog.setDogOwner(dogOwnerRepository.findById(dogDTO.dogOwner()).orElseThrow());
            DogOwner dogOwner = dogOwnerRepository.findById(dogDTO.dogOwner()).orElseThrow();
            dogOwner.setDog(dog);
            dogOwnerRepository.save(dogOwner);
        }
        if(dogDTO.status()==FREE||dogDTO.status()==BUSY||dogDTO.status()==ADOPTIVE){
            dog.setStatus(dogDTO.status());
        }
        if(dogDTO.gender()== GenderOfPet.MALE||dogDTO.gender()==FEMALE){
            dog.setGender(dogDTO.gender());
        }

        return dogToDTO(dogRepository.save(dog));
    }
    /**
     * Удаляет собаку из базы данных по идентификатору
     *
     * @param id идентификатор собаки
     * @throws NoPetException в случае, если собаки с таким идентификатором нет в базе данных
     */
    public void removeDog(long id) {
        Dog dog = dogRepository.findById(id).orElseThrow();
        DogOwner dogOwner = dog.getDogOwner();
        if(dogOwner == null) {
            dogRepository.deleteById(id);
        } else {
            dogOwner.setDog(null);
            dogOwnerRepository.save(dogOwner);
            dogRepository.deleteById(id);
        }
    }
}