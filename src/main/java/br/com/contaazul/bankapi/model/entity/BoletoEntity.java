package br.com.contaazul.bankapi.model.entity;

import br.com.contaazul.bankapi.model.utils.MultaUtils;
import br.com.contaazul.bankapi.model.utils.DataUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidade boleto
 * @author Ednardo Rubens
 */
@Entity @Getter @ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Table(schema = "bank", name = "tb_boletos")
@EqualsAndHashCode(of = {"id", "due_date", "total_in_cents", "customer", "status"})
@SuppressWarnings({"PersistenceUnitPresent", "HasNoArgConstructor"})
public class BoletoEntity implements Serializable {

    private static final long serialVersionUID = 1736803942798591642L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final UUID id;
    
    @NotNull(message = "A data de vencimento deve ser preenchida")
    private final LocalDate due_date;
    
    @Setter
    private LocalDate payment_date;
    
    private LocalDate cancelment_date;
    
    @DecimalMin("0")
    @NotNull(message = "O valor do boleto deve ser preenchido")
    private final BigDecimal total_in_cents;
    
    @DecimalMin("0")
    private BigDecimal fine;
    
    @NotBlank(message = "O nome do cliente do boleto deve ser preenchido")
    private final String customer;
    
    @Enumerated(EnumType.STRING)
    private BoletoStatus status;

    public BoletoEntity() {
        this(null, null, null, null);
    }
    
    public BoletoEntity(LocalDate due_date, BigDecimal total_in_cents, String customer) {
        this(UUID.randomUUID(), due_date, total_in_cents, customer, BoletoStatus.PENDING);
    }
    
    public BoletoEntity(UUID id, LocalDate due_date, BigDecimal total_in_cents, String customer) {
        this(id, due_date, total_in_cents, customer, BoletoStatus.PENDING);
    }
    
    public BoletoEntity(UUID id, LocalDate due_date, BigDecimal total_in_cents, String customer, BoletoStatus status) {
        this.id = id;
        this.due_date = due_date;
        this.total_in_cents = total_in_cents;
        this.customer = customer;
        this.status = status;
    }

    @JsonIgnore
    public boolean estaPendente() {
        return BoletoStatus.PENDING.equals(status);
    }
    
    @JsonIgnore
    public boolean estaPago() {
        return BoletoStatus.PAID.equals(status);
    }
    
    @JsonIgnore
    public boolean foiCancelado() {
        return BoletoStatus.CANCELED.equals(status);
    }

    @JsonIgnore
    public BoletoEntity atualizarMulta() {
        if (estaPendente()) {
            final long diasEmAtraso = DataUtils.diferencaEmDias(this.due_date, LocalDate.now());
            this.fine = MultaUtils.calcularMulta(diasEmAtraso, this.total_in_cents);
        }
        return this;
    }
    
    @JsonIgnore
    public BoletoEntity pagar(final LocalDate dataPagamento) {
        if (dataPagamento != null) { 
            this.payment_date = dataPagamento;
            this.status = BoletoStatus.PAID;
        }
        return this;
    }
    
    @JsonIgnore
    public BoletoEntity cancelar() {
        this.cancelment_date = LocalDate.now();
        this.status = BoletoStatus.CANCELED;
        return this;
    }
    
}