package com.example.todosecurity.controller;


import  com.example.todosecurity.dto.BaseResponseDto;
import com.example.todosecurity.model.User;
import com.example.todosecurity.service.UserService;
import com.example.todosecurity.dto.UserLoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {


    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public BaseResponseDto registerUser(@RequestBody User user){
        System.out.println("User register : "+user.getUsername());

        if(userService.createUser(user)){
            return new BaseResponseDto("Success");
        }else{
            return new BaseResponseDto("Failed");
        }

    }

    @PostMapping("/login")
    public BaseResponseDto loginUser(@RequestBody UserLoginDto loginDto) {
        String dto = loginDto.toString();
        System.out.println("DTO: " + dto);

        String identifier = loginDto.getUsernameOrEmail();
        System.out.println("Identifier: " + identifier);

        if (userService.checkUserExists(identifier)) {
            System.out.println("User exists: " + identifier);
            if (userService.verifyUser(identifier, loginDto.getPassword())) {
                Map<String, String> data = new HashMap<>();
                data.put("accessToken", userService.generateToken(identifier, loginDto.getPassword()));
                System.out.println("Data: " + data);
                return new BaseResponseDto("Success", data);
            } else {
                return new BaseResponseDto("Wrong password");
            }
        } else {
            return new BaseResponseDto("User does not exist");
        }
    }
}
