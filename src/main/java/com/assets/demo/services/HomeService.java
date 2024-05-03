package com.assets.demo.services;

import com.assets.demo.dto.HomeDTO;
import com.assets.demo.models.Home;
import com.assets.demo.models.Room;
import com.assets.demo.repository.HomeRepo;
import com.assets.demo.repository.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HomeService {

    @Autowired
    private HomeRepo homeRepo;

    @Autowired
    private RoomRepo roomRepo;

    public Home createHome(HomeDTO homeDTO) {
        Home builtHome = buildBody(homeDTO);
        return homeRepo.save(builtHome);
    }

    private Home buildBody(HomeDTO homeDTO) {
        String homeName = homeDTO.getName().trim();
        String homeId = homeDTO.getUsername().toLowerCase() + ":" + homeName.toLowerCase()
                .replace(" ", "_");
        return Home.builder()
                .id(homeId)
                .name(homeName)
                .usernameID(homeDTO.getUsername())
                .position(homeDTO.getPosition())
                .build();
    }

    public Home getHomeById(String id) {
        Optional<Home> optionalHome = homeRepo.findById(id);
        return optionalHome.isPresent() ? optionalHome.get() : null;
    }

    @Transactional
    public boolean deleteHomeById(String homeID) {
        if (homeRepo.existsById(homeID)) {
            homeRepo.deleteById(homeID);
            String[] parts = homeID.split(":");
            List<Room> rooms = roomRepo.findByUsernameID(parts[0]);
            for (Room room : rooms) {
                roomRepo.deleteById(room.getId());
            }
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
