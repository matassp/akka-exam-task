package main;

import lombok.Getter;

@Getter
// this class is used as a part of a message, therefore must be immutable
public class CreditCard {

    public CreditCard(String name, String number, double balance) {
        this.name = name;
        this.CCnumber = number;
        this.balance = balance;
        this.uselessValue = "N/A";
    }

    public CreditCard(CreditCard creditCard, String uselessValue) {
        this.name = creditCard.name;
        this.CCnumber = creditCard.CCnumber;
        this.balance = creditCard.balance;
        this.uselessValue = uselessValue;
    }

    private final String name;
    private final String CCnumber;
    private final double balance;
    private final String uselessValue;

    // get array of n ints from cc number
    public int[] getArrayOf(int n) {
        String subString = this.CCnumber.substring(0, n);
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = subString.charAt(i) - '0';
        }
        return array;
    }
}
