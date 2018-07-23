package br.com.contaazul.bankapi.controller;

import br.com.contaazul.bankapi.BankApiApplication;
import br.com.contaazul.bankapi.controller.exception.BoletoInvalidException;
import br.com.contaazul.bankapi.controller.exception.BoletoNotFoundException;
import br.com.contaazul.bankapi.controller.exception.BoletoNotProvidedException;
import br.com.contaazul.bankapi.model.entity.BoletoEntity;
import br.com.contaazul.bankapi.model.utils.Boletos;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.BDDAssertions.then;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Classe
 * @author Ednardo Rubens
 */
@SuppressWarnings("null")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BoletoControllerImplIntegrationTest {
    
    @Autowired
    private TestRestTemplate template;
    
    private static final String URL = "/bankslips";
    private static final String BANKSLIP_NOT_FOUND = "Bankslip not found with the specified id";
    private static final String BANKSLIP_NOT_PROVIDED = "Bankslip not provided in the request body";
    private static final String INVALID_BANKSLIP_PROVIDED = "Invalid bankslip provided. The possible reasons are:";
    
    private HttpEntity<BoletoEntity> getBoleto(BoletoEntity boleto) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<BoletoEntity> entity = new HttpEntity<>(boleto, headers);
        return entity;
    }

    private String criarBoleto() throws MalformedURLException {
        final ResponseEntity<BoletoEntity> resposta = template.postForEntity(URL, getBoleto(Boletos.boletoNovo()), BoletoEntity.class);
        then(resposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        final BoletoEntity boleto = resposta.getBody();
        then(boleto).isNotNull();
        final HttpHeaders headers = resposta.getHeaders();
        then(headers).isNotEmpty();
        final URI location = headers.getLocation();
        then(location).isNotNull();
        return URL + "/" + boleto.getId();
    }
    
    @Test
    public void tentarCriarBoleto() throws MalformedURLException {
        criarBoleto();
    }
    
    @Test
    public void criarBoletoSemBody() throws MalformedURLException {
        final ResponseEntity<BoletoNotProvidedException> resposta = template.postForEntity(URL, getBoleto(null), BoletoNotProvidedException.class);
        then(resposta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(resposta.getBody()).isNotNull();
        then(resposta.getBody().getMessage()).isEqualTo(BANKSLIP_NOT_PROVIDED);
    }
    
    @Test
    public void criarBoletoSemRequisitosNecessarios() throws MalformedURLException {
        final ResponseEntity<BoletoInvalidException> resposta = template.postForEntity(URL, getBoleto(new BoletoEntity()), BoletoInvalidException.class);
        then(resposta.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        then(resposta.getBody()).isNotNull();
        then(resposta.getBody().getMessage()).startsWith(INVALID_BANKSLIP_PROVIDED);
    }
    
    @Test
    public void listarBoletos() throws MalformedURLException {
        criarBoleto();
        final ResponseEntity<List> resposta = template.getForEntity(URL, List.class);
        then(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(resposta.getBody()).isNotEmpty();
    }

    @Test
    public void buscarBoleto() throws MalformedURLException {
        final String urlBoletoCriado = criarBoleto();
        final ResponseEntity<BoletoEntity> resposta = template.getForEntity(urlBoletoCriado, BoletoEntity.class);
        then(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(resposta.getBody()).isNotNull();
    }
    
    @Test
    public void buscarBoletoInexistente() throws MalformedURLException {
        final String urlBoleto = URL + "/3646caf7-dbac-4761-8ced-e05c23f97443";
        final ResponseEntity<BoletoNotFoundException> resposta = template.getForEntity(urlBoleto , BoletoNotFoundException.class);
        then(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(resposta.getBody()).isNotNull();
        then(resposta.getBody().getMessage()).isEqualTo(BANKSLIP_NOT_FOUND);
    }

    @Test
    public void pagarBoleto() throws MalformedURLException {
        final String urlBoleto = criarBoleto();
        final BoletoEntity boletoPraPagar = new BoletoEntity();
        boletoPraPagar.setPayment_date(LocalDate.now());
        final ResponseEntity<String> resposta = template.postForEntity(urlBoleto + "/payments", getBoleto(boletoPraPagar), String.class);
        then(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        then(resposta.getBody()).isNull();
    }
    
    @Test
    public void tentarPagarBoletoInexistente() throws MalformedURLException {
        final String urlBoleto = URL + "/3646caf7-dbac-4761-8ced-e05c23f97443";
        final BoletoEntity boletoPraPagar = new BoletoEntity();
        boletoPraPagar.setPayment_date(LocalDate.now());
        final ResponseEntity<BoletoNotFoundException> resposta = template.postForEntity(urlBoleto + "/payments", getBoleto(boletoPraPagar), BoletoNotFoundException.class);
        then(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(resposta.getBody()).isNotNull();
        then(resposta.getBody().getMessage()).isEqualTo(BANKSLIP_NOT_FOUND);
    }
    
    @Test
    public void tentarPagarBoletoSemData() throws MalformedURLException {
        final String urlBoleto = criarBoleto();
        final ResponseEntity<BoletoNotFoundException> resposta = template.postForEntity(urlBoleto + "/payments", getBoleto(new BoletoEntity()), BoletoNotFoundException.class);
        then(resposta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(resposta.getBody()).isNotNull();
        then(resposta.getBody().getMessage()).isEqualTo(BANKSLIP_NOT_PROVIDED);
    }

    @Test
    public void cancelarBoleto() throws MalformedURLException {
        final String urlBoleto = criarBoleto();
        final ResponseEntity<String> resposta = template.exchange(urlBoleto, HttpMethod.DELETE, null, String.class);
        then(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        then(resposta.getBody()).isNull();
    }
    
    @Test
    public void tentarCancelarBoletoInexistente() throws MalformedURLException {
        final String urlBoleto = URL + "/3646caf7-dbac-4761-8ced-e05c23f97443";
        final ResponseEntity<BoletoNotFoundException> resposta = template.exchange(urlBoleto, HttpMethod.DELETE, null, BoletoNotFoundException.class);
        then(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(resposta.getBody()).isNotNull();
        then(resposta.getBody().getMessage()).isEqualTo(BANKSLIP_NOT_FOUND);
    }
}
