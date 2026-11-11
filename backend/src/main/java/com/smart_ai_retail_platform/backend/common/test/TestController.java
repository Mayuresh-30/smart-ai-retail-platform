package com.smart_ai_retail_platform.backend.common.test;

import com.smart_ai_retail_platform.backend.common.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/not-found")
    public void testNotFound(){
        throw new ResourceNotFoundException("REsourse not found Exeption Test");
    }
}
