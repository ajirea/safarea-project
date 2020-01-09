package com.perjalanan.safarea.helpers;

import java.text.NumberFormat;
import java.util.Locale;

public abstract class FormatHelper {

    private static Locale getLocale() {
        return new Locale("id", "ID");
    }

    public static String priceFormat(Double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(getLocale());
        return formatter.format(price);
    }

}
