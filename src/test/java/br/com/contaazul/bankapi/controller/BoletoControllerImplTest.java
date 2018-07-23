package br.com.contaazul.bankapi.controller;

import br.com.contaazul.bankapi.controller.exception.BoletoInvalidException;
import br.com.contaazul.bankapi.controller.exception.BoletoNotFoundException;
import br.com.contaazul.bankapi.controller.exception.BoletoNotProvidedException;
import br.com.contaazul.bankapi.model.entity.BoletoEntity;
import br.com.contaazul.bankapi.model.service.BoletoService;
import br.com.contaazul.bankapi.model.utils.Boletos;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 *
 * @author Ednardo Rubens
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("FieldMayBeFinal")
public class BoletoControllerImplTest {

    @Mock
    private BoletoService boletoService;
    
    @Mock
    private HttpServletResponse resposta;
    
    @InjectMocks
    private BoletoController boletoController = new BoletoControllerImpl();

    @Test
    public void listarBoletos() {
        final List<BoletoEntity> boletos = Boletos.lista();
        Mockito.when(boletoService.listar()).thenReturn(boletos);
        Assert.assertEquals(boletos, boletoController.listar());
    }
    
    @Test
    public void deveBuscarUmBoleto() {
        final BoletoEntity boletoPendente = Boletos.boletoPendente();
        Mockito.when(boletoService.buscar(boletoPendente.getId())).thenReturn(boletoPendente);
        Assert.assertEquals(boletoPendente, boletoController.buscar(boletoPendente.getId()));
    }
    
    @Test(expected = BoletoNotFoundException.class)
    public void deveGerarExceptionAoBuscarUmBoleto() {
        final BoletoEntity boletoPendente = Boletos.boletoPendente();
        Mockito.when(boletoService.buscar(boletoPendente.getId())).thenThrow(BoletoNotFoundException.class);
        Assert.assertEquals(boletoPendente, boletoController.buscar(boletoPendente.getId()));
    }

    @Test
    public void criarBoletoPendente() {
        final BoletoEntity boletoPendente = Boletos.boletoPendente();
        Mockito.when(boletoService.criar(Mockito.any(BoletoEntity.class))).thenAnswer(iom -> (BoletoEntity) iom.getArgument(0));
        final BoletoEntity boletoSalvo = boletoController.criar(boletoPendente, resposta);
        Assert.assertEquals("Boleto diferente", boletoPendente, boletoSalvo);
    }

    
    @Test(expected = BoletoNotProvidedException.class)
    public void tentarCriarBoletoNulo() {
        boletoController.criar(null, resposta);
    }
    
    @Test(expected = BoletoInvalidException.class)
    public void tentarCriarBoletoSemParametrosObrigatorios() {
        Mockito.when(boletoService.criar(Mockito.any(BoletoEntity.class))).thenThrow(BoletoInvalidException.class);
        boletoController.criar(new BoletoEntity(), resposta);
    }
    
}