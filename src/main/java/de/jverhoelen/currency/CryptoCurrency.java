package de.jverhoelen.currency;

public enum CryptoCurrency {
    LTC("Litecoin"),
    DCR("Decred"),
    BTC("Bitcoin"),
    ETC("EthereumClassic"),
    ETH("Ethereum"),
    XRP("Ripple"),
    DASH("Dash"),
    XMR("Monero");

    private String fullName;

    CryptoCurrency(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
