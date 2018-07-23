package br.com.contaazul.bankapi.model.utils;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Classe auxiliar para cálculo da diferença de dias entre datas
 * @author Ednardo Rubens
 */
public class DataUtils {

    private DataUtils() {
    }

    public static long diferencaEmDias(LocalDate inicio, LocalDate fim) {
        if (inicio != null && fim != null) {
            return Math.abs(Duration.between(inicio.atStartOfDay(), fim.atStartOfDay()).toDays());
        }
        throw new IllegalArgumentException("Parâmetros inválidos");
    }
}
