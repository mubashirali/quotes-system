package com.quotes.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentResponseDTO implements Serializable {
    private static final long serialVersionUID = 7142068097455992974L;
    private String isin;
    private float price;
}
