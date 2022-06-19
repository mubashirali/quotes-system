package com.quotes.system.repository;

import com.quotes.system.domian.Quotes;
import com.quotes.system.dto.CandlestickDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends JpaRepository<Quotes, Long> {

    @Query(value =
            "SELECT open.openTimestamp, open.openPrice, highPrice, lowPrice, close.closePrice, close.closeTimestamp "
                    + "FROM (SELECT MIN(quo.price) as lowPrice " + "      FROM QUOTE_HISTORY.QUOTE quo "
                    + "      WHERE quo.isin = :isin "
                    + "        AND quo.created_on >= NOW() - INTERVAL 60 MINUTE) as lowPrice, "
                    + "     (SELECT MAX(quo.price) as highPrice " + "      FROM QUOTE_HISTORY.QUOTE quo "
                    + "      WHERE quo.isin = :isin "
                    + "        AND quo.created_on >= NOW() - INTERVAL 60 MINUTE) as highPrice, "
                    + "     (SELECT TOP(1) quo.price as openPrice, quo.created_on as openTimestamp"
                    + "      FROM QUOTE_HISTORY.QUOTE quo " + "      WHERE quo.isin = :isin "
                    + "        AND quo.created_on >= NOW() - INTERVAL 60 MINUTE " + "      order by created_on) as open, "
                    + "     (SELECT TOP(1) quo.price as closePrice, quo.created_on as closeTimestamp"
                    + "      FROM QUOTE_HISTORY.QUOTE quo " + "      WHERE quo.isin = :isin "
                    + "        AND quo.created_on >= NOW() - INTERVAL 60 MINUTE "
                    + "      order by created_on desc) as close", nativeQuery = true)
    CandlestickDTO getCandlestickData(@Param("isin") String isin);
}
