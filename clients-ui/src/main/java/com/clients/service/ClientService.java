package com.clients.service;

import com.model.client.ClientDto;
import com.model.client.ClientFeign;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientFeign clientFeign;

    public List<ClientDto> findAllContacts(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return clientFeign.findAllClients();
        } else {
            return clientFeign.search(filterText);
        }
    }

    public void saveClient(ClientDto clientDto) {
        if (clientDto == null) {
            System.err.println("Contact is null");
        } else {
            clientFeign.save(clientDto);
        }
    }

    public void deleteContact(ClientDto clientDto) {
        clientFeign.delete(clientDto);
    }
}
