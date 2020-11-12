package com.personal.bookmarkmanager.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    Bookmark findById(int id);
    Bookmark findByIdAndUserId(int id, int userId);
    List<Bookmark> findAllByUserId(int userId);
    List<Bookmark> findAllByUserIdAndTagName(int userId, String tagName);
    //TODO TEST THIS QUERY TO SEE IF IT IS WORKING CORRECTLY.
    List<Bookmark> findAllByUserIdAndTitleContainingOrDescriptionContaining(int userId, String title, String description);
    List<Bookmark> findAllByUserIdAndUntaggedTrue(int userId);
    List<Bookmark> findAllByPrivacyFalse();
}
