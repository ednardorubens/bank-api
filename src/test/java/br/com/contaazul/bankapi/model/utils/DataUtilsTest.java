package br.com.contaazul.bankapi.model.utils;

import java.text.ParseException;
import java.time.LocalDate;
import org.junit.Test;
import org.junit.Assert;

/**
 * DataUtils Teste
 * @author Ednardo Rubens
 */
public class DataUtilsTest {
    
    @Test
    public void deveRetornarAdiferencaEmDias() throws ParseException {
        LocalDate inicio = LocalDate.parse("2018-07-01");
        LocalDate fim = LocalDate.parse("2018-07-14");
        long result = DataUtils.diferencaEmDias(inicio, fim);
        Assert.assertEquals(13L, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void deveRetornarUmaExcessaoValorNulo() throws ParseException {
        DataUtils.diferencaEmDias(null, null);
    }
    
}
