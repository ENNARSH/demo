package com.assets.demo.repository;

import com.assets.demo.models.Profile;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepo extends ElasticsearchRepository<Profile, String> {

    @Query("{\"match_all\": {}}")
    List<Profile> findAllIds();

    Optional<Profile> findByUsername(String username);

}