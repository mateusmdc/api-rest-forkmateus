package br.uece.alunos.sisreserva.v1.infra.security;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço para obter informações do usuário autenticado no contexto de segurança.
 * Fornece métodos utilitários para verificar permissões e cargos do usuário logado.
 */
@Service
public class UsuarioAutenticadoService {

    /**
     * Obtém o usuário autenticado do contexto de segurança do Spring Security.
     * 
     * @return Usuario autenticado ou null se não houver usuário autenticado
     */
    public Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof Usuario) {
            return (Usuario) principal;
        }
        
        return null;
    }

    /**
     * Obtém a lista de cargos (roles) do usuário autenticado.
     * 
     * @return Lista com os nomes dos cargos do usuário ou lista vazia se não houver usuário autenticado
     */
    public List<String> getCargosUsuarioAutenticado() {
        Usuario usuario = getUsuarioAutenticado();
        
        if (usuario == null) {
            return List.of();
        }
        
        return usuario.getRoles();
    }

    /**
     * Verifica se o usuário autenticado possui um cargo específico.
     * 
     * @param nomeCargo Nome do cargo a ser verificado (ex: "ADMIN", "USUARIO_EXTERNO")
     * @return true se o usuário possui o cargo, false caso contrário
     */
    public boolean usuarioPossuiCargo(String nomeCargo) {
        List<String> cargos = getCargosUsuarioAutenticado();
        return cargos.contains(nomeCargo);
    }

    /**
     * Verifica se o usuário autenticado é um usuário externo.
     * Usuários externos possuem o cargo "USUARIO_EXTERNO".
     * 
     * @return true se o usuário é externo, false caso contrário
     */
    public boolean isUsuarioExterno() {
        return usuarioPossuiCargo("USUARIO_EXTERNO");
    }

    /**
     * Verifica se o usuário autenticado é um usuário interno.
     * Usuários internos possuem o cargo "USUARIO_INTERNO".
     * 
     * @return true se o usuário é interno, false caso contrário
     */
    public boolean isUsuarioInterno() {
        return usuarioPossuiCargo("USUARIO_INTERNO");
    }

    /**
     * Verifica se o usuário autenticado é um administrador.
     * Administradores possuem o cargo "ADMIN".
     * 
     * @return true se o usuário é admin, false caso contrário
     */
    public boolean isAdmin() {
        return usuarioPossuiCargo("ADMIN");
    }

    /**
     * Verifica se o usuário autenticado deve ter restrições de visualização de espaços.
     * Usuários externos só podem visualizar espaços multiusuário.
     * Admins e usuários internos não possuem restrições.
     * 
     * @return true se o usuário deve ver apenas espaços multiusuário, false caso contrário
     */
    public boolean deveRestringirEspacos() {
        return isUsuarioExterno() && !isAdmin();
    }
}
