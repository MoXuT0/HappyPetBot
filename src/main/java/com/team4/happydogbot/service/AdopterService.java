package com.team4.happydogbot.service;

import com.team4.happydogbot.entity.Adopter;
import com.team4.happydogbot.repository.AdopterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdopterService {

    private final AdopterRepository adopterRepository;


    public AdopterService(AdopterRepository adopterRepository) {
        this.adopterRepository = adopterRepository;
    }

    public Adopter add(Adopter adopter) {
        return this.adopterRepository.save(adopter);
    }

    public Adopter get(long id) {
        return this.adopterRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
