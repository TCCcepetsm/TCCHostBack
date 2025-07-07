package com.recorder.controller.entity;

import com.recorder.controller.entity.enuns.TipoStatusEvento;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "eventos_esportivos")
@Getter
@Setter
@NoArgsConstructor
public class EventoEsportivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento_esportivo")
    private Long id;

    @NotBlank(message = "Descrição do evento é obrigatória")
    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    @Column(name = "descricao_evento", nullable = false, length = 200)
    private String descricao;

    @NotNull(message = "Data do evento é obrigatória")
    @Future(message = "Data do evento deve ser futura")
    @Column(name = "data_evento", nullable = false)
    private LocalDate dataEvento;

    @NotNull(message = "Hora de início é obrigatória")
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @NotNull(message = "Hora de término é obrigatória")
    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;

    @NotNull(message = "Local é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_endereco", nullable = false)
    private Endereco local;

    @NotNull(message = "Status do evento é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "status_evento", nullable = false, length = 20)
    private TipoStatusEvento status;

    // Relacionamento com Usuário (organizador do evento)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario organizador;

    // Método para verificar se o evento está ativo
    public boolean isAtivo() {
        return status == TipoStatusEvento.CONFIRMADO ||
                status == TipoStatusEvento.EM_ANDAMENTO;
    }

    // Método para verificar conflito de horários
    public boolean temConflitoHorario(LocalTime novaHoraInicio, LocalTime novaHoraFim) {
        return (novaHoraInicio.isBefore(horaFim) && novaHoraFim.isAfter(horaInicio));
    }

    @Override
    public String toString() {
        return "EventoEsportivo{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", dataEvento=" + dataEvento +
                ", status=" + status +
                '}';
    }
}