package com.finance.business.controller;

import com.finance.business.data.entity.Client;
import com.finance.business.data.repository.ClientRepository;
import com.model.client.ClientDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public void save(ClientDto clientDto) {
        Client client = new Client();
        modelMapper.map(clientDto, client);
        clientRepository.save(client);
    }

}
