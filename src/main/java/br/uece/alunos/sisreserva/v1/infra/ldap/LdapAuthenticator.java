package br.uece.alunos.sisreserva.v1.infra.ldap;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

@Component
@RequiredArgsConstructor
public class LdapAuthenticator {

    private final LdapProperties ldapProperties;

    public void authenticate(String ldapUsername, String senha) {
        String userDn = resolveUserDn(ldapUsername);
        bindAsUser(userDn, senha);
    }

    private String resolveUserDn(String ldapUsername) {
        Hashtable<String, String> env = buildServiceAccountEnv();

        try {
            InitialDirContext ctx = new InitialDirContext(env);
            try {
                SearchControls controls = new SearchControls();
                controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

                String filter = ldapProperties.userSearchFilter().replace("{0}", ldapUsername);
                NamingEnumeration<SearchResult> results = ctx.search(
                        ldapProperties.userSearchBase(), filter, controls
                );

                if (!results.hasMore()) {
                    throw new BadCredentialsException("Usuário não encontrado no diretório.");
                }

                return results.next().getNameInNamespace();
            } finally {
                ctx.close();
            }
        } catch (BadCredentialsException e) {
            throw e;
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao conectar ao servidor LDAP.", e);
        }
    }

    private void bindAsUser(String userDn, String senha) {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapProperties.url());
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userDn);
        env.put(Context.SECURITY_CREDENTIALS, senha);

        try {
            InitialDirContext ctx = new InitialDirContext(env);
            ctx.close();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Credenciais inválidas.");
        } catch (NamingException e) {
            throw new RuntimeException("Erro ao conectar ao servidor LDAP.", e);
        }
    }

    private Hashtable<String, String> buildServiceAccountEnv() {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapProperties.url() + "/" + ldapProperties.baseDn());
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, ldapProperties.bindDn());
        env.put(Context.SECURITY_CREDENTIALS, ldapProperties.bindPassword());
        return env;
    }
}