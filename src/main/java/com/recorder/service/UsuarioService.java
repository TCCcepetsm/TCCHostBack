import com.recorder.controller.entity.Usuario;
import com.recorder.controller.entity.enuns.Roles;
import com.recorder.dto.UsuarioDTO;
import com.recorder.exception.BusinessException;
import com.recorder.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario registrar(UsuarioDTO dto) {
        // Validações
        if (!dto.getSenha().equals(dto.getConfirmarSenha())) {
            throw new BusinessException("As senhas não coincidem", HttpStatus.BAD_REQUEST);
        }

        if (!dto.agreeTerms()) {
            throw new BusinessException("Você deve aceitar os termos", HttpStatus.BAD_REQUEST);
        }

        if (repository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado", HttpStatus.CONFLICT);
        }

        // Construção do usuário
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail().toLowerCase()) // Normalização para PostgreSQL
                .cpf(dto.getCpf().replaceAll("\\D", "")) // Remove formatação
                .telefone(dto.getTelefone().replaceAll("\\D", ""))
                .senha(passwordEncoder.encode(dto.getSenha()))
                .roles(Set.of(Roles.ROLE_USUARIO))
                .ativo(true)
                .build();

        return repository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario autenticar(String email, String senha) {
        Usuario usuario = repository.findByEmailWithRoles(email.toLowerCase())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new BusinessException("Credenciais inválidas", HttpStatus.UNAUTHORIZED);
        }

        if (!usuario.isAtivo()) {
            throw new BusinessException("Usuário desativado", HttpStatus.FORBIDDEN);
        }

        return usuario;
    }

    // ======== CRUD ========
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return repository.findAllUsuariosWithRoles();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return repository.findByIdWithRoles(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public Usuario atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = buscarPorId(id);

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail().toLowerCase());
        usuario.setCpf(dto.getCpf().replaceAll("\\D", ""));
        usuario.setTelefone(dto.getTelefone().replaceAll("\\D", ""));

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        return repository.save(usuario);
    }

    @Transactional
    public void desativarUsuario(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(false);
        repository.save(usuario);
    }
}