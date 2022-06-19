package com.quotes.system.queue;

import com.quotes.system.dto.InstrumentDTO;
import com.quotes.system.dto.QuoteDTO;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class DataStreamQueue {

    private final BlockingQueue<InstrumentDTO> instrumentData;
    private final BlockingQueue<QuoteDTO> quoteData;

    public DataStreamQueue() {
        instrumentData = new LinkedBlockingQueue<>();
        quoteData = new LinkedBlockingQueue<>();
    }

    public void putInstrument(InstrumentDTO dto) throws InterruptedException {
        instrumentData.put(dto);
    }

    public void putQuoteDTO(QuoteDTO dto) throws InterruptedException {
        quoteData.put(dto);
    }

    public InstrumentDTO pollInstrumentData() {
        return instrumentData.poll();
    }

    public QuoteDTO pollQuoteDTO() {
        return quoteData.poll();
    }
}
