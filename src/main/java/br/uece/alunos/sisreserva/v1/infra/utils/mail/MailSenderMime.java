package br.uece.alunos.sisreserva.v1.infra.utils.mail;

import br.uece.alunos.sisreserva.v1.dto.utils.MailDTO;
import br.uece.alunos.sisreserva.v1.infra.exceptions.EmailSendingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailSenderMime {
    @Value("${spring.mail.username}")
    private String fromMail;

    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendMail(MailDTO data) {
        try {
            var mimeMessage = javaMailSender.createMimeMessage();
            var mimeMessageHelper = new MimeMessageHelper(mimeMessage, false);

            mimeMessageHelper.setFrom(fromMail);
            mimeMessageHelper.setTo(data.to());
            mimeMessageHelper.setSubject(data.subject());
            mimeMessageHelper.setText(data.Body());

            javaMailSender.send(mimeMessage);

            return true;
        } catch (Exception e) {
            throw new EmailSendingException("Erro ao enviar email", e);
        }
    }
}