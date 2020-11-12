package com.personal.bookmarkmanager.tag;

import com.personal.bookmarkmanager.DAO.ErrorMessageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bmm/v1")
public class TagController {
    @Autowired
    private TagRepository tagRepository;

    @RequestMapping("/tag/{name}")
    public ResponseEntity tag(@PathVariable String name){
        Tag tag = tagRepository.findByName(name);

        if(tag == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessageDAO(
                            404,
                            new Date(),
                            "Not Found",
                            "Error, Tag " + name + " Not Found",
                            "/bmm/v1/tag/"+name));
        }

        return ResponseEntity.status(HttpStatus.OK).body(tag);
    }

    @RequestMapping("/tag/{name}/{user}")
    public ResponseEntity tagByUser(@PathVariable String name, @PathVariable int user){
        Tag tag = tagRepository.findByNameAndUserId(name, user);

        if(tag == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessageDAO(
                            404,
                            new Date(),
                            "Not Found",
                            "Error, Tag " + name + " Not Found",
                            "/bmm/v1/tag/"+name+"/"+user));
        }

        return ResponseEntity.status(HttpStatus.OK).body(tag);
    }

    @RequestMapping("/tags")
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> tags(){
        return tagRepository.findAll();
    }

    @RequestMapping("/tags/{user}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity tagsByUser(@PathVariable int user){
        List<Tag> tags = tagRepository.findTagsByUserId(user);
        if(user == 0d){
            return  ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [user] cannot be empty",
                            "/bmm/v1/tag/"+user
                    ));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tags);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tag")
    public ResponseEntity createTag(@RequestBody Tag tag){
        if(tag.getName() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [name] cannot be empty",
                            "/bmm/v1/tag"));
        }else if(tag.getUser() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [user] cannot be empty",
                            "bmm/v1/tag"
                    ));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(tagRepository.save(tag));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/tag/{id}")
    public ResponseEntity updateTag(@PathVariable int id, @RequestBody Tag tag){
        Tag tagExists = tagRepository.findById(id);
        if(tagExists != null){
            if(!tagExists.getName().equals(tag.getName())){
                tagExists.setName(tag.getName());
            }

            return ResponseEntity.status(HttpStatus.OK).body(tagRepository.save(tagExists));
        }else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessageDAO(
                            404,
                            new Date(),
                            "Not Found",
                            "Error, tag with id {" + id + "} not found",
                            "bmm/v1/tag/"+id
                    ));
        }


    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/tag/{id}")
    public ResponseEntity deleteTag(@PathVariable int id){
        Tag tag = tagRepository.findById(id);
        if(tag != null){
            tagRepository.delete(tag);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        }else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorMessageDAO(
                            404,
                            new Date(),
                            "Not Found",
                            "Error, tag with id {"+id+"} not found",
                            "/bmm/v1/tag/"+id
                    ));
        }
    }

}
