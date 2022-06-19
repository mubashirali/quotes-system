-- db schema
CREATE SCHEMA quote_history;

CREATE TABLE IF NOT EXISTS quote_history.instrument
(
    isin        VARCHAR(12) NOT NULL PRIMARY KEY,
    description VARCHAR(150),
    type        VARCHAR(10) NOT NULL,
    created_on  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS quote_history.quote
(
    id         BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    price      BIGINT,
    isin       VARCHAR(12),
    type       VARCHAR(10),
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE OR REPLACE VIEW QUOTE_HISTORY.NEW_INSTRUMENT
AS
(
SELECT inst.isin as isin, MAX(quo.price) as price
FROM QUOTE_HISTORY.INSTRUMENT inst
         inner join QUOTE_HISTORY.QUOTE quo on inst.isin = quo.isin
WHERE inst.type = 'ADD'
  AND quo.created_on >= NOW() - INTERVAL 5 MINUTE
Group by inst.isin, quo.isin);

CREATE OR REPLACE VIEW QUOTE_HISTORY.OLD_INSTRUMENT
AS
(
SELECT inst.isin as isin, MIN(quo.price) as price
FROM QUOTE_HISTORY.INSTRUMENT inst
         inner join QUOTE_HISTORY.QUOTE quo on inst.isin = quo.isin
WHERE inst.type = 'ADD'
  AND quo.created_on <= NOW() - INTERVAL 5 MINUTE
  AND quo.created_on >= NOW() - INTERVAL 10 MINUTE
Group by inst.isin, quo.isin);
