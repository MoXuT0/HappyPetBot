package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.Cat;
import com.team4.happydogbot.exceptions.CatNotFoundException;
import com.team4.happydogbot.repository.CatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

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
        return this.catRepository.save(cat);
    }

    /**
     * Метод находит и возвращает кота по id
     * @param id
     * @return {@link CatRepository#findById(Object)}
     * @see CatService
     * @exception CatNotFoundException
     */
    public Cat get(Long id) {
        return this.catRepository.findById(id)
                .orElseThrow(CatNotFoundException::new);
    }

    /**
     * Метод находит и удаляет кота по id
     * @param id
     */
    public boolean remove(Long id) {
        if (catRepository.existsById(id)) {
            catRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Метод обновляет и возвращает кота
     * @param cat
     * @return {@link CatRepository#save(Object)}
     * @see CatService
     * @exception CatNotFoundException
     */
    public Optional<Cat> update(Cat cat) {
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
        return this.catRepository.findAll();
    }
}
