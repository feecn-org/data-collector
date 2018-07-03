package com.feecn.org.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author pczhangyu
 * @date 2018/7/3
 */
@RestController(value = "/job")
public class JobController {

    @GetMapping(name = "/do")
    public String fuck(){
        return "ok";
    }
}
