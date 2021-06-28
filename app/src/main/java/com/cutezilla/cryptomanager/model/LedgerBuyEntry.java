package com.cutezilla.cryptomanager.model;

public class LedgerBuyEntry {
    private String ledger_id, buyingDate, currency;
    private float investedAmount, buyingPrice, cryptoAmount;

    public LedgerBuyEntry() {
    }

    public LedgerBuyEntry(String ledger_id, String buyingDate, String currency, float investedAmount, float buyingPrice, float cryptoAmount) {
        this.ledger_id = ledger_id;
        this.buyingDate = buyingDate;
        this.currency = currency;
        this.investedAmount = investedAmount;
        this.buyingPrice = buyingPrice;
        this.cryptoAmount = cryptoAmount;
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

    public float getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(float buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public float getCryptoAmount() {
        return cryptoAmount;
    }

    public void setCryptoAmount(float cryptoAmount) {
        this.cryptoAmount = cryptoAmount;
    }
}