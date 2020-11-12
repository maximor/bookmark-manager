package com.personal.bookmarkmanager.bookmark;

import com.personal.bookmarkmanager.DAO.ErrorMessageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/bmm/v1")
public class BookmarkController {
    @Autowired
    private BookmarkRepository bookmarkRepository;

    @RequestMapping("/bookmark/{id}/{userId}")
    public ResponseEntity bookmarkByUser(@PathVariable int id, @PathVariable int userId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookmarkRepository.findByIdAndUserId(id, userId));
    }

    @RequestMapping("/bookmark/{userId}")
    public ResponseEntity bookmarksByUser(@PathVariable int userId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookmarkRepository.findAllByUserId(userId));
    }

    //TODO FIND A WAY TO HANDLE MULTIPLE TAGNAMES
    @RequestMapping("/bookmarks/{userId}/{tagName}")
    public ResponseEntity bookmarksByUserTag(@PathVariable int userId, @PathVariable String tagName){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookmarkRepository.findAllByUserIdAndTagName(userId, tagName));
    }

    @RequestMapping("/bookmarks/public")
    public ResponseEntity bookmarksPublic(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookmarkRepository.findAllByPrivacyFalse());
    }

    @RequestMapping("/bookmarks/untagged")
    public ResponseEntity bookmarksUntagged(@PathVariable int userId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookmarkRepository.findAllByUserIdAndUntaggedTrue(userId));
    }

    //TODO CREATE AN END-POINT THAT SEARCHES BOOKMARKS BY ANY CONCURRENCY IN THE TITLE OR IN THE DESCRIPTION

    @RequestMapping(method = RequestMethod.POST, value = "/bookmark")
    public ResponseEntity createBookmark(@RequestBody Bookmark bookmark){
        if(bookmark.getTitle() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [title] cannot be empty",
                            "/bmm/v1/bookmark"));
        }else if(bookmark.getDescription() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [description] cannot be empty",
                            "/bmm/v1/bookmark"));
        }else if(bookmark.getUser() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [object user] cannot be empty",
                            "/bmm/v1/bookmark"));
        }else if(bookmark.getUrl() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [url] cannot be empty",
                            "/bmm/v1/bookmark"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(bookmark);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/bookmark/{id}")
    public ResponseEntity updateBookmark(@PathVariable int id, @RequestBody Bookmark bookmark){
        Bookmark bookmarkExists = bookmarkRepository.findById(id);

        if(bookmarkExists != null){
            if(!bookmarkExists.getTitle().equals(bookmark.getTitle())){
                bookmarkExists.setTitle(bookmark.getTitle());
            }else if(!bookmarkExists.getDescription().equals(bookmark.getDescription())){
                bookmarkExists.setDescription(bookmark.getDescription());
            }else if(!bookmarkExists.getUrl().equals(bookmark.getUrl())){
                bookmarkExists.setUrl(bookmark.getUrl());
            }else if(!bookmarkExists.getTag().equals(bookmark.getTag())){
                bookmarkExists.setTag(bookmark.getTag());
            }else if(!bookmarkExists.getDirectory().equals(bookmark.getDirectory())){
                bookmarkExists.setDirectory(bookmark.getDirectory());
            }

            return ResponseEntity.status(HttpStatus.OK).body(bookmarkRepository.save(bookmarkExists));
        }else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessageDAO(
                            404,
                            new Date(),
                            "Not Found",
                            "Error, bookmark with id {" + id + "} not found",
                            "bmm/v1/bookmark/"+id
                    ));
        }

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/bookmark/{id}")
    public ResponseEntity deleteBookmark(@PathVariable int id){
        Bookmark bookmark = bookmarkRepository.findById(id);

        if(bookmark != null){
            bookmarkRepository.delete(bookmark);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        }else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessageDAO(
                            404,
                            new Date(),
                            "Not Found",
                            "Error, bookmark with id {"+id+"} not found",
                            "/bmm/v1/bookmark/"+id
                    ));
        }
    }
}
