package main;

import lombok.Data;

import static java.lang.String.valueOf;

@Data
public class CreditCard {

    public CreditCard(String name, String number, double balance) {
        Name = name;
        CCNumber = number;
        Balance = balance;
    }

    public CreditCard(String name, String number, double balance, String uselessValue) {
        Name = name;
        CCNumber = number;
        Balance = balance;
        UselessValue = uselessValue;
    }

    private String Name;
    private String CCNumber;
    private double Balance;
    private String UselessValue;

    public CreditCard uselessWork() throws InterruptedException {
        Thread.sleep(100);
        System.out.println(Name + " done");
        return new CreditCard(this.Name, this.CCNumber, this.Balance, getNumericString());
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
