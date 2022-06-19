package com.quotes.system;

import com.quotes.system.listener.WebSocketClientListener;
import com.quotes.system.service.InstrumentService;
import com.quotes.system.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
public class QuotesSystemApplication implements ApplicationListener<ApplicationReadyEvent> {

    private final WebSocketClientListener webSocketClientListener;
    private final InstrumentService instrumentService;
    private final QuoteService quoteService;

    public static void main(String[] args) {
        SpringApplication.run(QuotesSystemApplication.class, args);
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        webSocketClientListener.executeInstrument();
        instrumentService.insertOrUpdateQuote();
        webSocketClientListener.executeQuotes();
        quoteService.insertQuote();
    }
}
