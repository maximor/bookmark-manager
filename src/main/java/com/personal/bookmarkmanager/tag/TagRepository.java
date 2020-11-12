package com.personal.bookmarkmanager.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    Tag findByName(String name);
    Tag findByNameAndUserId(String name, int user);
    Tag findById(int id);
    Tag findByIdAndUserId(int id, int user);
    List<Tag> findTagsByUserId(int user);
}
