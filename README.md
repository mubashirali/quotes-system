## Content

```
├── quotes-system
    Port: 8081
    To start use: mvn spring-boot:run

```
## Endpoints 
* Instrument-Price retrieval 
    * GET: https://localhost:8081/quote/instruments: Returns all available instruments (Type = ADD) 

* Hot Instrument stream
    * GET: https://localhost:8081/candlestick/{isin}: Returns Stream of candlestick of provided instrument
    
* Aggregated-Price History retrieval
    * GET: https://localhost:8081/price/increased: Returns Stream of all instruments whose price has increased by 10 %
    * GET: https://localhost:8081/price/reduced: Returns Stream of all instruments whose price has decreased by 10 %

## Future Development Discussion
* For large streams we can use Apache Kafka
* To use multiple instances of consumer service we can use Zuul for Server Side LoadBalancing

## Details
* SpringBoot application created using maven build tool
* Flyway for db versioning and H2 Database to store instruments and quotes data
* Spring Reactive WebSockets for consuming streams from provided partnerService JAR
* Spring Webflex for producing streams
* All data produced by partnerService jar are stored in Blocking Queue for decoupling and later consumed by Instrument and Quote service
