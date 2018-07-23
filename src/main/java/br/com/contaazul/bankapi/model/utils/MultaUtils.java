package br.com.contaazul.bankapi.model.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Classe auxiliar para cálculo de multa
 * @author Ednardo Rubens
 */
public class MultaUtils {
    
    private MultaUtils() {}
    
    public static BigDecimal calcularMulta(final long diasEmAtraso, final BigDecimal valorBoleto) {
        BigDecimal multa = BigDecimal.ZERO;
        if (diasEmAtraso > 0L) {
            double taxa = diasEmAtraso * ((diasEmAtraso <= 10L) ? 0.005 : 0.01);
            multa = valorBoleto.multiply(new BigDecimal(taxa)).setScale(0, RoundingMode.HALF_UP);
        }
        return multa;
    }
}
