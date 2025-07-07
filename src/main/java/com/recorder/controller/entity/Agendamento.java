package com.recorder.controller.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.recorder.controller.entity.enuns.StatusAgendamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonManagedReference
    private Usuario usuario;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(nullable = false, length = 100)
    private String email;

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    @Column(length = 20)
    private String telefone;

    @Size(max = 50, message = "Plano deve ter no máximo 50 caracteres")
    @Column(length = 50)
    private String plano;

    @FutureOrPresent(message = "Data deve ser atual ou futura")
    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime horario;

    @NotBlank(message = "Esporte é obrigatório")
    @Size(max = 50, message = "Esporte deve ter no máximo 50 caracteres")
    @Column(nullable = false, length = 50)
    private String esporte;

    @Size(max = 200, message = "Local deve ter no máximo 200 caracteres")
    @Column(length = 200)
    private String local;

    @Digits(integer = 10, fraction = 6, message = "Latitude deve ter no máximo 10 dígitos inteiros e 6 decimais")
    @Column(precision = 10, scale = 6)
    private BigDecimal latitude;

    @Digits(integer = 10, fraction = 6, message = "Longitude deve ter no máximo 10 dígitos inteiros e 6 decimais")
    @Column(precision = 10, scale = 6)
    private BigDecimal longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAgendamento status = StatusAgendamento.PENDENTE;

    // Métodos auxiliares de negócio
    // Adicione estes métodos na sua classe Agendamento existente

    public boolean podeSerCancelado() {
        return this.status == StatusAgendamento.PENDENTE ||
                this.status == StatusAgendamento.CONFIRMADO;
    }

    public boolean estaAtivo() {
        return this.status != StatusAgendamento.RECUSADO;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getPlano() {
        return plano;
    }

    public void setPlano(String plano) {
        this.plano = plano;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public String getEsporte() {
        return esporte;
    }

    public void setEsporte(String esporte) {
        this.esporte = esporte;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    // Equals e HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Agendamento))
            return false;
        return id != null && id.equals(((Agendamento) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // ToString
    @Override
    public String toString() {
        return "Agendamento{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", data=" + data +
                ", status=" + status +
                '}';
    }
}