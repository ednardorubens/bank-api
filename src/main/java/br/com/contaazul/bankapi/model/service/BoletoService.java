package br.com.contaazul.bankapi.model.service;

import br.com.contaazul.bankapi.model.entity.BoletoEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Interface de serviço do boleto
 * @author Ednardo Rubens
 */
public interface BoletoService {

    List<BoletoEntity> listar();

    BoletoEntity buscar(final UUID boletoId);

    BoletoEntity criar(final BoletoEntity boleto);

    void pagar(final UUID boletoId, final LocalDate dataPagamento);

    void cancelar(final UUID boletoId);
    
}
