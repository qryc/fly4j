package international;

import java.util.Locale;

public class LocaleInJava {
    public static void main(String[] args) {
        Locale localeCN = new Locale("zh", "CN");
        Locale localeUS = Locale.US;
        Locale localeDe = Locale.getDefault();
        System.out.println(localeCN);
        System.out.println(localeUS);
        System.out.println(localeDe);

        Locale[] locales = Locale.getAvailableLocales();
        for( Locale locale : locales ) {
            System.out.print(locale.getDisplayCountry() + "=" + locale.getCountry());
            System.out.println("\t\t\t\t\t\t\t" + locale.getDisplayLanguage() + "=" + locale.getLanguage());

        }

    }
}
