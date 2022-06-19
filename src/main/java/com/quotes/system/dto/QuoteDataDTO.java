package com.quotes.system.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class QuoteDataDTO implements Serializable {
    private static final long serialVersionUID = 7110077186927597124L;

    private float price;
    private String isin;
}
