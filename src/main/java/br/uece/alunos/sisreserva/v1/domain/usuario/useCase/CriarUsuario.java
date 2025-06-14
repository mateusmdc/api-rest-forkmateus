package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CriarUsuario {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserReturnDTO criar(UsuarioDTO data) {
        boolean usuarioExiste = usuarioRepository.usuarioExistsByEmail(data.email());
        if (usuarioExiste) {
            throw new ValidationException("Email já cadastrado no sistema");
        }



        var updatedData = new UserDTO(
                data.login(),
                data.password(),
                data.name(),
                data.cpf(),
                data.phone(),
                formattedBirthday,
                data.countryId(),
                data.username(),
                data.twoFactorEnabled(),
                data.refreshTokenEnabled(),
                data.theme()
        );

        var createDTO = new UserCreateDTO(updatedData, country);

        var newUser = new User(createDTO);

        String encodedPassword = bCryptPasswordEncoder.encode(data.password());
        newUser.setPassword(encodedPassword);

        var userOnDb = userRepository.save(newUser);

        return new UserReturnDTO(userOnDb);
    }

}
