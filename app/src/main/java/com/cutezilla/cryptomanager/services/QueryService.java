package com.cutezilla.cryptomanager.services;

import com.cutezilla.cryptomanager.util.Common;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class QueryService {
    private Query query;
    public Query getAccount(String userEmail) {
        query = FirebaseDatabase.getInstance().getReference(Common.STR_Account)
                .orderByChild(Common.STR_EMAIL)
                .equalTo(userEmail);
        return query;
    }

    public Query getDefaultAndUserCurrency(){
        query = FirebaseDatabase.getInstance().getReference(Common.STR_Currency)
                .orderByChild("defaultCurrency")
                .equalTo(true);
        return query;
    }
    public Query getLedgerByLedgerId(String ledgerId){
        query = FirebaseDatabase.getInstance().getReference(Common.STR_Ledger)
                .child(ledgerId);
        return query;
    }
    public  Query getLedgerObjectByLedgerId(String ledgerId){
        query = FirebaseDatabase.getInstance().getReference(Common.STR_Ledger)
                .orderByChild("ledgerEntry_id")
                    .equalTo(ledgerId);
        return query;
    }
}
