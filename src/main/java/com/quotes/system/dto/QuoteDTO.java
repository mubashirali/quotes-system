package com.quotes.system.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class QuoteDTO implements Serializable {
    private static final long serialVersionUID = 2255215255884707847L;

    private QuoteDataDTO data;
    private String type;
}
