package com.model.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "business", path = "/api/client")
public interface ClientFeign {

    @GetMapping("/all")
    List<ClientDto> findAllClients();

    @GetMapping("/search/{searchTerm}")
    List<ClientDto> search(@PathVariable("searchTerm") String searchTerm);

    @PostMapping("/save")
    void save(@RequestBody ClientDto clientDto);

    @DeleteMapping("/delete")
    void delete(@RequestBody ClientDto clientDto);
}
