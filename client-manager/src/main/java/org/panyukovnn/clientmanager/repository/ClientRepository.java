package org.panyukovnn.clientmanager.repository;

import org.panyukovnn.clientmanager.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByUsername(String username);
}
