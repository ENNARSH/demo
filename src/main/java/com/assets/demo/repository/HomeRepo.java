package com.assets.demo.repository;

import com.assets.demo.models.Home;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeRepo extends ElasticsearchRepository<Home, String> {


    Optional<Home> findByName(String name);

    Home findByUsernameID(String usernameID);

    Optional findById(String id);

    Home findByUsernameIDAndName(String usernameID, String name);

    void deleteByUsernameID(String usernameID);

    @Query("{\"match_all\": {}}")
    List<Home> findAllIds();

}
