package com.recorder.controller.entity;

import com.recorder.dto.AgendamentoDTO;
import com.recorder.exception.AgendamentoNaoEncontradoException;
import com.recorder.exception.OperacaoNaoPermitidaException;
import com.recorder.repository.AgendamentoRepository;
import com.recorder.repository.UsuarioRepository;
import com.recorder.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/agendamentos")
@Tag(name = "Agendamentos", description = "API para gerenciamento de agendamentos")
@SecurityRequirement(name = "bearerAuth")
public class AgendamentoController {

	private final AgendamentoService agendamentoService;
	private final UsuarioRepository usuarioRepository;
	private final AgendamentoRepository agendamentoRepository;

	public AgendamentoController(AgendamentoService agendamentoService,
			UsuarioRepository usuarioRepository,
			AgendamentoRepository agendamentoRepository) {
		this.agendamentoService = agendamentoService;
		this.usuarioRepository = usuarioRepository;
		this.agendamentoRepository = agendamentoRepository;
	}

	@PostMapping
	@PreAuthorize("hasRole('ROLE_USUARIO')")
	@Operation(summary = "Criar novo agendamento")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Agendamento> criarAgendamento(
			@Valid @RequestBody AgendamentoDTO agendamentoDTO,
			@AuthenticationPrincipal UserDetails userDetails) {

		log.info("Criação de agendamento solicitada por: {}", userDetails.getUsername());

		Agendamento novoAgendamento = agendamentoService.criarAgendamento(
				agendamentoDTO,
				userDetails.getUsername());

		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(novoAgendamento.getId())
				.toUri();

		return ResponseEntity.created(location).body(novoAgendamento);
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_PROFISSIONAL', 'ROLE_ADMIN')")
	@Operation(summary = "Listar agendamentos do usuário")
	public ResponseEntity<Page<Agendamento>> listarAgendamentos(
			@AuthenticationPrincipal UserDetails userDetails,
			@PageableDefault(size = 10) Pageable pageable) {

		Page<Agendamento> agendamentos = agendamentoService
				.listarAgendamentosPorUsuario(userDetails.getUsername(), pageable);

		return ResponseEntity.ok(agendamentos);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_PROFISSIONAL', 'ROLE_ADMIN')")
	@Operation(summary = "Obter detalhes de um agendamento")
	public ResponseEntity<Agendamento> obterAgendamento(
			@PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails) {

		Agendamento agendamento = agendamentoService
				.buscarAgendamentoPorIdEUsuario(id, userDetails.getUsername());

		return ResponseEntity.ok(agendamento);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USUARIO')")
	@Operation(summary = "Cancelar um agendamento")
	public ResponseEntity<Void> cancelarAgendamento(
			@PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails) {

		agendamentoService.cancelarAgendamento(id, userDetails.getUsername());
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/confirmar")
	@PreAuthorize("hasAnyRole('ROLE_PROFISSIONAL', 'ROLE_ADMIN')")
	@Operation(summary = "Confirmar um agendamento")
	public ResponseEntity<Agendamento> confirmarAgendamento(
			@PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails) {

		Agendamento agendamento = agendamentoService
				.confirmarAgendamento(id, userDetails.getUsername());

		return ResponseEntity.ok(agendamento);
	}
}