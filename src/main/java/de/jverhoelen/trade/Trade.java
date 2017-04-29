package de.jverhoelen.trade;

import com.google.common.base.MoreObjects;

public class Trade {

    private long globalTradeID;
    private long tradeID;
    private String date;
    private double rate;
    private double amount;
    private double total;
    private double fee;
    private String orderNumber;
    private TradeType type;
    private String category;

    public long getGlobalTradeID() {
        return globalTradeID;
    }

    public void setGlobalTradeID(long globalTradeID) {
        this.globalTradeID = globalTradeID;
    }

    public long getTradeID() {
        return tradeID;
    }

    public void setTradeID(long tradeID) {
        this.tradeID = tradeID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public TradeType getType() {
        return type;
    }

    public void setType(TradeType type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("globalTradeID", globalTradeID)
                .add("tradeID", tradeID)
                .add("date", date)
                .add("rate", rate)
                .add("amount", amount)
                .add("total", total)
                .add("fee", fee)
                .add("orderNumber", orderNumber)
                .add("type", type)
                .add("category", category)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trade trade = (Trade) o;

        return globalTradeID == trade.globalTradeID;
    }

    @Override
    public int hashCode() {
        return (int) (globalTradeID ^ (globalTradeID >>> 32));
    }
}
