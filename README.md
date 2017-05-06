## cryptoalerts
### Alerting and Reporting on Crypto Currencies @ Poloniex

Cryptoalerts is a fully configurable Slack bot with the purpose of serving a Slack team alerts and reports about crypto currency prices and developments at Poloniex.com

Its goal is to support crypto-currency-enthusiastic investors and traders to make decisions about selling and buying and keeping track of their wins and losses.


### Features

It contains features such as:

- Alerting configured course changes within a time frame of currency combinations (e.g. ETH in BTC) in the currencies' channel (eg. #ethereum)
- Half hourly & daily statistics on all tracked currency combinations
- Maintaining Poloniex API credentials in relation to Slack team members
- Hourly & daily reports on their portfolio (sum of all in BTC, currencies available and for sale)
- Proposals for channels to join because there are amounts of a currency in portfolio
- Selling & buying a currency by in-channel-message

### Pre-requisites

- Operating a Spring Boot application on the internet
- A slack team with the channels #general, #statistik, #tages-statistik and #admin (closed one, only with admins invited)
- Adding a custom bot as app so that you have an access token of it

