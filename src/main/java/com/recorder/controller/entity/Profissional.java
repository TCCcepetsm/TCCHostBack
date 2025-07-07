package com.recorder.controller.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "profissionais")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Profissional extends Usuario {

   @Column(name = "ativo", nullable = false)
   private boolean ativo = true;

   @Column(name = "registro_profissional", length = 50)
   private String registroProfissional;

   @Column(name = "especializacao", length = 100)
   private String especializacao;

   @Column(name = "avaliacao_media")
   private Double avaliacaoMedia;

   @Column(name = "total_servicos")
   private Integer totalServicosRealizados = 0;

   @ElementCollection
   @CollectionTable(name = "profissional_equipamentos", joinColumns = @JoinColumn(name = "profissional_id"))
   @Column(name = "equipamento")
   private Set<String> equipamentos = new HashSet<>();

   @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<Agendamento> agendamentos = new HashSet<>();

   // Métodos de negócio
   public void adicionarEquipamento(String equipamento) {
      this.equipamentos.add(equipamento);
   }

   public void removerEquipamento(String equipamento) {
      this.equipamentos.remove(equipamento);
   }

   public void incrementarServicosRealizados() {
      this.totalServicosRealizados++;
   }

   public void atualizarAvaliacao(double novaAvaliacao) {
      if (this.avaliacaoMedia == null) {
         this.avaliacaoMedia = novaAvaliacao;
      } else {
         this.avaliacaoMedia = (this.avaliacaoMedia + novaAvaliacao) / 2;
      }
   }
}