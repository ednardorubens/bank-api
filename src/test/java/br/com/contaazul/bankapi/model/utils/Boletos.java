package br.com.contaazul.bankapi.model.utils;

import br.com.contaazul.bankapi.model.entity.BoletoEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Classe com boletos a serem utilizados nos testes
 * @author Ednardo Rubens
 */
public class Boletos {

    public Boletos() {
    }
    
    public static BoletoEntity boletoNulo() {
        return new BoletoEntity();
    }
    
    public static BoletoEntity boletoNovo() {
        return new BoletoEntity(LocalDate.parse("2018-01-01"), new BigDecimal("100000"), "Ford Prefect Company");
    }
    
    public static BoletoEntity boletoPendente() {
        return new BoletoEntity(
            UUID.fromString("84e8adbf-1a14-403b-ad73-d78ae19b59bf"),
            LocalDate.parse("2018-01-01"), new BigDecimal("100000"), "Ford Prefect Company");
    }

    public static BoletoEntity boletoCancelado() {
        final BoletoEntity boleto = new BoletoEntity(UUID.fromString("3646caf7-dbac-4761-8ced-e05c23f97443"),
                LocalDate.parse("2018-07-01"), new BigDecimal("100000"), "Trillian Company");
        return boleto.cancelar();
    }
    
    public static BoletoEntity boletoPago() {
        final BoletoEntity boleto = new BoletoEntity(UUID.fromString("c2dbd236-3fa5-4ccc-9c12-bd0ae1d6dd89"),
                LocalDate.parse("2018-05-10"), new BigDecimal("99000"), "Ford Prefect Company");
        return boleto.pagar(LocalDate.parse("2018-05-13"));
    }

    public static List<BoletoEntity> lista() {
        final List<BoletoEntity> boletos = new ArrayList<>();
        boletos.add(boletoPendente());
        boletos.add(boletoCancelado());
        boletos.add(boletoPago());
        return boletos;
    }
}
