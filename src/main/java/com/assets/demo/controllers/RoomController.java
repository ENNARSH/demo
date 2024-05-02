package com.assets.demo.controllers;

import com.assets.demo.dto.RoomDTO;
import com.assets.demo.models.Home;
import com.assets.demo.models.Room;
import com.assets.demo.services.HomeService;
import com.assets.demo.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private HomeService homeService;

    @PostMapping()
    public ResponseEntity<?> createRoom(@RequestBody RoomDTO roomDTO) {
        try {
            Home home = homeService.getHomeById(roomDTO.getHomeID());
            roomDTO.setHomeID(home.getId());
            Room createdRoom = roomService.createRoom(roomDTO);
            return ResponseEntity.ok(createdRoom);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/{roomName}")
    public ResponseEntity<Room> getRoomById(@PathVariable String username, @PathVariable String homeName, @PathVariable String roomName) {
        String roomId = username.toLowerCase() + ":" + homeName.toLowerCase() + ":" + roomName.toLowerCase();
        Room room = roomService.getRoomById(roomId);
        if (room != null) {
            return ResponseEntity.ok(room);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{roomName}")
    public ResponseEntity<String> deleteRoomById(@PathVariable String username, @PathVariable String homeName, @PathVariable String roomName) {
        String roomId = username.toLowerCase() + ":" + homeName.toLowerCase() + ":" + roomName.toLowerCase();
        boolean deleted = roomService.deleteRoomById(roomId);
        if (deleted) {
            return ResponseEntity.ok("Room deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
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
