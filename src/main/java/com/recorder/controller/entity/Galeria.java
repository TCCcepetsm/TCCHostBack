package com.recorder.controller.entity;

import com.recorder.controller.entity.enuns.TipoMidia;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "galerias")
@Getter
@Setter
@NoArgsConstructor
public class Galeria {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank(message = "URL da mídia é obrigatória")
        @Column(name = "midia_url", nullable = false, length = 512)
        private String midiaUrl;

        @NotNull(message = "Tipo de mídia é obrigatório")
        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 20)
        private TipoMidia tipo;

        @NotNull(message = "Profissional é obrigatório")
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "profissional_id", nullable = false)
        private Usuario profissional;

        @PastOrPresent(message = "Data de postagem deve ser atual ou passada")
        @Column(name = "data_postagem", nullable = false)
        private LocalDateTime dataPostagem = LocalDateTime.now();

        @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
        @Column(length = 255)
        private String descricao;

        @Column(name = "is_publico", nullable = false)
        private boolean publico = true;

        // Construtor para campos obrigatórios
        public Galeria(String midiaUrl, TipoMidia tipo, Usuario profissional) {
                this.midiaUrl = midiaUrl;
                this.tipo = tipo;
                this.profissional = profissional;
        }

        // Método para verificar se é uma imagem
        public boolean isImagem() {
                return tipo == TipoMidia.FOTO;
        }

        // Método para verificar se é um vídeo
        public boolean isVideo() {
                return tipo == TipoMidia.VIDEO;
        }

        @Override
        public String toString() {
                return "Galeria{" +
                                "id=" + id +
                                ", tipo=" + tipo +
                                ", dataPostagem=" + dataPostagem +
                                '}';
        }
}