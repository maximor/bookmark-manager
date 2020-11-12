package com.personal.bookmarkmanager.user;


import com.personal.bookmarkmanager.DAO.ErrorMessageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
    @RequestMapping("/bmm/v1")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/users")
    public ResponseEntity getAllUsers(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userRepository.findAll());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user")
    public ResponseEntity createUser(@RequestBody User user){
        User userExists;
        if(user.getEmail() != null){
            userExists = userRepository.findByEmail(user.getEmail());
            if(userExists != null){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorMessageDAO(
                                400,
                                new Date(),
                                "Bad Request",
                                "Error, email [" + user.getEmail() + "] already exists!",
                                "/bmm/v1/user"
                        ));
            }
        }

        if(user.getEmail() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [email] cannot be empty",
                            "/bmm/v1/user"
                    ));
        }else if(user.getFirstname() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [firstname] cannot be empty",
                            "/bmm/v1/user"
                    ));
        }else if(user.getLastname() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [lastname] cannot be empty",
                            "/bmm/v1/user"
                    ));
        }else if(user.getPassword() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessageDAO(
                            400,
                            new Date(),
                            "Bad Request",
                            "Error, the field [password] cannot be empty",
                            "/bmm/v1/user"
                    ));
        }



        //TODO HANDLE IF THERE'S A ROLE IN THE CREATION OF THE USER OR NOT

        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user));
    }
}
