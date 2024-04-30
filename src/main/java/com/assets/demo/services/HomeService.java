package com.assets.demo.services;

import com.assets.demo.dto.HomeDTO;
import com.assets.demo.models.Home;
import com.assets.demo.repository.HomeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HomeService {

    @Autowired
    private HomeRepo homeRepo;

    public Home createHome(HomeDTO homeDTO) {
        Home builtHome = buildBody(homeDTO);
        return homeRepo.save(builtHome);
    }

    private Home buildBody(HomeDTO homeDTO) {
        String homeName = homeDTO.getName().trim();
        String homeId = homeDTO.getUsername().toLowerCase() + ":" + homeName.toLowerCase().replace(" ", "_");
        return Home.builder()
                .id(homeId)
                .name(homeName)
                .username(homeDTO.getUsername())
                .build();
    }


    public Home getHomeById(String id) {
        Optional<Home> optionalHome = homeRepo.findById(id);
        return optionalHome.isPresent() ? optionalHome.get() : null;
    }


//    public List<Home> getAllHomes(String username) {
//        return homeRepo.findAllHomesByUsername(username);
//    }

    public boolean deleteHomeById(String username) {
        if (homeRepo.existsById(username)) {
            homeRepo.deleteById(username);
            return true;
        } else {
            return false;
        }
    }

    public Home updateHome(HomeDTO homeDTO) {
        Optional<Home> optionalExistingHome = homeRepo.findById(homeDTO.getUsername());
        if (optionalExistingHome.isPresent()) {
            Home existingHome = optionalExistingHome.get();
            existingHome.setName(homeDTO.getName());
            return homeRepo.save(existingHome);
        } else {
            return null;
        }
    }


}
