package com.assets.demo.services;

import com.assets.demo.dto.HomeDTO;
import com.assets.demo.models.Home;
import com.assets.demo.models.Room;
import com.assets.demo.repository.HomeRepo;
import com.assets.demo.repository.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class HomeService {

    @Autowired
    private HomeRepo homeRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private MessageSource messageSource;

    public Home createHome(HomeDTO homeDTO) {
        try {
            Home builtHome = buildBody(homeDTO);
            return homeRepo.save(builtHome);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("create.error", new Object[]{e.getMessage()}, Locale.getDefault());
            throw new RuntimeException(errorMessage, e);
        }
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
        try {
            Optional<Home> optionalHome = homeRepo.findById(id);
            return optionalHome.orElseThrow(() -> new RuntimeException(messageSource.getMessage("not.found", new Object[]{id}, Locale.getDefault())));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Transactional
    public boolean deleteHomeById(String homeID) {
        try {
            if (homeRepo.existsById(homeID)) {
                homeRepo.deleteById(homeID);
                String[] parts = homeID.split(":");
                List<Room> rooms = roomRepo.findByUsernameID(parts[0]);
                for (Room room : rooms) {
                    roomRepo.deleteById(room.getId());
                }
                return true;
            } else {
                throw new RuntimeException(messageSource.getMessage("not.found", new Object[]{homeID}, Locale.getDefault()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Home updateHome(HomeDTO homeDTO) {
        try {
            Optional<Home> optionalExistingHome = homeRepo.findById(homeDTO.getUsername());
            if (optionalExistingHome.isPresent()) {
                Home existingHome = optionalExistingHome.get();
                existingHome.setName(homeDTO.getName());
                return homeRepo.save(existingHome);
            } else {
                throw new RuntimeException(messageSource.getMessage("not.found", new Object[]{homeDTO.getUsername()}, Locale.getDefault()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


}
