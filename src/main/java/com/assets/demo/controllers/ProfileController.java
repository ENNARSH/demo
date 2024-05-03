package com.assets.demo.controllers;

import com.assets.demo.dto.ProfileDTO;
import com.assets.demo.models.Profile;
import com.assets.demo.utils.ProfileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.assets.demo.services.ProfileService;

import java.net.ConnectException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileUtils profileUtils;

    @Autowired
    private MessageSource messageSource;

    @PostMapping()
    public ResponseEntity<?> createProfile(@RequestBody ProfileDTO profileDTO) {
        try {
            profileUtils.checkDTO(profileDTO);
            Profile createdProfile = profileService.createProfile(profileDTO);
            return ResponseEntity.ok(createdProfile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfileById(@PathVariable String id) {
        try {
            Optional<Profile> profile = profileService.getProfileById(id);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (e.getCause() instanceof ConnectException) {
                String errorMessage = messageSource.getMessage("db.connection.error", null, Locale.getDefault());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la richiesta");
        }
    }


    @GetMapping()
    public ResponseEntity<?> getAllProfileIds() {
        List<Profile> profileIds = profileService.getAllProfileIds();
        if (!profileIds.isEmpty()) {
            return ResponseEntity.ok(profileIds);
        } else {
            String emptyListMessage = messageSource.getMessage("profile.list.empty", null, getLocale());
            return ResponseEntity.ok(emptyListMessage);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfileById(@PathVariable String id) {

        boolean deleted = profileService.deleteProfileById(id);

        if (deleted) {
            String successMessage = messageSource.getMessage("profile.deleted.success", new Object[]{id}, getLocale());
            return ResponseEntity.ok(successMessage);
        } else {
            String notFoundMessage = messageSource.getMessage("profile.not.found", new Object[]{id}, getLocale());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundMessage);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable String id, @RequestBody ProfileDTO profileDTO) {
        Profile updatedProfile = profileService.updateProfile(id, profileDTO);
        if (updatedProfile != null) {
            return ResponseEntity.ok(updatedProfile);
        } else {
            String notFoundMessage = messageSource.getMessage("profile.not.found", new Object[]{id}, getLocale());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundMessage);
        }
    }

    private Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }


}
