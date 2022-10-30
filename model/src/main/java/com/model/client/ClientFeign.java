package com.model.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "business", path = "/api/client")
public interface ClientFeign {

    @GetMapping("/all")
    List<ClientDto> findAllClients();

    @GetMapping("/search/{searchTerm}")
    List<ClientDto> search(@PathVariable("searchTerm") String searchTerm);

    @PostMapping("/save")
    void save(@RequestBody ClientDto clientDto);
}
