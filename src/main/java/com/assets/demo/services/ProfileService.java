package com.assets.demo.services;

import com.assets.demo.dto.ProfileDTO;
import com.assets.demo.models.Profile;
import com.assets.demo.repository.ProfileRepo;
import com.assets.demo.utils.ProfileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepo profileRepo;

    @Autowired
    private ProfileUtils profileUtils;


    public Profile createProfile(ProfileDTO profileDTO) {
        Profile builtProfile = buildBody(profileDTO);
        return profileRepo.save(builtProfile);
    }

    private Profile buildBody(ProfileDTO profileDTO) {
        String username = profileDTO.getUsername();
//        String encryptedPassword = encryptPassword(profileDTO.getPassword());
        String lowUsername = username.toLowerCase();
        return Profile.builder()
                .username(username)
                .id(lowUsername)  // id = username
                .name(profileDTO.getName())
                .surname(profileDTO.getSurname())
                .password(profileDTO.getPassword())
                .build();
    }

//    private String encryptPassword(String password) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        return passwordEncoder.encode(password);
//    }

    public Profile getProfileById(String id) {
        return profileRepo.findById(id).orElse(null);
    }

    public List<Profile> getAllProfileIds() {
        return profileRepo.findAllIds();
    }

    public boolean deleteProfileById(String id) {
        if (profileRepo.existsById(id)) {
            profileRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Profile updateProfile(String id, ProfileDTO profileDTO) {
        Profile existingProfile = profileRepo.findById(id).orElse(null);
        if (existingProfile != null) {
            existingProfile.setUsername(profileDTO.getUsername());
            existingProfile.setName(profileDTO.getName());
            existingProfile.setSurname(profileDTO.getSurname());
            existingProfile.setPassword(profileDTO.getPassword());
            return profileRepo.save(existingProfile);
        } else {
            return null;
        }
    }


}

