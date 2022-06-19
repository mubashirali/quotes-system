package com.quotes.system.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quotes.system.dto.InstrumentDTO;
import com.quotes.system.dto.QuoteDTO;
import com.quotes.system.queue.DataStreamQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketClientListener {

    private final DataStreamQueue queue;
    private final ObjectMapper mapper;

    @Async
    public void executeInstrument() {
        WebSocketClient client = new ReactorNettyWebSocketClient();
        EmitterProcessor<String> output = EmitterProcessor.create();

        final Mono<Void> sessionMono = client.execute(URI.create("ws://localhost:8080/instruments"),
                sersionHandler(output));

        final Flux<String> stringFlux = output.doOnSubscribe(s -> sessionMono.subscribe());

        stringFlux.toStream().map(this::toInstrumentDTO).filter(Optional::isPresent).map(Optional::get)
                .forEach(ints -> {
                    try {
                        queue.putInstrument(ints);
                    } catch (InterruptedException e) {
                        log.error("Unable to process data: {}", e.getMessage());
                    }
                });
    }

    @Async
    public void executeQuotes() {
        WebSocketClient client = new ReactorNettyWebSocketClient();
        EmitterProcessor<String> output = EmitterProcessor.create();

        final Mono<Void> sessionMono = client.execute(URI.create("ws://localhost:8080/quotes"), sersionHandler(output));

        final Flux<String> stringFlux = output.doOnSubscribe(s -> sessionMono.subscribe());

        stringFlux.toStream().map(this::toQuoteDTO).filter(Optional::isPresent).map(Optional::get).forEach(dto -> {
            try {
                queue.putQuoteDTO(dto);
            } catch (InterruptedException e) {
                log.error("Unable to process data: {}", e.getMessage());
            }
        });
    }

    private WebSocketHandler sersionHandler(EmitterProcessor<String> output) {
        return session -> session.send(Mono.just(session.textMessage("ack")))
                .thenMany(session.receive().map(WebSocketMessage::getPayloadAsText).subscribeWith(output).then())
                .then();
    }

    private Optional<InstrumentDTO> toInstrumentDTO(String data) {
        InstrumentDTO instrumentDTO;
        try {
            instrumentDTO = mapper.readValue(data, InstrumentDTO.class);
        } catch (JsonProcessingException e) {
            return empty();
        }

        return of(instrumentDTO);
    }

    private Optional<QuoteDTO> toQuoteDTO(String data) {
        QuoteDTO dto;

        try {
            dto = mapper.readValue(data, QuoteDTO.class);
        } catch (JsonProcessingException e) {
            return empty();
        }
        return of(dto);
    }
}
