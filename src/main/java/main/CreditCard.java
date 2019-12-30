package main;

import lombok.Getter;

@Getter
// this class is used as a part of a message, therefore must be immutable
public class CreditCard {

    public CreditCard(String name, String number, double balance) {
        this.name = name;
        this.CCnumber = number;
        this.balance = balance;
    }

    private final String name;
    private final String CCnumber;
    private final double balance;

    // get array of n ints from CCnumber
    public int[] getArrayOf(int n) {
        String subString = this.CCnumber.substring(0, n);
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = subString.charAt(i) - '0';
        }
        return array;
    }
}
