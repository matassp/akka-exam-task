package main;

import lombok.Data;

import static java.lang.String.valueOf;

@Data
public class CreditCard {

    public CreditCard(String name, String number, double balance) {
        Name = name;
        CCNumber = number;
        Balance = balance;
        UselessValue = getNumericString();
    }

    private String Name;
    private String CCNumber;
    private double Balance;
    private String UselessValue;

    public void uselessWork() throws InterruptedException {
        Thread.sleep(100);
        String UselessValue = getNumericString();
        System.out.println(Name + " done");
    }

    private String getNumericString() {
        StringBuilder str = new StringBuilder();
        for(char c : Name.toCharArray()) {
            str.append(Character.getNumericValue(c));
        }
        str.append(CCNumber);
        String balance = valueOf(Balance).replace(".", "");
        str.append(balance);
        return str.toString();
    }

}
