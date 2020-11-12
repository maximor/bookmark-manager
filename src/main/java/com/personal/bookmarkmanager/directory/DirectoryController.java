package com.personal.bookmarkmanager.directory;

import com.personal.bookmarkmanager.DAO.ErrorMessageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/bmm/v1")
public class DirectoryController {
    @Autowired
    private DirectoryRepository directoryRepository;

    @RequestMapping("/directory/{id}/{userId}")
    public ResponseEntity directoryByUser(@PathVariable int id, @PathVariable int userId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(directoryRepository.findByIdAndUserId(id, userId));
    }

    @RequestMapping("/directories/{userId}")
    public ResponseEntity directoriesByUser(@PathVariable int userId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(directoryRepository.findAllByUserId(userId));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/directory")
    public ResponseEntity createDirectory(@RequestBody Directory directory){
        if(directory.getName().isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [name] cannot be empty",
                            "/bmm/v1/directory"
                    ));
        }else if(directory.getUser() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [object user] cannot be empty",
                            "/bmm/v1/directory"
                    ));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(directoryRepository.save(directory));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/directory/{id}")
    public ResponseEntity updateDirectory(@PathVariable int id, @RequestBody Directory directory){
        Directory directoryExists = directoryRepository.findById(id);
        if(directoryExists != null){
            if(!directoryExists.getName().equals(directory.getName())){
                directoryExists.setName(directory.getName());
            }else if(!directoryExists.getDescription().equals(directory.getDescription())){
                directoryExists.setDescription(directory.getDescription());
            }

            return ResponseEntity.status(HttpStatus.OK).body(directoryRepository.save(directoryExists));
        }else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessageDAO(
                            404,
                            new Date(),
                            "Not Found",
                            "Error, directory with id {" + id + "} not found",
                            "bmm/v1/directory/"+id
                    ));
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/directory/{id}")
    public ResponseEntity deleteDirectory(@PathVariable int id){
        Directory directory = directoryRepository.findById(id);
        if(directory != null){
            directoryRepository.delete(directory);
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
