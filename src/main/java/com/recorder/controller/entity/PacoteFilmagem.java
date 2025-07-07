package com.recorder.controller.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "pacotes_filmagem")
@Getter
@Setter
@NoArgsConstructor
public class PacoteFilmagem {

    public enum NivelQualidade {
        BASICO, // Equipamentos simples, edição mínima
        STANDARD, // Equipamentos intermediários
        PREMIUM, // Equipamentos profissionais
        PROFISSIONAL // Equipamentos top de linha + serviços extras
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pacote_id")
    private Long id;

    @NotBlank(message = "Nome do pacote é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String descricao;

    @NotNull(message = "Duração é obrigatória")
    @Positive(message = "Duração deve ser positiva")
    @Column(name = "duracao_horas", nullable = false)
    private Integer duracaoHoras;

    @NotNull(message = "Preço base é obrigatório")
    @PositiveOrZero(message = "Preço deve ser positivo ou zero")
    @Column(name = "preco_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoBase;

    @NotNull(message = "Nível de qualidade é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_qualidade", nullable = false, length = 20)
    private NivelQualidade nivelQualidade;

    @ElementCollection
    @CollectionTable(name = "pacote_equipamentos", joinColumns = @JoinColumn(name = "pacote_id"))
    @Column(name = "equipamento", length = 100)
    private List<String> equipamentosInclusos;

    @ElementCollection
    @CollectionTable(name = "pacote_servicos", joinColumns = @JoinColumn(name = "pacote_id"))
    @Column(name = "servico", length = 100)
    private List<String> servicosInclusos;

    @Column(name = "maximo_pessoas")
    private Integer maximoPessoas;

    @Column(name = "destaque", nullable = false)
    private boolean destaque = false;

    @Column(name = "mais_vendido", nullable = false)
    private boolean maisVendido = false;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    // Construtor principal
    public PacoteFilmagem(String nome, Integer duracaoHoras, BigDecimal precoBase,
            NivelQualidade nivelQualidade) {
        this.nome = nome;
        this.duracaoHoras = duracaoHoras;
        this.precoBase = precoBase;
        this.nivelQualidade = nivelQualidade;

        // Configurações padrão baseadas no nível
        if (nivelQualidade == NivelQualidade.BASICO) {
            this.maximoPessoas = 1;
        } else if (nivelQualidade == NivelQualidade.STANDARD) {
            this.maximoPessoas = 2;
        } else {
            this.maximoPessoas = 3; // Para premium e profissional
        }
    }

    // Métodos de negócio
    public boolean requerEquipeEspecial() {
        return nivelQualidade == NivelQualidade.PREMIUM ||
                nivelQualidade == NivelQualidade.PROFISSIONAL;
    }

    public boolean isPlanoBasico() {
        return nivelQualidade == NivelQualidade.BASICO;
    }

    public void adicionarEquipamento(String equipamento) {
        this.equipamentosInclusos.add(equipamento);
    }

    public void adicionarServico(String servico) {
        this.servicosInclusos.add(servico);
    }

    @Override
    public String toString() {
        return "PacoteFilmagem{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", nivel=" + nivelQualidade +
                ", preco=R$" + precoBase +
                '}';
    }
}