package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioEmailDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioEsqueciSenhaDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.dto.utils.MailDTO;
import br.uece.alunos.sisreserva.v1.infra.utils.mail.*;
import br.uece.alunos.sisreserva.v1.infra.exceptions.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EsqueciSenha {
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private GerarTokenEsqueciMinhaSenha mailToken;
    @Autowired
    private MailSenderMime mailSender;

    public boolean esqueciMinhaSenha(UsuarioEmailDTO data) {
        var email = data.email();
        var usuarioExiste = repository.usuarioExistsByEmail(email);

        if (!usuarioExiste) {
            throw new ValidationException("Não foi encontrado nenhum usuário com o e-mail informado para processo de Esqueci Minha Senha.");
        }

        var token = mailToken.gerarTokenEmail();
        var emUmaHora = LocalDateTime.now().plusHours(1);
        var esqueciSenhaDTO = new UsuarioEsqueciSenhaDTO(token, emUmaHora);

        var usuario = repository.findByEmailToHandle(email);
        usuario.esqueciSenha(esqueciSenhaDTO);

        var subject = "Esqueci Minha Senha - Sis Reserva";

        var mailDTO = new MailDTO(subject, email, token);

        return mailSender.sendMail(mailDTO);
    }
}