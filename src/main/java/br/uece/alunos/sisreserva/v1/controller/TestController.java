package br.uece.alunos.sisreserva.v1.controller;

import br.uece.alunos.sisreserva.v1.domain.usuario.DTO.UsuarioRetornoDTO;
import br.uece.alunos.sisreserva.v1.domain.usuario.Usuario;
import br.uece.alunos.sisreserva.v1.service.EntityHandlerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teste")
@Tag(name = "Rotas de teste mapeadas no controller")
public class TestController {
    @Autowired
    private EntityHandlerService entityHandlerService;


    @GetMapping("/searchuserbyid/{id}")
    @Transactional
    public ResponseEntity runTeste(@PathVariable String id) {
        System.out.println(id);
        var usuario = entityHandlerService.obterUsuarioPorId(id);
        System.out.println("usuario no controller: "+usuario);
        return ResponseEntity.ok(new UsuarioRetornoDTO(usuario));
    }
}
