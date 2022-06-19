package com.quotes.system.repository;

import com.quotes.system.domian.Instruments;
import com.quotes.system.domian.type.InstrumentType;
import com.quotes.system.dto.InstrumentResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentRepository extends JpaRepository<Instruments, String> {

    @Query("SELECT new com.quotes.system.dto.InstrumentResponseDTO(inst.isin, quo.price) "
            + "FROM Instruments inst INNER JOIN Quotes quo "
            + "ON inst.isin = quo.isin WHERE inst.type = :type AND quo.createdOn = "
            + "(SELECT MAX (q.createdOn) FROM Quotes q WHERE inst.isin = q.isin) " + "order by inst.createdOn")
    List<InstrumentResponseDTO> findAllByType(@Param("type") InstrumentType type);

    @Query(value = "SELECT * FROM QUOTE_HISTORY.OLD_INSTRUMENT old "
            + "WHERE (SELECT * FROM QUOTE_HISTORY.NEW_INSTRUMENT new "
            + "WHERE new.price >= (old.price + old.price * 0.01) AND old.isin = new.isin)", nativeQuery = true)
    List<InstrumentResponseDTO> findAllInterestingInstruments();

    @Query(value = "SELECT * FROM QUOTE_HISTORY.OLD_INSTRUMENT old "
            + "WHERE (SELECT * FROM QUOTE_HISTORY.NEW_INSTRUMENT new "
            + "WHERE new.price <= (old.price + old.price * 0.01) AND old.isin = new.isin)", nativeQuery = true)
    List<InstrumentResponseDTO> findAllReduceInstruments();
}
