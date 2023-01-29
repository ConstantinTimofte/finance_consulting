package com.clients.service;

import com.model.client.ClientDto;
import com.model.client.ClientFeign;
import com.model.clientinvest.ClientInvestmentDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientFeign clientFeign;

    public List<ClientDto> findAllContacts(String filterText, String contactType, String payment) {

        if (filterText == null || filterText.trim().isEmpty()
                && contactType == null && payment == null) {
            /** layout di search vuoto */
            return clientFeign.findAllClients();
        } else {
            return clientFeign.search(filterText,contactType,payment);
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

    public void saveInvestment(ClientInvestmentDto clientInvestmentDto) throws Exception {
        if (clientInvestmentDto == null ||
                (clientInvestmentDto.getInvestmentName() == null || clientInvestmentDto.getInvestmentName().isEmpty())) {
            throw new Exception();
        } else {
            clientFeign.saveInvestment(clientInvestmentDto);
        }
    }

    /**
     * Proponi investimenti che il cliente non ha ancora fatto
     */
    public List<String> getPossibleInvestments(String firstName, String secondName) {
        return clientFeign.allInvestments(firstName, secondName);
    }

}
