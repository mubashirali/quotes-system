package com.quotes.system.service;

import com.quotes.system.domian.Instruments;
import com.quotes.system.domian.type.InstrumentType;
import com.quotes.system.dto.InstrumentDTO;
import com.quotes.system.dto.InstrumentResponseDTO;
import com.quotes.system.queue.DataStreamQueue;
import com.quotes.system.repository.InstrumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

import static com.quotes.system.domian.type.InstrumentType.ADD;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstrumentService {
    private final DataStreamQueue queue;
    private final InstrumentRepository repository;

    public List<InstrumentResponseDTO> getAllActiveInstruments() {
        return repository.findAllByType(ADD);
    }

    public Flux<ServerSentEvent<Collection<InstrumentResponseDTO>>> getInterestingInstrumentStream() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(t -> ServerSentEvent.<Collection<InstrumentResponseDTO>> builder()
                        .data(repository.findAllInterestingInstruments()).build());
    }

    public Flux<ServerSentEvent<Collection<InstrumentResponseDTO>>> getReducedInstrumentStream() {
        return Flux.interval(Duration.ofSeconds(1)).map(t -> ServerSentEvent
                .<Collection<InstrumentResponseDTO>> builder().data(repository.findAllReduceInstruments()).build());
    }

    @Async
    public void insertOrUpdateQuote() {
        while (true) {
            try {
                final InstrumentDTO dto = queue.pollInstrumentData();
                if (dto != null) {
                    Instruments instruments = toInstrument(dto);
                    repository.save(instruments);
                }
            } catch (Exception ex) {
                log.error("Exception occurred while processing Instrument {}", ex.getMessage());
            }
        }
    }

    private Instruments toInstrument(InstrumentDTO dto) {
        return Instruments.builder().isin(dto.getData().getIsin()).description(dto.getData().getDescription())
                .type(InstrumentType.valueOf(dto.getType())).build();
    }

}
