package com.personal.bookmarkmanager.note;

import com.personal.bookmarkmanager.DAO.ErrorMessageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/bmm/v1")
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;

    @RequestMapping("/note/{bookmark}/{user}")
    public ResponseEntity noteByBookmarkUser(@PathVariable int bookmark, @PathVariable int user){
         return ResponseEntity
                 .status(HttpStatus.OK)
                 .body(noteRepository.findByBookmarkAndUserId(bookmark, user));

    }

    @RequestMapping("/notes/{bookmark}/{user}")
    public ResponseEntity notesByBookmarkUser(@PathVariable int bookmark, @PathVariable int user){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(noteRepository.findAllByBookmarkAndUserId(bookmark, user));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/note" )
    public ResponseEntity createNote(@RequestBody Note note){
        if(note.getName() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [name] cannot be empty",
                            "/bmm/v1/note"
                    ));
        }else if(note.getNote() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [note] cannot be empty",
                            "/bmm/v1/note"
                    ));
        }else if(note.getUser() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [user] cannot be empty",
                            "/bmm/v1/note"
                    ));
        } else if(note.getBookmark() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [bookmark] cannot be empty",
                            "/bmm/v1/note"
                    ));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(noteRepository.save(note));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/note/{id}" )
    public ResponseEntity updateNote(@PathVariable int id, @RequestBody Note note){
        Note noteExists = noteRepository.findById(id);
        if(noteExists != null){
            if(!noteExists.getName().equals(note.getName())){
                noteExists.setName(note.getName());
            }else if(!noteExists.getNote().equals(note.getNote())){
                noteExists.setNote(note.getNote());
            }

            return ResponseEntity.status(HttpStatus.OK).body(noteRepository.save(noteExists));
        }else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessageDAO(
                            404,
                            new Date(),
                            "Not Found",
                            "Error, note with id {" + id + "} not found",
                            "bmm/v1/note/"+id
                    ));
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/note/{id}")
    public ResponseEntity deleteNote(@PathVariable int id){
        Note note = noteRepository.findById(id);
        if(note != null){
            noteRepository.delete(note);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessageDAO(
                        404,
                        new Date(),
                        "Not Found",
                        "Error, note with id {"+id+"} not found",
                        "/bmm/v1/note/"+id
                ));
    }
}
