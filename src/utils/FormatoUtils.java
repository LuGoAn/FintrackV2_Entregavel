package utils;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatoUtils {

    private static final DateTimeFormatter PADRAO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatarValorMoeda(double quantia) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return nf.format(quantia);
    }

    public static String formatarDataRegistro(LocalDate data) {
        if (data == null) {
            return "--/--/----";
        }
        return data.format(PADRAO_DATA);
    }
}
