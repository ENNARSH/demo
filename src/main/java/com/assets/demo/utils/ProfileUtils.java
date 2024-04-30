package com.assets.demo.utils;

import com.assets.demo.dto.ProfileDTO;
import com.assets.demo.repository.ProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileUtils {

    @Autowired
    private ProfileRepo profileRepo;

    public void checkDTO(ProfileDTO profileDTO) {
        if (profileDTO.getName().length() > 15 || profileDTO.getSurname().length() > 15 || profileDTO.getPassword().length() > 15) {
            throw new IllegalArgumentException("La lunghezza dei campi nome, cognome e password deve essere inferiore o uguale a 15 caratteri.");
        }

        String username = profileDTO.getUsername();
        if (profileRepo.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("L'username è già stato utilizzato");
        }
    }

}
