package com.cutezilla.cryptomanager.model;

public class LedgerEntry {
    private String ledger_id, date, currency,status;
    private float investedAmount, price, cryptoAmount;

    public LedgerEntry() {
    }

    public LedgerEntry(String ledger_id, String date, String currency, float investedAmount, float buyingPrice, float cryptoAmount, String bought) {
        this.ledger_id = ledger_id;
        this.date = date;
        this.currency = currency;
        this.investedAmount = investedAmount;
        this.price = buyingPrice;
        this.cryptoAmount = cryptoAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLedger_id() {
        return ledger_id;
    }

    public void setLedger_id(String ledger_id) {
        this.ledger_id = ledger_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getCryptoAmount() {
        return cryptoAmount;
    }

    public void setCryptoAmount(float cryptoAmount) {
        this.cryptoAmount = cryptoAmount;
    }
}