package com.assets.demo.repository;

import com.assets.demo.models.Home;
import com.assets.demo.models.Room;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepo extends ElasticsearchRepository<Room, String> {

    Optional<Home> findByName(String name);

    void deleteByUsernameID(String usernameID);

}
