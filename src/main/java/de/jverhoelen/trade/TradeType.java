package de.jverhoelen.trade;

public enum TradeType {

    buy("Käufe", "gekauft"),
    sell("Verkäufe", "verkauft");

    private String nounPlural;
    private String verbPast;

    TradeType(String nounPlural, String verbPast) {
        this.nounPlural = nounPlural;
        this.verbPast = verbPast;
    }

    public String getNounPlural() {
        return nounPlural;
    }

    public String getVerbPast() {
        return verbPast;
    }
}
