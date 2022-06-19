package com.quotes.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@Builder
public class InstrumentDTO implements Serializable {
    private static final long serialVersionUID = -5398499578775132330L;

    private InstrumentDataDTO data;
    private String type;
}
