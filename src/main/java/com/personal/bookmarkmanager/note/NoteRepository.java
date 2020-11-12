package com.personal.bookmarkmanager.note;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    Note findById(int id);
    Note findByBookmarkAndUserId(int bookmark, int user);
    List<Note> findAllByBookmarkAndUserId(int bookmark, int user);
}
