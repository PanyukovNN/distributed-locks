package org.panyukovnn.clientmanager.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.panyukovnn.clientmanager.model.Client;
import org.panyukovnn.clientmanager.service.ClientService;
import org.panyukovnn.clientmanager.service.LockManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class CMController {

    private final LockManager lockManager;
    private final ClientService clientService;

    @PostMapping("/")
    public Client postProcess(@RequestBody Client client) {
        return lockManager.lockSupplier(
                client.getUsername(),
                () -> clientService.checkAndSave(client.getUsername())
        );
    }

    @GetMapping("/count")
    public Long count() {
        return clientService.count();
    }

    @DeleteMapping("/all")
    public void deleteAll() {
        clientService.deleteAll();
    }

}
