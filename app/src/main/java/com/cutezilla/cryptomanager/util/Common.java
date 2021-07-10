package com.cutezilla.cryptomanager.util;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import com.cutezilla.cryptomanager.model.Account;
import com.cutezilla.cryptomanager.model.AllCoin;
import com.cutezilla.cryptomanager.model.Ledger;
import com.cutezilla.cryptomanager.model.LedgerEntry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Common {


    public static final String ConiFeckoGETLISTURL = "https://api.coingecko.com/api/v3/coins/list?include_platform=true";
    public static final String ConiFeckoGETVSCURRENCY = "https://api.coingecko.com/api/v3/simple/supported_vs_currencies";

    public static Account userAccount;
    public static final String LOGTRACE = "logtrace";
    public static final String STR_EMAIL = "email";
    public static final String STR_Account = "accounts";
    public static final String STR_Currency = "currencys";
    public static final String STR_LedgerEntry = "ledgerEntry";
    public static final String STR_Ledger = "ledger";
    public static String email;
    public static final String STR_BUY ="BUY";
    public static final String STR_SELL ="SELL";
    public static  String SELECTED_CURRENCY ;

    public static String MAX_VALUE;
    public static Ledger STR_SELECTED_LEDGER_SELL;
    public  static Ledger STR_SELECTED_LEDGER_INT;

    public static List<Ledger> LEDG_LIST = new ArrayList<>();
    public static List<LedgerEntry> LEDG_ENTRY_LIST = new ArrayList<>();
    public static boolean isReversed;
    public static final String STR_NO_DATA = "Record not found";
    public static AllCoin SELECTED_COIN_DES;




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



    public static void writefile(String message, Context context){
        String fileName="symbol.json";
        File file = new File(context.getFilesDir(),fileName);
        if (file!=null){
            FileWriter fileWriter = null;
            BufferedWriter bufferedWriter = null;
            try {
                if (!file.exists()){
                    if (file.createNewFile()){
                    file = new File(context.getFilesDir(),fileName);
                    }else {return;}
                }
                fileWriter = new FileWriter(file.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(message);
                bufferedWriter.close();

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    public static String readFile(Context context){
        String fileName="symbol.json";
        String response = null;
        File file = new File(context.getFilesDir(),fileName);

        if (!file.exists()){
            return null;
        }
        if (file!=null){
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;

            try {
                //read
                StringBuffer output = new StringBuffer();
                fileReader= new FileReader(file.getAbsoluteFile());
                bufferedReader=new BufferedReader(fileReader);
                String line="";
                while ((line=bufferedReader.readLine())!=null){
                    output.append(line+"\n");

                }
                response=output.toString();
                bufferedReader.close();
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }
        return response;
    }

    public static double roundOf(double d){

            DecimalFormat twoDForm = new DecimalFormat("#.##");
            return Double.valueOf(twoDForm.format(d));
    }
    public static boolean isDouble(String number){
        try
        {
            Double.parseDouble(number);
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }

}
