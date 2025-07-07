package com.recorder.controller.entity;

import com.recorder.controller.entity.enuns.TipoMetodoPagamento;
import com.recorder.controller.entity.enuns.TipoStatusPagamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
@Getter
@Setter
@NoArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagamento")
    private Long id;

    @NotNull(message = "Data do pagamento é obrigatória")
    @PastOrPresent(message = "Data do pagamento deve ser atual ou passada")
    @Column(name = "data_pagamento", nullable = false)
    private LocalDateTime dataPagamento = LocalDateTime.now();

    @NotNull(message = "Valor do pagamento é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    @Column(name = "valor_pagamento", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorPagamento;

    @NotNull(message = "Método de pagamento é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento", nullable = false, length = 20)
    private TipoMetodoPagamento metodoPagamento;

    @NotNull(message = "Status do pagamento é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false, length = 20)
    private TipoStatusPagamento status = TipoStatusPagamento.PENDENTE;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String descricao;

    @Size(max = 100, message = "ID da transação deve ter no máximo 100 caracteres")
    @Column(name = "id_transacao", length = 100)
    private String idTransacao;

    // Relacionamento com Agendamento (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agendamento")
    private Agendamento agendamento;

    // Relacionamento com Usuário (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // Métodos de negócio
    public boolean isPago() {
        return status == TipoStatusPagamento.CONFIRMADO;
    }

    public boolean podeSerEstornado() {
        return isPago() && dataPagamento.plusDays(30).isAfter(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", valor=" + valorPagamento +
                ", status=" + status +
                '}';
    }
}