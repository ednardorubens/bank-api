package br.com.contaazul.bankapi.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Ednardo Rubens
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BoletoNotProvidedException extends RuntimeException {
    
    private static final long serialVersionUID = -350583336563418868L;

    public BoletoNotProvidedException(String message) {
        super(message);
    }

    public BoletoNotProvidedException() {
        super("Bankslip not provided in the request body");
    }
}