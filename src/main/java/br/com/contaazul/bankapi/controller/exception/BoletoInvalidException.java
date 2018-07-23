package br.com.contaazul.bankapi.controller.exception;

import br.com.contaazul.bankapi.model.entity.BoletoEntity;
import java.util.Set;
import javax.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excessão gerada ao tentar utilizar boletos inválidos
 * @author Ednardo Rubens
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class BoletoInvalidException extends RuntimeException {

    private static final long serialVersionUID = -350583336563418868L;

    public BoletoInvalidException(String message) {
        super(message);
    }

    public BoletoInvalidException(final Set<ConstraintViolation<BoletoEntity>> violations) {
        super(formatar(violations));
    }

    private static String formatar(final Set<ConstraintViolation<BoletoEntity>> violations) {
        final StringBuilder sb = new StringBuilder("Invalid bankslip provided. The possible reasons are: ");
        violations.forEach(violation -> sb.append(" * ").append(StringUtils.capitalize(violation.getMessage())).append(";"));
        return sb.toString();
    }
}
