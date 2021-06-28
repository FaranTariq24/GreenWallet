package com.cutezilla.cryptomanager.model;

public class Ledger {
    private String ledger_id,ledgerEntry_id, currency_name,time, description ;
    private float highestBuyingPrice,lowestBuyingPrice,totalInvested;

    public Ledger() {
    }

    public Ledger(String ledger_id, String ledgerEntry_id, String currency_name, String time, String description, float highestBuyingPrice, float lowestBuyingPrice, float totalInvested) {
        this.ledger_id = ledger_id;
        this.ledgerEntry_id = ledgerEntry_id;
        this.currency_name = currency_name;
        this.time = time;
        this.description = description;
        this.highestBuyingPrice = highestBuyingPrice;
        this.lowestBuyingPrice = lowestBuyingPrice;
        this.totalInvested = totalInvested;
    }

    public String getLedger_id() {
        return ledger_id;
    }

    public void setLedger_id(String ledger_id) {
        this.ledger_id = ledger_id;
    }

    public String getLedgerEntry_id() {
        return ledgerEntry_id;
    }

    public void setLedgerEntry_id(String ledgerEntry_id) {
        this.ledgerEntry_id = ledgerEntry_id;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getHighestBuyingPrice() {
        return highestBuyingPrice;
    }

    public void setHighestBuyingPrice(float highestBuyingPrice) {
        this.highestBuyingPrice = highestBuyingPrice;
    }

    public float getLowestBuyingPrice() {
        return lowestBuyingPrice;
    }

    public void setLowestBuyingPrice(float lowestBuyingPrice) {
        this.lowestBuyingPrice = lowestBuyingPrice;
    }

    public float getTotalInvested() {
        return totalInvested;
    }

    public void setTotalInvested(float totalInvested) {
        this.totalInvested = totalInvested;
    }
}
