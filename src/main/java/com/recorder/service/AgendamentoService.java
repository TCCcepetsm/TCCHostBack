package com.recorder.service;

import com.recorder.controller.entity.Agendamento;
import com.recorder.controller.entity.Usuario;
import com.recorder.controller.entity.enuns.StatusAgendamento;
import com.recorder.dto.AgendamentoDTO;
import com.recorder.exception.AgendamentoException;
import com.recorder.exception.ResourceNotFoundException;
import com.recorder.repository.AgendamentoRepository;
import com.recorder.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Agendamento criarAgendamento(AgendamentoDTO dto, String emailUsuario) {
        validarConflitoAgendamento(dto.getData(), dto.getHorario(), dto.getLocal());

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Agendamento agendamento = new Agendamento();
        // Configuração dos campos básicos
        agendamento.setUsuario(usuario);
        agendamento.setNome(dto.getNome());
        agendamento.setEmail(dto.getEmail());
        agendamento.setTelefone(dto.getTelefone());
        agendamento.setPlano(dto.getPlano());
        agendamento.setData(dto.getData());
        agendamento.setHorario(dto.getHorario());
        agendamento.setEsporte(dto.getEsporte());
        agendamento.setLocal(dto.getLocal());
        agendamento.setLatitude(dto.getLatitude());
        agendamento.setLongitude(dto.getLongitude());
        agendamento.setStatus(StatusAgendamento.PENDENTE);

        return agendamentoRepository.save(agendamento);
    }

    @Transactional(readOnly = true)
    public Page<Agendamento> listarAgendamentosPorUsuario(String email, Pageable pageable) {
        return agendamentoRepository.findByUsuarioEmail(email, pageable);
    }

    @Transactional(readOnly = true)
    public Agendamento buscarAgendamentoPorIdEUsuario(Long id, String email) {
        return agendamentoRepository.findByIdAndUsuarioEmail(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));
    }

    @Transactional
    public void cancelarAgendamento(Long id, String email) {
        Agendamento agendamento = buscarAgendamentoPorIdEUsuario(id, email);

        if (!agendamento.podeSerCancelado()) {
            throw new AgendamentoException(
                    "Agendamento não pode ser cancelado no status atual",
                    HttpStatus.BAD_REQUEST);
        }

        agendamento.setStatus(StatusAgendamento.RECUSADO);
        agendamentoRepository.save(agendamento);
    }

    @Transactional
    public Agendamento confirmarAgendamento(Long id, String profissionalEmail) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado"));

        if (agendamento.getStatus() != StatusAgendamento.PENDENTE) {
            throw new AgendamentoException(
                    "Somente agendamentos pendentes podem ser confirmados",
                    HttpStatus.BAD_REQUEST);
        }

        agendamento.setStatus(StatusAgendamento.CONFIRMADO);
        return agendamentoRepository.save(agendamento);
    }

    private void validarConflitoAgendamento(LocalDate data, LocalTime horario, String local) {
        if (agendamentoRepository.existsByDataAndHorarioAndLocal(data, horario, local)) {
            throw new AgendamentoException(
                    "Já existe um agendamento para este horário e local",
                    HttpStatus.CONFLICT);
        }
    }
}