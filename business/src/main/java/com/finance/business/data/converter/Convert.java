package com.finance.business.data.converter;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Convert {

    private final ModelMapper modelMapper;



/*    public ClientDto convertClient(Client client) {
        String description = "";
        if (client.getStatus() != null) {
            description = client.getStatus().equals('P') ? "Payment successful" : "Waiting for payment";
        }
        ClientDto clientDto = modelMapper.map(client, ClientDto.class);
        clientDto.setStatusDescription(description);
        return clientDto;
    }*/
}
