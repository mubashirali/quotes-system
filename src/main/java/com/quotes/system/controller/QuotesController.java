package com.quotes.system.controller;

import com.quotes.system.dto.CandlestickDTO;
import com.quotes.system.dto.InstrumentResponseDTO;
import com.quotes.system.service.InstrumentService;
import com.quotes.system.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/quote")
@RequiredArgsConstructor
public class QuotesController {

    private final InstrumentService instrumentService;
    private final QuoteService quoteService;

    @GetMapping("/instruments")
    public List<InstrumentResponseDTO> getAll() {
        return instrumentService.getAllActiveInstruments();
    }

    @GetMapping(value = "/candlestick/{isin}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<CandlestickDTO>> getCandlestick(@PathVariable final String isin) {
        return quoteService.getCandlestickData(isin);
    }

    @GetMapping(value = "/price/increased", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Collection<InstrumentResponseDTO>>> getInterestingInstruments() {
        return instrumentService.getInterestingInstrumentStream();
    }

    @GetMapping(value = "/price/reduced", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Collection<InstrumentResponseDTO>>> getReducedInstruments() {
        return instrumentService.getReducedInstrumentStream();
    }
}
