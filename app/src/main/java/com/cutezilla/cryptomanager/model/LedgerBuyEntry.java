package com.cutezilla.cryptomanager.model;

public class LedgerBuyEntry {
    private String ledger_id, buyingDate,currency;
    private float investedAmount,buyingPrice;

    public LedgerBuyEntry() {
    }

    public LedgerBuyEntry(String ledger_id, String buyingDate, float buyingPrice, String currency, float investedAmount) {
        this.ledger_id = ledger_id;
        this.buyingDate = buyingDate;
        this.buyingPrice = buyingPrice;
        this.currency = currency;
        this.investedAmount = investedAmount;
    }

    public String getLedger_id() {
        return ledger_id;
    }

    public void setLedger_id(String ledger_id) {
        this.ledger_id = ledger_id;
    }

    public String getBuyingDate() {
        return buyingDate;
    }

    public void setBuyingDate(String buyingDate) {
        this.buyingDate = buyingDate;
    }

    public float getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(float buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getInvestedAmount() {
        return investedAmount;
    }

    public void setInvestedAmount(float investedAmount) {
        this.investedAmount = investedAmount;
    }
}
