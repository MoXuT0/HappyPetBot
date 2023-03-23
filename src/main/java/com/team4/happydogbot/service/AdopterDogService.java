package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.AdopterDog;
import com.team4.happydogbot.exception.AdopterDogNotFoundException;
import com.team4.happydogbot.exception.DogNotFoundException;
import com.team4.happydogbot.repository.AdopterDogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 *Класс - сервис, содержащий набор CRUD операций над объектом AdopterDog
 * @see AdopterDog
 * @see AdopterDogRepository
 */
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
        log.info("Was invoked method to create a adopterDog");

        return this.adopterDogRepository.save(adopterDog);
    }

    /**
     * Метод находит и возвращает пользователя по id
     * @param id
     * @return {@link AdopterDogRepository#findById(Object)}
     * @throws AdopterDogNotFoundException если пользователь с указанным id не найден
     * @see AdopterDogService
     */
    public AdopterDog get(Long id) {
        log.info("Was invoked method to get a adopterDog by id={}", id);

        return this.adopterDogRepository.findById(id)
                .orElseThrow(AdopterDogNotFoundException::new);
    }

    /**
     * Метод находит и удаляет пользователя по id
     * @param id
     * @return true если удаление прошло успешно
     * @throws AdopterDogNotFoundException если пользователь с указанным id не найден
     */
    public boolean remove(Long id) {
        log.info("Was invoked method to remove a adopterDog by id={}", id);

        if (adopterDogRepository.existsById(id)) {
            adopterDogRepository.deleteById(id);
            return true;
        }
        throw new AdopterDogNotFoundException();
    }

    /**
     * Метод обновляет и возвращает пользователя
     * @param adopterDog
     * @return {@link AdopterDogRepository#save(Object)}
     * @throws AdopterDogNotFoundException если пользователь с указанным id не найден
     * @see AdopterDogService
     */
    public AdopterDog update(AdopterDog adopterDog) {
        log.info("Was invoked method to update a adopterCat");
        if (adopterDog.getChatId() != null && get(adopterDog.getChatId()) != null) {
            AdopterDog findAdopterDog = get(adopterDog.getChatId());
            findAdopterDog.setFirstName(adopterDog.getFirstName());
            findAdopterDog.setLastName(adopterDog.getLastName());
            findAdopterDog.setUserName(adopterDog.getUserName());
            findAdopterDog.setAge(adopterDog.getAge());
            findAdopterDog.setAddress(adopterDog.getAddress());
            findAdopterDog.setTelephoneNumber(adopterDog.getTelephoneNumber());
            findAdopterDog.setState(adopterDog.getState());
            return this.adopterDogRepository.save(findAdopterDog);
        }
        throw new DogNotFoundException();
    }

    /**
     * Метод находит всех пользователей
     * @return {@link AdopterDogRepository#findById(Object)}
     * @see AdopterDogService
     */
    public Collection<AdopterDog> getAll() {
        log.info("Was invoked method to get all adoptersDod");

        return this.adopterDogRepository.findAll();
    }
}
