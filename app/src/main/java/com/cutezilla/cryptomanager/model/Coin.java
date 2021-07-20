package com.cutezilla.cryptomanager.model;

import java.util.Map;

public class Coin {
    private String id,symbol,name;

    public Coin() {
    }

    public Coin(String id, String symbol, String name, Map<String, String> platforms) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
