package com.cutezilla.cryptomanager.model;

public class Currency {
    private String currency_id, name;
    private boolean isDefaultCurrency;

    public Currency() {
    }

    public Currency(String currency_id, String name, boolean isDefaultCurrency) {
        this.currency_id = currency_id;
        this.name = name;
        this.isDefaultCurrency = isDefaultCurrency;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefaultCurrency() {
        return isDefaultCurrency;
    }

    public void setDefaultCurrency(boolean defaultCurrency) {
        isDefaultCurrency = defaultCurrency;
    }
}
