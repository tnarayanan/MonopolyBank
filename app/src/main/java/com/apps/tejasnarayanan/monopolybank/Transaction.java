package com.apps.tejasnarayanan.monopolybank;

/**
 * Created by Tejas Narayanan on 6/13/17.
 */

public class Transaction {
    String sender;
    String receiver;
    int amount;

    public Transaction(String sender, String receiver, int amount){
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return (sender + " --> " + receiver + ": " + amount);
    }
}
