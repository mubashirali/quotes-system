package com.quotes.system.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class InstrumentDataDTO implements Serializable {
    private static final long serialVersionUID = 8176362101536261257L;

    private String description;
    private String isin;
}
