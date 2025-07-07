import com.recorder.controller.entity.Agendamento;
import com.recorder.controller.entity.Usuario;
import com.recorder.controller.entity.enuns.StatusAgendamento;
import com.recorder.dto.AgendamentoDTO;
import com.recorder.exception.AgendamentoException;
import com.recorder.repository.AgendamentoRepository;
import com.recorder.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Agendamento criarAgendamento(AgendamentoDTO dto, String emailUsuario) {
        // 1. Validação de conflitos de horário
        validarConflitoAgendamento(dto.getData(), dto.getHorario(), dto.getLocal());

        // 2. Busca otimizada do usuário
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new AgendamentoException(
                        "Usuário não encontrado: " + emailUsuario,
                        HttpStatus.NOT_FOUND));

        // 3. Construção do agendamento com timezone específico
        Agendamento agendamento = Agendamento.builder()
                .usuario(usuario)
                .nome(dto.getNome())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .plano(dto.getPlano())
                .data(dto.getData())
                .horario(dto.getHorario())
                .esporte(dto.getEsporte())
                .local(dto.getLocal())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .status(dto.getStatus() != null ? dto.getStatus() : StatusAgendamento.PENDENTE)
                .dataCriacao(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .build();

        return agendamentoRepository.save(agendamento);
    }

    private void validarConflitoAgendamento(LocalDate data, LocalTime horario, String local) {
        boolean existeConflito = agendamentoRepository.existsByDataAndHorarioAndLocal(
                data, horario, local);

        if (existeConflito) {
            throw new AgendamentoException(
                    "Já existe um agendamento para este horário e local",
                    HttpStatus.CONFLICT);
        }
    }
}