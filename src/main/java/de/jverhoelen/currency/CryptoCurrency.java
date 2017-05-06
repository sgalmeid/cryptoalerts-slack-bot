package de.jverhoelen.currency;

public enum CryptoCurrency {
    LTC("Litecoin"),
    DCR("Decred"),
    BTC("Bitcoin"),
    ETC("EthereumClassic"),
    ETH("Ethereum"),
    XRP("Ripple"),
    DASH("Dash"),
    XMR("Monero"),
    GAME("GameCredits");

    private String fullName;

    CryptoCurrency(String fullName) {
        this.fullName = fullName;
    }

    public static CryptoCurrency byName(String name) {
        for (CryptoCurrency currency : values()) {
            if (currency.getFullName().toLowerCase().equals(name.toLowerCase())) {
                return currency;
            }
        }

        return null;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
