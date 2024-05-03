package com.assets.demo.services;

import com.assets.demo.dto.ProfileDTO;
import com.assets.demo.models.Profile;
import com.assets.demo.repository.HomeRepo;
import com.assets.demo.repository.ProfileRepo;
import com.assets.demo.repository.RoomRepo;
import com.assets.demo.utils.ProfileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.elasticsearch.UncategorizedElasticsearchException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepo profileRepo;

    @Autowired
    private ProfileUtils profileUtils;

    @Autowired
    private HomeRepo homeRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private MessageSource messageSource;


    public Profile createProfile(ProfileDTO profileDTO) {
        try {
            Profile builtProfile = buildBody(profileDTO);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encryptedPassword = passwordEncoder.encode(profileDTO.getPassword());
            builtProfile.setPassword(encryptedPassword);
            return profileRepo.save(builtProfile);
        } catch (UncategorizedElasticsearchException e) {
            String errorMessage = messageSource.getMessage("db.connection.error", null, Locale.getDefault());
            throw new RuntimeException(errorMessage, e);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("profile.create.error", new Object[]{e.getMessage()}, Locale.getDefault());
            throw new RuntimeException(errorMessage, e);
        }
    }

    private Profile buildBody(ProfileDTO profileDTO) {
        try {
            if (profileDTO == null) {
                String errorMessage = messageSource.getMessage("profile.create.error", new Object[]{"ProfileDTO non può essere null"}, Locale.getDefault());
                throw new IllegalArgumentException(errorMessage);
            }
            String username = profileDTO.getUsername();
            if (username == null) {
                String errorMessage = messageSource.getMessage("profile.create.error", new Object[]{"Username non può essere null"}, Locale.getDefault());
                throw new IllegalArgumentException(errorMessage);
            }
            String lowUsername = username.toLowerCase();
            return Profile.builder()
                    .username(username)
                    .id(lowUsername)  // id = username
                    .name(profileDTO.getName())
                    .surname(profileDTO.getSurname())
                    .password(profileDTO.getPassword())
                    .build();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public Optional<Profile> getProfileById(String id) {
        return profileRepo.findById(id);
    }


    public List<Profile> getAllProfileIds() {
        return profileRepo.findAllIds();
    }

    @Transactional
    public boolean deleteProfileById(String id) {
        try {
            if (profileRepo.existsById(id)) {
                Profile profile = profileRepo.findById(id).orElse(null);
                if (profile != null) {
                    String username = profile.getUsername();
                    profileRepo.delete(profile);
                    homeRepo.deleteByUsernameID(username);
                    roomRepo.deleteByUsernameID(username);
                    return true;
                }
            }
            return false;
        } catch (TransactionException e) {
            throw new RuntimeException("Impossibile eliminare il profilo con id " + id, e);
        }
    }

    public Profile updateProfile(String id, ProfileDTO profileDTO) {
        Profile existingProfile = null;
        try {
            if (profileRepo.findById(id).isPresent()) {
                existingProfile = profileRepo.findById(id).get();
            }
            if (existingProfile != null) {
                existingProfile.setUsername(profileDTO.getUsername());
                existingProfile.setName(profileDTO.getName());
                existingProfile.setSurname(profileDTO.getSurname());
                existingProfile.setPassword(profileDTO.getPassword());
                return profileRepo.save(existingProfile);
            }
        } catch (Exception e) {
            throw new RuntimeException("Profilo non trovato con id " + id, e);
        }
        return null;
    }


}


