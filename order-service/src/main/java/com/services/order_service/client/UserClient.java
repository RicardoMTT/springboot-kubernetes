package com.services.order_service.client;


import com.services.order_service.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "user-service", url = "${user.service.url}")
@FeignClient(name = "user-service", url = "http://user-service:8081")
public interface UserClient{

    @GetMapping("/api/users/{id}")
    User getUserById(@PathVariable Long id);

}
