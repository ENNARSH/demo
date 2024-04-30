package com.assets.demo.repository;

import com.assets.demo.models.Home;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomeRepo extends ElasticsearchRepository<Home, String> {


    Optional<Home> findByName(String name);

    Home findByUsername(String username);

    Optional findById(String id);

    Home findByUsernameAndName(String username, String name);

    // List<Home> findAllHomesByUsername(String username);

}
