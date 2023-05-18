package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.Dog;
import com.team4.happydogbot.exception.DogNotFoundException;
import com.team4.happydogbot.repository.DogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 *Класс - сервис, содержащий набор CRUD операций над объектом Dog
 * @see Dog
 * @see DogRepository
 */
@Slf4j
@Service
public class DogService {

    private final DogRepository dogRepository;

    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    /**
     * Метод создает новую собаку
     * @param dog
     * @return {@link DogRepository#save(Object)}
     * @see DogService
     */
    public Dog add(Dog dog) {
        log.info("Was invoked method to create a dog");

        return this.dogRepository.save(dog);
    }

    /**
     * Метод находит и возвращает собаку по id
     * @param id
     * @return {@link DogRepository#findById(Object)}
     * @throws DogNotFoundException если собака с указанным id не найдена
     * @see DogService
     */
    public Dog get(Long id) {
        log.info("Was invoked method to get a dog by id={}", id);

        return this.dogRepository.findById(id)
                .orElseThrow(DogNotFoundException::new);
    }

    /**
     * Метод находит и удаляет собаку по id
     * @param id
     * @return true если удаление прошло успешно
     * @throws DogNotFoundException если собака с указанным id не найдена
     */
    public boolean remove(Long id) {
        log.info("Was invoked method to remove a dog by id={}", id);

        if (dogRepository.existsById(id)) {
            if (dogRepository.getReferenceById(id).getAdopterDog() != null) {
                dogRepository.getReferenceById(id).getAdopterDog().setDog(null);
            }
            dogRepository.deleteById(id);
            return true;
        }
        throw new DogNotFoundException();
    }

    /**
     * Метод обновляет и возвращает собаку
     * @param dog
     * @return {@link DogRepository#save(Object)}
     * @throws DogNotFoundException если собака с указанным id не найдена
     * @see DogService
     */
    public Dog update(Dog dog) {
        log.info("Was invoked method to update a dog");

        if (dog.getId() != null && get(dog.getId()) != null) {
            Dog findDog = get(dog.getId());
            findDog.setName(dog.getName());
            findDog.setBreed(dog.getBreed());
            findDog.setYearOfBirth(dog.getYearOfBirth());
            findDog.setDescription(dog.getDescription());
            return this.dogRepository.save(findDog);
        }
        throw new DogNotFoundException();
    }

    /**
     * Метод находит и возвращает всех собак
     * @return {@link DogRepository#findById(Object)}
     * @see DogService
     */
    public Collection<Dog> getAll() {
        log.info("Was invoked method to get all dogs");

        return this.dogRepository.findAll();
    }
}
