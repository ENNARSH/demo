package com.assets.demo.controllers;

import com.assets.demo.dto.HomeDTO;
import com.assets.demo.models.Home;
import com.assets.demo.services.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping("/homes")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping()
    public ResponseEntity<?> createHome(@RequestBody HomeDTO homeDTO) {
        try {
            Home createdHome = homeService.createHome(homeDTO);
            return ResponseEntity.ok(createdHome);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("create.error", new Object[]{e.getMessage()}, Locale.getDefault());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Home> getHomeById(@PathVariable String id) {
        try {
            Home home = homeService.getHomeById(id);
            if (home != null) {
                return new ResponseEntity<>(home, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Accesso vietato se la casa non appartiene al profilo
            }
        } catch (Exception e) {
            messageSource.getMessage("not.found", new Object[]{id}, Locale.getDefault());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHomeById(@PathVariable String id) {
        try {
            boolean deleted = homeService.deleteHomeById(id);
            if (deleted) {
                String successMessage = messageSource.getMessage("deleted.success", new Object[]{id}, Locale.getDefault());
                return ResponseEntity.ok(successMessage);
            } else {
                String errorMessage = messageSource.getMessage("not.found", new Object[]{id}, Locale.getDefault());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageSource.getMessage("error.deleting", null, Locale.getDefault()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Home> updateHome(@PathVariable String id, @RequestBody HomeDTO homeDTO) {
        try {
            Home updatedHome = homeService.updateHome(homeDTO);
            if (updatedHome != null && updatedHome.belongsToProfile(homeDTO)) {
                return ResponseEntity.ok(updatedHome);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            messageSource.getMessage("not.found", new Object[]{id}, Locale.getDefault());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping()
    public ResponseEntity<List<Home>> getAllHomeIds() {
        try {
            List<Home> homeIds = homeService.getAllHomeIds();
            if (!homeIds.isEmpty()) {
                return ResponseEntity.ok(homeIds);
            } else {
                String emptyListMessage = messageSource.getMessage("empty.list", null, Locale.getDefault());
                throw new RuntimeException(emptyListMessage);
            }
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("error.getting.homes", null, Locale.getDefault());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
