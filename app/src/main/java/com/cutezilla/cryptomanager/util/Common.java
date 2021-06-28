package com.cutezilla.cryptomanager.util;

import android.util.Patterns;

import com.cutezilla.cryptomanager.model.Account;

public class Common {

    public static Account userAccount;
    public static final String LOGTRACE = "logtrace";
    public static final String STR_EMAIL = "email";
    public static final String STR_Account = "accounts";
    public static final String STR_Currency = "currencys";
    public static final String STR_LedgerBuy = "ledgerBuyEntry";
    public static final String STR_Ledger = "ledger";
    public static String email;




    public static String Validation(String username, String email, String password) {

        if (username.isEmpty()) {
            return "required username";
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "vaildate email";
        }

        if (email.isEmpty()) {
            return "required email";
        }

        if (password.isEmpty()) {
            return "required password";
        }

        if (password.length() < 6) {
            return "min password";
        } else {
            return "vaildated";
        }
    }

    public static String removeSpecialCharacter(String str){
        String newStr=null;
        newStr = str.replaceAll("[^a-zA-Z0-9]", "");

        return newStr;
    }

    public static String createLedgerEntryId(String currency){
        String id = removeSpecialCharacter(userAccount.getEmail()+"ledgerEntryId"+currency);
                return id;
    }

    public static String createLedgerId(String currency){
        String id = removeSpecialCharacter(userAccount.getEmail()+"ledgerId"+currency);
        return id;
    }

}
