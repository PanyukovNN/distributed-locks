package org.panyukovnn.clientmanager.service;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.panyukovnn.clientmanager.model.Client;
import org.panyukovnn.clientmanager.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client checkAndSave(String username) {
        if (clientRepository.existsByUsername(username)) {
            throw new EntityExistsException("Client already exists");
        }

        Client client = Client.builder()
                .username(username)
                .build();

        return clientRepository.save(client);
    }

    public long count() {
        return clientRepository.count();
    }

    public void deleteAll() {
        clientRepository.deleteAll();
    }
}
