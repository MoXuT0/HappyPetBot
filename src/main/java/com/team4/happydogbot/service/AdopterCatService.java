package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.AdopterCat;
import com.team4.happydogbot.exceptions.AdopterCatNotFoundException;
import com.team4.happydogbot.repository.AdopterCatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;


@Slf4j
@Service
public class AdopterCatService {
    private final AdopterCatRepository adopterCatRepository;

    public AdopterCatService(AdopterCatRepository adopterRepository) {
        this.adopterCatRepository = adopterRepository;
    }

    /**
     * Метод создает нового пользователя
     * @param adopterCat
     * @return {@link AdopterCatRepository#save(Object)}
     * @see AdopterCatService
     */
    public AdopterCat add(AdopterCat adopterCat) {
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
        return this.adopterCatRepository.findById(id)
                .orElseThrow(AdopterCatNotFoundException::new);
    }

    /**
     * Метод находит и удаляет пользователя по id
     * @param id
     * @return
     */
    public boolean remove(Long id) {
        if (adopterCatRepository.existsById(id)) {
            adopterCatRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Метод обновляет и возвращает пользователя
     * @param adopterCat
     * @return {@link AdopterCatRepository#save(Object)}
     * @throws AdopterCatNotFoundException
     * @see AdopterCatService
     */
    public Optional<AdopterCat> update(AdopterCat adopterCat) {
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
        return this.adopterCatRepository.findAll();
    }
}
