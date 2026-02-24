package br.uece.alunos.sisreserva.v1.infra.security;

import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public class UsuarioCache {

    private final ConcurrentHashMap<String, Usuario> cache = new ConcurrentHashMap<>();

    /**
     * Returns the cached user for the given key, loading it via the provided
     * function if absent.
     *
     * @param key    the JWT subject (user email)
     * @param loader function to load the user from the database on cache miss
     * @return the cached or freshly loaded Usuario
     */
    public Usuario computeIfAbsent(String key, Function<String, Usuario> loader) {
        return cache.computeIfAbsent(key, loader);
    }

    /**
     * Removes a single user from the cache.
     *
     * @param key the JWT subject (user email)
     */
    public void evict(String key) {
        cache.remove(key);
    }

    /**
     * Clears the entire cache.
     */
    public void clear() {
        cache.clear();
    }
}