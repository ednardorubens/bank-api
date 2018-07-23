package br.com.contaazul.bankapi.model.service;

import br.com.contaazul.bankapi.controller.exception.BoletoInvalidException;
import br.com.contaazul.bankapi.controller.exception.BoletoNotFoundException;
import br.com.contaazul.bankapi.controller.exception.BoletoNotProvidedException;
import br.com.contaazul.bankapi.model.entity.BoletoEntity;
import br.com.contaazul.bankapi.model.repository.BoletoRepository;
import br.com.contaazul.bankapi.model.utils.Boletos;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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
public class BoletoServiceImplTest {
    
    @Mock
    private Validator validator;

    @Mock
    private BoletoRepository boletoRepository;

    @InjectMocks
    private BoletoService boletoService = new BoletoServiceImpl();

    @Test
    public void listarBoletos() {
        final List<BoletoEntity> boletos = Boletos.lista();
        Mockito.when(boletoRepository.listarBoletos()).thenReturn(boletos);
        Assert.assertEquals(boletos, boletoService.listar());
    }

    @Test
    public void deveBuscarUmBoleto() {
        final BoletoEntity boletoPendente = Boletos.boletoPendente();
        Mockito.when(boletoRepository.findById(boletoPendente.getId())).thenReturn(Optional.ofNullable(boletoPendente));
        Assert.assertEquals(boletoPendente, boletoService.buscar(boletoPendente.getId()));
    }
    
    @Test(expected = BoletoNotFoundException.class)
    public void deveGerarExceptionAoBuscarUmBoleto() {
        final BoletoEntity boletoPendente = Boletos.boletoPendente();
        Mockito.when(boletoRepository.findById(boletoPendente.getId())).thenReturn(Optional.ofNullable(null));
        Assert.assertEquals(boletoPendente, boletoService.buscar(boletoPendente.getId()));
    }

    @Test
    public void criarBoletoPendente() {
        final BoletoEntity boletoPendente = Boletos.boletoPendente();
        Mockito.when(boletoRepository.save(Mockito.any(BoletoEntity.class))).thenAnswer(iom -> (BoletoEntity) iom.getArgument(0));
        final BoletoEntity boletoSalvo = boletoService.criar(boletoPendente);
        Assert.assertEquals("Boleto diferente", boletoPendente, boletoSalvo);
    }
    
    @Test(expected = BoletoNotProvidedException.class)
    public void tentarCriarBoletoNulo() {
        boletoService.criar(null);
    }
    
    @Test(expected = BoletoInvalidException.class)
    public void tentarCriarBoletoSemParametrosObrigatorios() {
        final Set<ConstraintViolation<BoletoEntity>> violations = new HashSet<>();
        violations.add(Mockito.mock(ConstraintViolation.class));
        Mockito.when(validator.validate(Mockito.any(BoletoEntity.class))).thenReturn(violations);
        boletoService.criar(Boletos.boletoNulo());
    }

    @Test
    public void pagarBoletoPendente() {
        final BoletoEntity boletoPendente = Boletos.boletoPendente();
        Mockito.when(boletoRepository.findById(boletoPendente.getId())).thenReturn(Optional.ofNullable(boletoPendente));
        Mockito.when(boletoRepository.save(Mockito.any(BoletoEntity.class))).thenAnswer(iom -> (BoletoEntity) iom.getArgument(0));
        boletoService.pagar(boletoPendente.getId(), LocalDate.now());
        Assert.assertTrue(boletoPendente.estaPago());
    }

    @Test
    public void cancelarBoletoPendente() {
        final BoletoEntity boletoPendente = Boletos.boletoPendente();
        Mockito.when(boletoRepository.findById(boletoPendente.getId())).thenReturn(Optional.ofNullable(boletoPendente));
        Mockito.when(boletoRepository.save(Mockito.any(BoletoEntity.class))).thenAnswer(iom -> (BoletoEntity) iom.getArgument(0));
        boletoService.cancelar(boletoPendente.getId());
        Assert.assertTrue(boletoPendente.foiCancelado());
    }
}