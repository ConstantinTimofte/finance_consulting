package com.finance.business.controller;

import com.finance.business.data.entity.Client;
import com.finance.business.data.repository.ClientRepository;
import com.model.client.ClientDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("api/client")
public class ClientController {

    private final ModelMapper modelMapper;
    private final ClientRepository clientRepository;

    //* https://www.baeldung.com/java-modelmapper-lists  *//*
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<ClientDto> findAllClients() {
        List<Client> clientEntityList = clientRepository.findAll();
        return clientEntityList
                .stream()
                .map(client -> modelMapper.map(client, ClientDto.class))
                .collect(Collectors.toList());

    }

    @RequestMapping(value = "/search/{searchTerm}", method = RequestMethod.GET)
    public List<ClientDto> search(@PathVariable("searchTerm") String searchTerm) {
        List<Client> clientEntityList = clientRepository.search(searchTerm);
        return clientEntityList
                .stream()
                .map(client -> modelMapper.map(client, ClientDto.class))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(@RequestBody ClientDto clientDto) {
        Client client = new Client();
        clientDto.setClient(clientDto.getClient() == null ? false : clientDto.getClient());
        modelMapper.map(clientDto, client);
        clientRepository.save(client);
    }

}
