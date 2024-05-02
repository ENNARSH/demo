package com.assets.demo.services;

import com.assets.demo.dto.ProfileDTO;
import com.assets.demo.models.Profile;
import com.assets.demo.repository.HomeRepo;
import com.assets.demo.repository.ProfileRepo;
import com.assets.demo.repository.RoomRepo;
import com.assets.demo.utils.ProfileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    public Profile createProfile(ProfileDTO profileDTO) {
        Profile builtProfile = buildBody(profileDTO);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(profileDTO.getPassword());
        builtProfile.setPassword(encryptedPassword);
        return profileRepo.save(builtProfile);
    }

    private Profile buildBody(ProfileDTO profileDTO) {
        String username = profileDTO.getUsername();
        String lowUsername = username.toLowerCase();
        return Profile.builder()
                .username(username)
                .id(lowUsername)  // id = username
                .name(profileDTO.getName())
                .surname(profileDTO.getSurname())
                .password(profileDTO.getPassword())
                .build();
    }

    public Profile getProfileById(String id) {
        return profileRepo.findById(id).orElse(null);
    }

    public List<Profile> getAllProfileIds() {
        return profileRepo.findAllIds();
    }

    @Transactional
    public boolean deleteProfileById(String id) {
        if (profileRepo.existsById(id)) {
            Profile profile = profileRepo.findById(id).orElse(null);
            if (profile != null) {
                String username = profile.getUsername();
                profileRepo.delete(profile);
                // Elimina le case associate al profilo
                homeRepo.deleteByUsernameID(username);
                // Elimina le stanze associate al profilo
                roomRepo.deleteByUsernameID(username);
                return true;
            }
        }
        return false;
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


