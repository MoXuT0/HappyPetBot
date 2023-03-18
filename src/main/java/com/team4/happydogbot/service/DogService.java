package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.Dog;
import com.team4.happydogbot.repository.DogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

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
        return this.dogRepository.save(dog);
    }

    /**
     * Метод находит и возвращает собаку по id
     * @param id
     * @return {@link DogRepository#findById(Object)}
     * @throws IllegalArgumentException
     * @see DogService
     */
    public Dog get(Long id) {
        return this.dogRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Метод находит и удаляет собаку по id
     * @param id
     * @return
     */
    public boolean remove(Long id) {
        if (dogRepository.existsById(id)) {
            dogRepository.getReferenceById(id).getAdopterDog().setDog(null);
            dogRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Метод обновляет и возвращает собаку
     * @param dog
     * @return {@link DogRepository#save(Object)}
     * @throws IllegalArgumentException
     * @see DogService
     */
    public Optional<Dog> update(Dog dog) {
        if (dogRepository.existsById(dog.getId())) {
            return Optional.ofNullable(dogRepository.save(dog));
        }
        throw new IllegalArgumentException();
    }

    /**
     * Метод находит всех собак
     * @return {@link DogRepository#findById(Object)}
     * @see DogService
     */
    public Collection<Dog> getAll() {
        return this.dogRepository.findAll();
    }
}
