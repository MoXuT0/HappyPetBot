package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.AdopterCat;
import com.team4.happydogbot.exception.AdopterCatNotFoundException;
import com.team4.happydogbot.repository.AdopterCatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Optional;

/**
 *Класс - сервис, содержащий набор CRUD операций над объектом AdopterCat
 * @see AdopterCat
 * @see AdopterCatRepository
 */
@Slf4j
@Service
public class AdopterCatService {

    private final AdopterCatRepository adopterCatRepository;

    public AdopterCatService(AdopterCatRepository adopterCatRepository) {
        this.adopterCatRepository = adopterCatRepository;
    }

    /**
     * Метод создает нового пользователя
     * @param adopterCat
     * @return {@link AdopterCatRepository#save(Object)}
     * @see AdopterCatService
     */
    public AdopterCat add(AdopterCat adopterCat) {
        log.info("Was invoked method to add a adopterCat");
        return this.adopterCatRepository.save(adopterCat);
    }

    /**
     * Метод находит и возвращает пользователя по id
     * @param id
     * @return {@link AdopterCatRepository#findById(Object)}
     * @throws AdopterCatNotFoundException
     * @see AdopterCatService
     */
    public AdopterCat get(Long id) {
        log.info("Was invoked method to get a adopterCat by id={}", id);
        return this.adopterCatRepository.findById(id)
                .orElseThrow(AdopterCatNotFoundException::new);
    }

    /**
     * Метод находит и удаляет пользователя по id
     * @param id
     * @throws AdopterCatNotFoundException
     * @see AdopterCatService
     */
    public boolean remove(Long id) {
        log.info("Was invoked method to remove a adopterCat by id={}", id);
        if (adopterCatRepository.existsById(id)) {
            adopterCatRepository.deleteById(id);
            return true;
        }
        throw new AdopterCatNotFoundException();
    }

    /**
     * Метод обновляет и возвращает пользователя
     * @param adopterCat
     * @return {@link AdopterCatRepository#save(Object)}
     * @throws AdopterCatNotFoundException
     * @see AdopterCatService
     */
    public Optional<AdopterCat> update(AdopterCat adopterCat) {
        log.info("Was invoked method to update a adopterCat");
        if (adopterCatRepository.existsById(adopterCat.getChatId())) {
            return Optional.of(adopterCatRepository.save(adopterCat));
        }
        throw new AdopterCatNotFoundException();
    }

    /**
     * Метод находит всех пользователей
     * @return {@link AdopterCatRepository#findById(Object)}
     * @see AdopterCatService
     */
    public Collection<AdopterCat> getAll() {
        log.info("Was invoked method to get all adoptersCat");
        return this.adopterCatRepository.findAll();
    }
}
