package com.recorder.controller.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "enderecos")
@Getter
@Setter
@NoArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Logradouro é obrigatório")
    @Size(max = 150, message = "Logradouro deve ter no máximo 150 caracteres")
    @Column(nullable = false, length = 150)
    private String logradouro;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 20, message = "Número deve ter no máximo 20 caracteres")
    @Column(nullable = false, length = 20)
    private String numero;

    @Size(max = 100, message = "Complemento deve ter no máximo 100 caracteres")
    @Column(length = 100)
    private String complemento;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String bairro;

    @NotBlank(message = "Cidade é obrigatório")
    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres (UF)")
    @Column(nullable = false, length = 2)
    private String estado;

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve estar no formato 00000-000 ou 00000000")
    @Column(nullable = false, length = 9)
    private String cep;

    @Size(max = 50, message = "País deve ter no máximo 50 caracteres")
    @Column(length = 50)
    private String pais = "Brasil"; // Valor padrão

    // Construtor para os campos obrigatórios
    public Endereco(String logradouro, String numero, String bairro,
            String cidade, String estado, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    // Método utilitário para formatação
    public String formatarEndereco() {
        return String.format("%s, %s %s - %s, %s/%s - CEP: %s",
                logradouro,
                numero,
                complemento != null ? complemento : "",
                bairro,
                cidade,
                estado,
                cep);
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "id=" + id +
                ", logradouro='" + logradouro + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}