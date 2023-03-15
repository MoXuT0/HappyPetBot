package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.Adopter;
import com.team4.happydogbot.repository.AdopterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
public class AdopterService {
    private final AdopterRepository adopterRepository;

    public AdopterService(AdopterRepository adopterRepository) {
        this.adopterRepository = adopterRepository;
    }

    /**
     * Метод создает нового пользователя
     *
     * @param adopter
     * @return {@link AdopterRepository#save(Object)}
     * @see AdopterService
     */
    public Adopter add(Adopter adopter) {
        return this.adopterRepository.save(adopter);
    }

    /**
     * Метод находит и возвращает пользователя по id
     *
     * @param id
     * @return {@link AdopterRepository#findById(Object)}
     * @throws IllegalArgumentException
     * @see AdopterService
     */
    public Adopter get(long id) {
        return this.adopterRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    /**
     * Метод находит и удаляет пользователя по id
     *
     * @param id
     * @return
     */
    public boolean remove(long id) {
        if (adopterRepository.existsById(id)) {
            adopterRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Метод обновляет и возвращает пользователя
     *
     * @param adopter
     * @return {@link AdopterRepository#save(Object)}
     * @throws IllegalArgumentException
     * @see AdopterService
     */
    public boolean update(Adopter adopter) {
        if (adopterRepository.existsById(adopter.getChatId())) {
            adopterRepository.save(adopter);
            return true;
        }
        throw new IllegalArgumentException();
    }

    /**
     * Метод находит всех пользователей
     *
     * @return {@link AdopterRepository#findById(Object)}
     * @see AdopterService
     */
    public Collection<Adopter> getAll() {
        return this.adopterRepository.findAll();
    }
}
