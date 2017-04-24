package de.jverhoelen.currency;

public enum ExchangeCurrency {
    USDT("$"),
    BTC("BTC");

    private String fullName;

    ExchangeCurrency(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
