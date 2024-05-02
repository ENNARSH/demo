package com.assets.demo.services;

import com.assets.demo.dto.RoomDTO;
import com.assets.demo.models.Room;
import com.assets.demo.repository.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    @Autowired
    private RoomRepo roomRepo;

    public Room createRoom(RoomDTO roomDTO) {
        Room builtRoom = buildBody(roomDTO);
        return roomRepo.save(builtRoom);
    }

    private Room buildBody(RoomDTO roomDTO) {
        String cleanedRoomName = roomDTO.getName().trim();
        String roomId = roomDTO.getHomeID() + ":" + cleanedRoomName.toLowerCase()
                .replace(" ", "_");
        return Room.builder()
                .id(roomId)
                .name(cleanedRoomName)
                .homeID(roomDTO.getHomeID())
                .build();
    }


    public Room getRoomById(String id) {
        return roomRepo.findById(id).orElse(null);
    }

    public boolean deleteRoomById(String id) {
        if (roomRepo.existsById(id)) {
            roomRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Room updateRoom(String id, RoomDTO roomDTO) {
        Room existingRoom = roomRepo.findById(id).orElse(null);
        if (existingRoom != null) {
            existingRoom.setName(roomDTO.getName());
            return roomRepo.save(existingRoom);
        } else {
            return null;
        }
    }


}

