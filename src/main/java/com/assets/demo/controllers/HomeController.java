package com.assets.demo.controllers;

import com.assets.demo.dto.HomeDTO;
import com.assets.demo.models.Home;
import com.assets.demo.services.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/homes")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @PostMapping("/")
    public ResponseEntity<?> createHome(@RequestBody HomeDTO homeDTO) {
        try {
            Home createdHome = homeService.createHome(homeDTO);
            return ResponseEntity.ok(createdHome);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{username}/{id}")
    public ResponseEntity<Home> getHomeById(@PathVariable HomeDTO homeDTO, @PathVariable String id) {
        Home home = homeService.getHomeById(id);
        if (home != null) {
            if (home.belongsToProfile(homeDTO)) {
                return new ResponseEntity<>(home, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Accesso vietato se la casa non appartiene al profilo
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/{username}/homes/all")
//    public ResponseEntity<List<Home>> getAllHomes(@PathVariable String username) {
//        List<Home> homes = homeService.getAllHomes(username);
//        return new ResponseEntity<>(homes, HttpStatus.OK);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHomeById(@RequestBody String username, @PathVariable String id) {
        boolean deleted = homeService.deleteHomeById(username);
        if (deleted) {
            return ResponseEntity.ok("Casa eliminata con successo");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Casa non trovata");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Home> updateHome(@PathVariable String id, @RequestBody HomeDTO homeDTO) {
        Home updatedHome = homeService.updateHome(homeDTO);
        if (updatedHome != null) {
            if (updatedHome.belongsToProfile(homeDTO)) {
                return ResponseEntity.ok(updatedHome);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
