package br.com.fiap.mapper;

import br.com.fiap.exceptions.DadoInvalidoException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DataUtil {

    private static final String DATE_PATTERN     = "yyyy-MM-dd";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private DataUtil() {}

    /** Converte "yyyy-MM-dd" em java.util.Date. Lanca 400 se invalida. */
    public static Date parse(String iso) {
        if (iso == null || iso.isBlank()) {
            throw new DadoInvalidoException("Data obrigatoria no formato yyyy-MM-dd.");
        }
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(DATE_PATTERN);
            fmt.setLenient(false);
            return fmt.parse(iso);
        } catch (Exception e) {
            throw new DadoInvalidoException("Data invalida: '" + iso + "'. Use yyyy-MM-dd.");
        }
    }

    /**
     * Aceita tanto "yyyy-MM-dd" quanto "yyyy-MM-dd'T'HH:mm" ou "yyyy-MM-dd'T'HH:mm:ss".
     * Quando vier só data, considera 00:00:00.
     */
    public static Date parseDateOrDateTime(String iso) {
        if (iso == null || iso.isBlank()) {
            throw new DadoInvalidoException("Data obrigatoria.");
        }
        String[] patterns = { DATE_TIME_PATTERN, "yyyy-MM-dd'T'HH:mm", DATE_PATTERN };
        for (String p : patterns) {
            try {
                SimpleDateFormat fmt = new SimpleDateFormat(p);
                fmt.setLenient(false);
                return fmt.parse(iso);
            } catch (Exception ignored) { /* tenta proximo */ }
        }
        throw new DadoInvalidoException(
                "Data invalida: '" + iso + "'. Use yyyy-MM-dd ou yyyy-MM-dd'T'HH:mm.");
    }

    /** Converte java.util.Date em "yyyy-MM-dd" (ou null). */
    public static String format(Date data) {
        if (data == null) return null;
        return new SimpleDateFormat(DATE_PATTERN).format(data);
    }

    /** Converte java.util.Date em "yyyy-MM-dd'T'HH:mm:ss" (ou null). */
    public static String formatDateTime(Date data) {
        if (data == null) return null;
        return new SimpleDateFormat(DATE_TIME_PATTERN).format(data);
    }
}
