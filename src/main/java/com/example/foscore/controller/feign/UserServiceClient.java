package com.example.foscore.controller.feign;

import com.example.foscore.model.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "fos-auth", url = "http://localhost:9000")
public interface UserServiceClient {

    @GetMapping("/api/users")
    List<UserDto> getAllUsers();
}
