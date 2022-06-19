package com.quotes.system.dto;

import java.time.LocalDateTime;

public interface CandlestickDTO {

    LocalDateTime getOpenTimestamp();

    float getOpenPrice();

    float getHighPrice();

    float getLowPrice();

    float getClosePrice();

    LocalDateTime getCloseTimestamp();
}
