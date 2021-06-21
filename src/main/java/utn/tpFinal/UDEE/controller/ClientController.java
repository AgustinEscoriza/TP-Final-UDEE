package utn.tpFinal.UDEE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utn.tpFinal.UDEE.model.Client;
import utn.tpFinal.UDEE.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @PostMapping
    public void addClient(@RequestBody Client client) {
        clientService.add(client);
    }

    @GetMapping
    public List<Client> getAll(){
        return clientService.getAll();
    }

    @GetMapping("/{id}")
    public Client getClientById(@PathVariable Integer id) {
        return clientService.getByID(id);
    }
}
