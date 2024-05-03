package com.assets.demo.controllers;

import com.assets.demo.dto.RoomDTO;
import com.assets.demo.models.Home;
import com.assets.demo.models.Room;
import com.assets.demo.services.HomeService;
import com.assets.demo.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private HomeService homeService;

    @Autowired
    private MessageSource messageSource;

    @PostMapping()
    public ResponseEntity<?> createRoom(@RequestBody RoomDTO roomDTO) {
        try {
            Home home = homeService.getHomeById(roomDTO.getHomeID());
            roomDTO.setHomeID(home.getId());
            Room createdRoom = roomService.createRoom(roomDTO);
            return ResponseEntity.ok(createdRoom);
        } catch (Exception e) {
            String errorMessage = messageSource.getMessage("create.error", new Object[]{e.getMessage()}, Locale.getDefault());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }


    @GetMapping("/{roomName}")
    public ResponseEntity<Room> getRoomById(@PathVariable String username, @PathVariable String homeName, @PathVariable String roomName) {
        try {
            String roomId = username.toLowerCase() + ":" + homeName.toLowerCase() + ":" + roomName.toLowerCase();
            Room room = roomService.getRoomById(roomId);
            if (room != null) {
                return ResponseEntity.ok(room);
            } else {
                messageSource.getMessage("not.found", new Object[]{roomId}, Locale.getDefault());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{roomName}")
    public ResponseEntity<String> deleteRoomById(@PathVariable String username, @PathVariable String homeName, @PathVariable String roomName) {
        try {
            String roomId = username.toLowerCase() + ":" + homeName.toLowerCase() + ":" + roomName.toLowerCase();
            boolean deleted = roomService.deleteRoomById(roomId);
            if (deleted) {
                String successMessage = messageSource.getMessage("deleted.success", new Object[]{roomId}, Locale.getDefault());
                return ResponseEntity.ok(successMessage);
            } else {
                String errorMessage = messageSource.getMessage("not.found", new Object[]{roomId}, Locale.getDefault());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageSource.getMessage("error.deleting", null, Locale.getDefault()));
        }
    }

    @PutMapping("/{roomName}")
    public ResponseEntity<Room> updateRoom(@PathVariable String username, @PathVariable String homeName, @PathVariable String roomName, @RequestBody RoomDTO roomDTO) {
        String roomId = username.toLowerCase() + ":" + homeName.toLowerCase() + ":" + roomName.toLowerCase();
        Room updatedRoom = roomService.updateRoom(roomId, roomDTO);
        if (updatedRoom != null) {
            return ResponseEntity.ok(updatedRoom);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
