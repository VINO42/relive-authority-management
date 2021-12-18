package com.relive.controller;

import com.relive.dto.UserInfoDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: ReLive
 * @date: 2021/12/8 4:58 下午
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/list")
    public List<UserInfoDTO> userList(@RequestParam Integer pageNum, @RequestParam Integer pageSize){
        return null;
    }

    @PostMapping
    public void saveUser(){

    }

    @PutMapping
    public void updateUser(){}

    @DeleteMapping
    public void deleteUser(){}
}
