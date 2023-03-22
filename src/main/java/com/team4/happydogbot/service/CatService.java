package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.Cat;
import com.team4.happydogbot.exception.CatNotFoundException;
import com.team4.happydogbot.repository.CatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 *Класс - сервис, содержащий набор CRUD операций над объектом Cat
 * @see Cat
 * @see CatRepository
 */
@Slf4j
@Service
public class CatService {

    private final CatRepository catRepository;

    public CatService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    /**
     * Метод создает нового кота
     * @param cat
     * @return {@link CatRepository#save(Object)}
     * @see CatService
     */
    public Cat add(Cat cat) {
        log.info("Was invoked method to add a cat");
        return this.catRepository.save(cat);
    }

    /**
     * Метод находит и возвращает кота по id
     * @param id
     * @return {@link CatRepository#findById(Object)}
     * @throws CatNotFoundException если кот с указанным id не найдена
     * @see CatService
     */
    public Cat get(Long id) {
        log.info("Was invoked method to get a cat by id={}", id);
        return this.catRepository.findById(id)
                .orElseThrow(CatNotFoundException::new);
    }

    /**
     * Метод находит и удаляет кота по id
     * @param id
     * @throws CatNotFoundException если кот с указанным id не найдена
     * @see CatService
     */
    public boolean remove(Long id) {
        log.info("Was invoked method to remove a cat by id={}", id);
        if (catRepository.existsById(id)) {
        if (catRepository.getReferenceById(id).getAdopterCat() != null) {
            catRepository.getReferenceById(id).getAdopterCat().setCat(null);
        }
            catRepository.deleteById(id);
            return true;
        }
        throw new CatNotFoundException();
    }

    /**
     * Метод обновляет и возвращает кота
     * @param cat
     * @return {@link CatRepository#save(Object)}
     * @throws CatNotFoundException если кот с указанным id не найдена
     * @see CatService
     */
    public Optional<Cat> update(Cat cat) {
        log.info("Was invoked method to update a cat");
        if (catRepository.existsById(cat.getId())) {
            return Optional.of(catRepository.save(cat));
        }
        throw new CatNotFoundException();
    }

    /**
     * Method to get all cats.
     * @return {@link CatRepository#findAll()}
     * @see CatService
     */
    public Collection<Cat> getAll() {
        log.info("Was invoked method to get all cats");
        return this.catRepository.findAll();
    }
}
