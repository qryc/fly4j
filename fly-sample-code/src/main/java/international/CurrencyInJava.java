package international;

import java.util.Currency;
import java.util.Locale;

public class CurrencyInJava {
    public static void main(String[] args) {
        displayCurrencySymbols();
    }
    public static void displayCurrencySymbols() {

        Currency currency = Currency.getInstance(Locale.US);
        System.out.println("United States: " + currency.getSymbol());

        currency = Currency.getInstance(Locale.UK);
        System.out.println("United Kingdom: " + currency.getSymbol());

        currency = Currency.getInstance(Locale.FRANCE);
        System.out.println("France: " + currency.toString());

        System.out.println("th: " + Currency.getInstance(new Locale("th", "TH")).toString());
        System.out.println("zn: " + Currency.getInstance(new Locale("zh", "CN")).toString());

        System.out.println("china: " + Currency.getInstance(Locale.CHINA).toString());
        System.out.println("ID: " + Currency.getInstance(new Locale("in", "ID")).toString());

        System.out.println("CNY: " + Currency.getInstance("CNY").getSymbol());

    }
}
