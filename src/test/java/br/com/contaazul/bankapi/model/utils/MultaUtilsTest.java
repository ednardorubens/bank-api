package br.com.contaazul.bankapi.model.utils;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

/**
 * MultaUtils Teste
 * @author Ednardo Rubens
 */
public class MultaUtilsTest {
    
    public MultaUtilsTest() {
    }

    @Test
    public void deveRetornarZeroQuandoNaoHouverAtraso() {
        long diasEmAtraso = 0L;
        BigDecimal valorBoleto = BigDecimal.TEN;
        BigDecimal expResult = BigDecimal.ZERO;
        BigDecimal result = MultaUtils.calcularMulta(diasEmAtraso, valorBoleto);
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void deveRetornarMultaTresDias() {
        long diasEmAtraso = 3L;
        BigDecimal valorBoleto = new BigDecimal("99000");
        BigDecimal expResult = new BigDecimal("1485");
        BigDecimal result = MultaUtils.calcularMulta(diasEmAtraso, valorBoleto);
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void deveRetornarMultaAteDezDias() {
        long diasEmAtraso = 10L;
        BigDecimal valorBoleto = new BigDecimal("50000");
        BigDecimal expResult = new BigDecimal("2500");
        BigDecimal result = MultaUtils.calcularMulta(diasEmAtraso, valorBoleto);
        Assert.assertEquals(expResult, result);
    }

    @Test
    public void deveRetornarMultaMaisDezDias() {
        long diasEmAtraso = 25L;
        BigDecimal valorBoleto = new BigDecimal("50000");
        BigDecimal expResult = new BigDecimal("12500");
        BigDecimal result = MultaUtils.calcularMulta(diasEmAtraso, valorBoleto);
        Assert.assertEquals(expResult, result);
    }
}