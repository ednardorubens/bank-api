package br.com.contaazul.bankapi.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excessão gerada ao tentar utilizar boletos inexistentes
 * @author Ednardo Rubens
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BoletoNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = -350583336563418868L;

    public BoletoNotFoundException(String message) {
        super(message);
    }

    public BoletoNotFoundException() {
        super("Bankslip not found with the specified id");
    }
}