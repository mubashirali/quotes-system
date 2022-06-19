package com.quotes.system.domian;

import com.quotes.system.domian.type.InstrumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import static com.quotes.system.domian.type.InstrumentType.ADD;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "instrument", schema = "quote_history")
public class Instruments implements Serializable {

    @Id
    private String isin;

    private String description;

    @Enumerated(EnumType.STRING)
    private InstrumentType type = ADD;

    @OneToMany(mappedBy = "isin", fetch = FetchType.LAZY)
    private Set<Quotes> quotes;

    @CreationTimestamp
    @Column(name = "created_on")
    private LocalDateTime createdOn;
}
