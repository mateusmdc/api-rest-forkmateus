package br.uece.alunos.sisreserva.v1.infra.ldap;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LdapProperties.class)
public class LdapConfig {}