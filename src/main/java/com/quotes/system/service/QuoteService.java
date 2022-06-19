package com.quotes.system.service;

import com.quotes.system.domian.Quotes;
import com.quotes.system.domian.type.QuoteType;
import com.quotes.system.dto.CandlestickDTO;
import com.quotes.system.dto.QuoteDTO;
import com.quotes.system.queue.DataStreamQueue;
import com.quotes.system.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuoteService {

    private final DataStreamQueue queue;
    private final QuoteRepository repository;

    public Flux<ServerSentEvent<CandlestickDTO>> getCandlestickData(String isin) {
        return Flux.interval(Duration.ofMinutes(1)).map(
                stocks -> ServerSentEvent.<CandlestickDTO> builder().data(repository.getCandlestickData(isin)).build());

    }

    @Async
    public void insertQuote() {
        while (true) {
            try {
                final QuoteDTO dto = queue.pollQuoteDTO();
                if (dto != null) {
                    Quotes quotes = toQuote(dto);
                    repository.save(quotes);
                }
            } catch (Exception ex) {
                log.error("Exception occurred while processing Quotes {}", ex.getMessage());
            }
        }
    }

    private Quotes toQuote(QuoteDTO dto) {
        return Quotes.builder().isin(dto.getData().getIsin()).price(dto.getData().getPrice())
                .type(QuoteType.valueOf(dto.getType())).build();
    }

}
