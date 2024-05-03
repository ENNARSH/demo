package com.assets.demo.services;

import com.assets.demo.dto.RoomDTO;
import com.assets.demo.models.Room;
import com.assets.demo.repository.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class RoomService {

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private MessageSource messageSource;

    public Room createRoom(RoomDTO roomDTO) {
        try {
            Room builtRoom = buildBody(roomDTO);
            return roomRepo.save(builtRoom);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("create.error", new Object[]{e.getMessage()}, Locale.getDefault());
            throw new RuntimeException(errorMessage, e);
        }
    }

    private Room buildBody(RoomDTO roomDTO) {
        String cleanedRoomName = roomDTO.getName().trim();
        String roomId = roomDTO.getHomeID() + ":" + cleanedRoomName.toLowerCase().replace(" ", "_");
        String[] parts = roomDTO.getHomeID().split(":");
        return Room.builder()
                .id(roomId)
                .name(cleanedRoomName)
                .homeID(roomDTO.getHomeID())
                .usernameID(parts[0])
                .build();
    }

    public Room getRoomById(String id) {
        try {
            return roomRepo.findById(id).orElseThrow(() -> new RuntimeException(messageSource.getMessage("not.found", new Object[]{id}, Locale.getDefault())));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public boolean deleteRoomById(String id) {
        try {
            if (roomRepo.existsById(id)) {
                roomRepo.deleteById(id);
                return true;
            } else {
                throw new RuntimeException(messageSource.getMessage("not.found", new Object[]{id}, Locale.getDefault()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Room updateRoom(String id, RoomDTO roomDTO) {
        try {
            Room existingRoom = roomRepo.findById(id).orElseThrow(() -> new RuntimeException(messageSource.getMessage("not.found", new Object[]{id}, Locale.getDefault())));
            if (existingRoom != null) {
                existingRoom.setName(roomDTO.getName());
                return roomRepo.save(existingRoom);
            } else {
                throw new RuntimeException(messageSource.getMessage("not.found", new Object[]{id}, Locale.getDefault()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


}

