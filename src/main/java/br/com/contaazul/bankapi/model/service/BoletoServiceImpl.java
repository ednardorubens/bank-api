package br.com.contaazul.bankapi.model.service;

import br.com.contaazul.bankapi.controller.exception.BoletoInvalidException;
import br.com.contaazul.bankapi.controller.exception.BoletoNotFoundException;
import br.com.contaazul.bankapi.controller.exception.BoletoNotProvidedException;
import br.com.contaazul.bankapi.model.entity.BoletoEntity;
import br.com.contaazul.bankapi.model.repository.BoletoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação da interface de serviço do boleto
 * @author Ednardo Rubens
 */
@Log4j2
@Service
@Transactional(readOnly = true)
public class BoletoServiceImpl implements BoletoService {
    
    @Autowired
    private Validator validator;

    @Autowired
    private BoletoRepository boletoRepository;

    @Override
    public List<BoletoEntity> listar() {
        return boletoRepository.listarBoletos();
    }
    
    @Override
    public BoletoEntity buscar(final UUID boletoId) {
        log.debug("Buscando o boleto id: " + Optional.ofNullable(boletoId).toString());
        return boletoRepository.findById(boletoId).orElseThrow(BoletoNotFoundException::new).atualizarMulta();
    }

    private BoletoEntity salvar(final BoletoEntity boleto) {
        final BoletoEntity boletoOpt = Optional.ofNullable(boleto).orElseThrow(BoletoNotProvidedException::new);
        final Set<ConstraintViolation<BoletoEntity>> violations = validator.validate(boletoOpt);
        if (violations.isEmpty()) {
            return boletoRepository.save(boletoOpt);
        } else {
            throw new BoletoInvalidException(violations);
        }
    }
    
    @Override
    @Transactional(readOnly = false)
    public BoletoEntity criar(final BoletoEntity boleto) {
        final BoletoEntity boletoOpt = Optional.ofNullable(boleto).orElseThrow(BoletoNotProvidedException::new);
        log.debug("Criando o boleto: " + boletoOpt.toString());
        return salvar(new BoletoEntity(boletoOpt.getDue_date(), boletoOpt.getTotal_in_cents(), boletoOpt.getCustomer()));
    }
    
    @Override
    @Transactional(readOnly = false)
    public void pagar(final UUID boletoId, final LocalDate dataPagamento) {
        final BoletoEntity boletoEncontrado = buscar(boletoId);
        log.debug("Pagando o boleto: " + Optional.ofNullable(boletoEncontrado).toString());
        salvar(boletoEncontrado.pagar(dataPagamento));
    }
    
    @Override
    @Transactional(readOnly = false)
    public void cancelar(final UUID boletoId) {
        final BoletoEntity boletoEncontrado = buscar(boletoId);
        log.debug("Cancelando o boleto: " + Optional.ofNullable(boletoEncontrado).toString());
        salvar(boletoEncontrado.cancelar());
    }
}
