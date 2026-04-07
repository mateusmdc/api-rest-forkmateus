package br.uece.alunos.sisreserva.v1.infra.ldap;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds LDAP connection parameters from the environment.
 * All values are supplied via the .env file and never committed to the repository.
 */
@ConfigurationProperties(prefix = "ldap")
public record LdapProperties(
        String url,
        String baseDn,
        String bindDn,
        String bindPassword,
        String userSearchBase,
        String userSearchFilter
) {}