package com.relive.controller;

import com.relive.dto.RoleInfoDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: ReLive
 * @date: 2021/12/8 5:02 下午
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @GetMapping("/list")
    public List<RoleInfoDTO> roleList(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return null;
    }

    @PostMapping
    public void saveRole() {

    }

    @PutMapping
    public void updateRole() {

    }

    @DeleteMapping
    public void deleteRole() {

    }
}
