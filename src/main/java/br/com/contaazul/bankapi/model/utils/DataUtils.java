package br.com.contaazul.bankapi.model.utils;

import java.time.Duration;
import java.time.LocalDate;

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
