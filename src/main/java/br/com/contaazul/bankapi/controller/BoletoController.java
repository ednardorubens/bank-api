package br.com.contaazul.bankapi.controller;

import br.com.contaazul.bankapi.model.entity.BoletoEntity;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ednardo Rubens
 */
public interface BoletoController {

    List<BoletoEntity> listar();

    BoletoEntity buscar(final UUID boletoId);

    BoletoEntity criar(final BoletoEntity boleto, final HttpServletResponse resposta);

    void pagar(final UUID boletoId, final BoletoEntity boleto);

    void cancelar(final UUID boletoId);
    
}
