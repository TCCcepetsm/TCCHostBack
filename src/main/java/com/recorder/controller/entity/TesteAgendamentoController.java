package com.recorder.controller.entity;

import com.recorder.controller.entity.enuns.StatusAgendamento;
import com.recorder.repository.AgendamentoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/teste/agendamentos")
@Tag(name = "Testes - Agendamentos", description = "Endpoints para testes de agendamentos")
public class TesteAgendamentoController {

    private final AgendamentoRepository agendamentoRepository;

    // Injeção por construtor (recomendado em vez de @Autowired)
    public TesteAgendamentoController(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    @PostMapping
    @Operation(summary = "Criar agendamento de teste")
    public ResponseEntity<String> criarAgendamentoTeste() {
        Agendamento agendamento = new Agendamento();
        agendamento.setDescricao("TESTE PERSISTÊNCIA - " + System.currentTimeMillis());
        agendamento.setData(LocalDate.now());
        agendamento.setStatus(StatusAgendamento.PENDENTE);

        agendamentoRepository.save(agendamento);

        return ResponseEntity.ok()
                .body("Agendamento de teste criado com ID: " + agendamento.getId());
    }

    @GetMapping
    @Operation(summary = "Listar todos os agendamentos de teste")
    public ResponseEntity<List<Agendamento>> listarTodosAgendamentosTeste() {
        List<Agendamento> agendamentos = agendamentoRepository.findAll();

        if (agendamentos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(agendamentos);
    }

    @DeleteMapping
    @Operation(summary = "Limpar todos os agendamentos de teste")
    public ResponseEntity<String> limparAgendamentosTeste() {
        long count = agendamentoRepository.count();

        if (count == 0) {
            return ResponseEntity.ok("Nenhum agendamento para remover");
        }

        agendamentoRepository.deleteAll();
        return ResponseEntity.ok(count + " agendamentos de teste removidos");
    }
}