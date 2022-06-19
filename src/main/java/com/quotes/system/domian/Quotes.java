package com.quotes.system.domian;

import com.quotes.system.domian.type.QuoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static com.quotes.system.domian.type.QuoteType.QUOTE;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quote", schema = "quote_history")
public class Quotes implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private long id;

    private float price;

    private String isin;

    @Enumerated(EnumType.STRING)
    private QuoteType type = QUOTE;

    @CreationTimestamp
    @Column(name = "created_on")
    private LocalDateTime createdOn;
}
