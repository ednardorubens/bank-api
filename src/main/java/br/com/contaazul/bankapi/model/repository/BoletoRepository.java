package br.com.contaazul.bankapi.model.repository;

import br.com.contaazul.bankapi.model.entity.BoletoEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Interface de acesso ao banco de dados
 * @author Ednardo Rubens
 */
@Repository
public interface BoletoRepository extends JpaRepository<BoletoEntity, UUID> {
    
    @Query("SELECT new BoletoEntity(id, due_date, total_in_cents, customer, status) FROM BoletoEntity bol")
    List<BoletoEntity> listarBoletos();
    
}