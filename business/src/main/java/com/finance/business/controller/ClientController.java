package com.finance.business.controller;

import com.finance.business.data.entity.Client;
import com.finance.business.data.repository.ClientRepository;
import com.finance.business.service.ClientService;
import com.finance.business.service.EmailComponent;
import com.model.client.ClientDto;
import com.model.clientinvest.ClientInvestmentDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("api/client")
public class ClientController {

    private final ModelMapper modelMapper;
    private final ClientRepository clientRepository;
    private final ClientService clientService;
    private final EmailComponent emailComponent;

    /**
     * @see https://www.baeldung.com/java-modelmapper-lists
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<ClientDto> findAllClients() {
        List<Client> clientEntityList = clientRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<ClientDto> clientDtoList = new ArrayList<>();
        clientEntityList.forEach(cliente -> {
            clientService.findAllClients(cliente, clientDtoList);
        });
        return clientDtoList;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<ClientDto> search(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                  @RequestParam(value = "contactType", required = false) String contactType,
                                  @RequestParam(value = "payment", required = false) String payment) {

        return clientService.search(searchTerm, contactType, payment);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(@RequestBody ClientDto clientDto) {
        Client client = new Client();
        clientDto.setClient(clientDto.getClient() == null ? false : clientDto.getClient());
        modelMapper.map(clientDto, client);
        clientRepository.save(client);
    }


    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(@RequestBody ClientDto clientDto) {
        Client client = new Client();
        modelMapper.map(clientDto, client);
        clientRepository.delete(client);
    }

    /**
     * Salva investimento
     */
    @RequestMapping(value = "/invest", method = RequestMethod.POST)
    public void save(@RequestBody ClientInvestmentDto clientInvestmentDto) {
        clientService.savingInvestment(clientInvestmentDto);
    }


    /**
     * Se il cliente ha gia investito : Recupera tutti i possibili investimenti che il cliente non ha ancora fatto
     * in caso contrario recupera tutti i investiemti gia salvati nel database
     */
    @RequestMapping(value = "/investments/{firstname}/{lastname}", method = RequestMethod.GET)
    public List<String> allInvestments(@PathVariable("firstname") String firstName, @PathVariable("lastname") String lastName) {
        return clientService.findClientInvestments(firstName, lastName);
    }

    /**
     * Invia email
     */
    @RequestMapping(value = "/sendEmail/{email}/{text}", method = RequestMethod.GET)
    public String sendEmail(@PathVariable("email") String email, @PathVariable("text") String text) {
        try {
          //  emailComponent.sendEmail(email, "FINANCE CONSULTING APP", text);
            emailComponent.sendEmail(email, "FINANCE CONSULTING APP", text,1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "Success";
    }
}
