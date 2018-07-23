package br.com.contaazul.bankapi.model.entity;

import br.com.contaazul.bankapi.model.utils.MultaUtils;
import br.com.contaazul.bankapi.model.utils.DataUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "Boleto", description = "Representação de um boleto")
@Entity @Getter @ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Table(schema = "bank", name = "tb_boletos")
@EqualsAndHashCode(of = {"id", "due_date", "total_in_cents", "customer", "status"})
@SuppressWarnings({"PersistenceUnitPresent"})
public class BoletoEntity implements Serializable {

    private static final long serialVersionUID = 1736803942798591642L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "Id do boleto", example = "c2dbd236-3fa5-4ccc-9c12-bd0ae1d6dd89", accessMode = ApiModelProperty.AccessMode.READ_ONLY) 
    private final UUID id;
    
    @NotNull(message = "A data de vencimento deve ser preenchida")
    @ApiModelProperty(value = "Data de vencimento do boleto", required = true, example = "2018-01-01")
    private final LocalDate due_date;
    
    @Setter
    @ApiModelProperty(value = "Data de vencimento do boleto", example = "2018-03-01")
    private LocalDate payment_date;
    
    @ApiModelProperty(value = "Data de cancelamento do boleto", example = "2018-01-01", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private LocalDate cancelment_date;
    
    @DecimalMin(value = "1", message = "O valor do boleto deve maior ou igual a 1")
    @NotNull(message = "O valor do boleto deve ser preenchido")
    @ApiModelProperty(value = "Valor do boleto", example = "10000", required = true) 
    private final BigDecimal total_in_cents;
    
    @DecimalMin("0")
    @ApiModelProperty(value = "Multa por atraso do boleto", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private BigDecimal fine;
    
    @NotBlank(message = "O nome do cliente do boleto deve ser preenchido")
    @ApiModelProperty(value = "Nome do cliente do boleto", required = true)
    private final String customer;
    
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Estado do boleto", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
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
            this.cancelment_date = null;
            this.payment_date = dataPagamento;
            this.status = BoletoStatus.PAID;
        }
        return this;
    }
    
    @JsonIgnore
    public BoletoEntity cancelar() {
        this.payment_date = null;
        this.cancelment_date = LocalDate.now();
        this.status = BoletoStatus.CANCELED;
        return this;
    }
    
}