package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.AdopterDog;
import com.team4.happydogbot.repository.AdopterDogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class AdopterDogService {
    private final AdopterDogRepository adopterDogRepository;

    public AdopterDogService(AdopterDogRepository adopterDogRepository) {
        this.adopterDogRepository = adopterDogRepository;
    }

    /**
     * Метод создает нового пользователя
     * @param adopterDog
     * @return {@link AdopterDogRepository#save(Object)}
     * @see AdopterDogService
     */
    public AdopterDog add(AdopterDog adopterDog) {
        return this.adopterDogRepository.save(adopterDog);
    }

    /**
     * Метод находит и возвращает пользователя по id
     * @param id
     * @return {@link AdopterDogRepository#findById(Object)}
     * @throws IllegalArgumentException
     * @see AdopterDogService
     */
    public AdopterDog get(Long id) {
        return this.adopterDogRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Метод находит и удаляет пользователя по id
     * @param id
     * @return
     */
    public boolean remove(Long id) {
        if (adopterDogRepository.existsById(id)) {
            adopterDogRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Метод обновляет и возвращает пользователя
     * @param adopterDog
     * @return {@link AdopterDogRepository#save(Object)}
     * @throws IllegalArgumentException
     * @see AdopterDogService
     */
    public Optional<AdopterDog> update(AdopterDog adopterDog) {
        if (adopterDogRepository.existsById(adopterDog.getChatId())) {
            return Optional.ofNullable(adopterDogRepository.save(adopterDog));
        }
        throw new IllegalArgumentException();
    }

    /**
     * Метод находит всех пользователей
     * @return {@link AdopterDogRepository#findById(Object)}
     * @see AdopterDogService
     */
    public Collection<AdopterDog> getAll() {
        return this.adopterDogRepository.findAll();
    }
}
