package br.com.contaazul.bankapi.controller;

import br.com.contaazul.bankapi.controller.exception.BoletoNotProvidedException;
import br.com.contaazul.bankapi.model.entity.BoletoEntity;
import br.com.contaazul.bankapi.model.service.BoletoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


/**
 * Implementação da interface de controller dos boletos
 * @author Ednardo Rubens
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/bankslips")
@Api(value="List operations of control bankslip")
public class BoletoControllerImpl implements BoletoController {

    @Autowired
    private BoletoService boletoService;

    @ApiOperation(value = "List all bankslips")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok")
    })
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<BoletoEntity> listar() {
        return boletoService.listar();
    }

    @ApiOperation(value="Get a detailed bankslip")
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 404, message = "Bankslip not found with the specified id")
    })
    @Override
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BoletoEntity buscar(@PathVariable("id") final UUID boletoId) {
        return boletoService.buscar(boletoId);
    }

    @ApiOperation(value = "Create a bankslip")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Bankslip created"),
        @ApiResponse(code = 400, message = "Bankslip not provided in the request body"),
        @ApiResponse(code = 422, message = "Invalid bankslip provided.The possible reasons are: * Reasons")
    })
    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BoletoEntity criar(@RequestBody(required = false) final BoletoEntity boleto, final HttpServletResponse response) {
        final BoletoEntity boletoCriado = Optional.ofNullable(boletoService.criar(boleto)).orElseThrow(BoletoNotProvidedException::new);
        response.setHeader("Location", getLocation(boletoCriado.getId()));
        return boletoCriado;
    }
    
    @ApiOperation(value="Pay a bankslip")
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "No content"),
        @ApiResponse(code = 404, message = "Bankslip not found with the specified id")
    })
    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(path = "/{id}/payments", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void pagar(@PathVariable("id") final UUID boletoId, @RequestBody(required = false) final BoletoEntity boleto) {
        if (boleto == null || boleto.getPayment_date() == null) {
            throw new BoletoNotProvidedException();
        }
        boletoService.pagar(boletoId, boleto.getPayment_date());
    }

    @ApiOperation(value="Cancel a bankslip")
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Bankslip canceled"),
        @ApiResponse(code = 404, message = "Bankslip not found with the specified id")
    })
    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void cancelar(@PathVariable("id") final UUID boletoId) {
        boletoService.cancelar(boletoId);
    }

    /**
     * Método utilizado para montar a url do Location
     * @param boletoId
     * @return url do recurso
     */
    private String getLocation(final UUID boletoId) {
        try {
            return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(boletoId).toUri().toString();
        } catch (Exception e) {
            return "./bankslips/" + boletoId;
        }
    }

}