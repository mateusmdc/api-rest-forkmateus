package br.uece.alunos.sisreserva.v1.domain.usuario.useCase;

import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioEmailDTO;
import br.uece.alunos.sisreserva.v1.dto.usuario.UsuarioEsqueciSenhaDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.UsuarioRepository;
import br.uece.alunos.sisreserva.v1.dto.utils.MailDTO;
import br.uece.alunos.sisreserva.v1.dto.utils.MessageResponseDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.EmailSendingException;
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

    public MessageResponseDTO esqueciMinhaSenha(UsuarioEmailDTO data) {
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

        var body = """
                Olá %s,

                Recebemos uma solicitação para redefinir a senha da sua conta no SisReserva.
        
                Para continuar com a troca de senha, utilize o token abaixo na aplicação:
        
                Token de redefinição: %s
        
                Este token é válido por 1 hora e deve ser utilizado apenas na plataforma oficial. 
                Por motivos de segurança, não compartilhe este código com ninguém. 
                A equipe do SisReserva nunca solicitará este token por e-mail ou outros meios de contato.
        
                Caso você não tenha solicitado essa troca de senha, ignore este e-mail.
        
                Atenciosamente,
                Equipe SisReserva
                """.formatted(email, token);

        var mailDTO = new MailDTO(subject, email, body);

        try {
            mailSender.sendMail(mailDTO);
            return new MessageResponseDTO("Sucesso no envio de e-mail com dados para troca de senha.");
        } catch (Exception e) {
            throw new EmailSendingException("Erro ao enviar email com os dados para troca de senha.", e);
        }
    }
}