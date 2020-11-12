package com.personal.bookmarkmanager.directory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectoryRepository extends JpaRepository<Directory, Integer> {
    Directory findById(int id);
    Directory findByIdAndUserId(int id, int user);
    List<Directory> findAllByUserId(int userId);
}
