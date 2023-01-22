package org.panyukovnn.distributedlocks.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.panyukovnn.distributedlocks.model.Client;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClintManagerClient {

    private static final String BASE_URL = "http://localhost:8088/client-manager/api/v1/client/";

    private final WebClient webClient;

    @PostConstruct
    public void process() {
        int userCount = 1000;
        AtomicInteger errors = new AtomicInteger();
        List<String> generatedUsernames = generateUsernamesWithDuplicates(userCount);

        clearClients()
                .thenMany(Flux.fromIterable(generatedUsernames))
                .flatMap(username -> save(Client.builder().username(username).build())
                        .onErrorResume(Exception.class, t -> Mono.fromRunnable(errors::incrementAndGet)))
                .then(verifyConsistency(userCount))
                .doFinally(signal -> log.info("Number of failed requests is correct: " + (errors.get() == generatedUsernames.size() - userCount)))
                .subscribe();
    }

    public Mono<Client> save(Client client) {
        return webClient.post()
                .uri(BASE_URL)
                .body(BodyInserters.fromValue(client))
                .retrieve()
                .bodyToMono(Client.class);
    }

    public Mono<Void> verifyConsistency(int userCount) {
        return webClient.get()
                .uri(BASE_URL + "count")
                .retrieve()
                .bodyToMono(Long.class)
                .doOnNext(count -> log.info("Data consistent: " + (count == userCount)))
                .then();
    }

    public Mono<Void> clearClients() {
        return webClient.delete()
                .uri(BASE_URL + "all")
                .retrieve()
                .bodyToMono(Void.class);
    }

    /**
     * Creates list of usernames, where every item repeates several times.
     *
     * @param userCount number of users
     * @return list of usernames
     */
    private List<String> generateUsernamesWithDuplicates(int userCount) {
        int multiplier = 10;

        return IntStream.range(0, userCount).boxed()
                .map(i -> UUID.randomUUID().toString())
                .flatMap(username -> IntStream.range(0, multiplier).boxed()
                        .map(l -> username))
                .toList();
    }
}
