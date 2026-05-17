package br.com.fiap.mapper;

import br.com.fiap.exceptions.DadoInvalidoException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DataUtil {

    private DataUtil() {}

    /** Converte "yyyy-MM-dd" em java.util.Date. Lanca 400 se invalida. */
    public static Date parse(String iso) {
        if (iso == null || iso.isBlank()) {
            throw new DadoInvalidoException("Data obrigatoria no formato yyyy-MM-dd.");
        }
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            fmt.setLenient(false);
            return fmt.parse(iso);
        } catch (Exception e) {
            throw new DadoInvalidoException("Data invalida: '" + iso + "'. Use yyyy-MM-dd.");
        }
    }

    /** Converte java.util.Date em "yyyy-MM-dd" (ou null). */
    public static String format(Date data) {
        if (data == null) return null;
        return new SimpleDateFormat("yyyy-MM-dd").format(data);
    }
}
