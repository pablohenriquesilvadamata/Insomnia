package controllers;

import Teste.src.main.repository.Repositorio;
import models.UsuarioModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import services.FileUploadService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioController {
    @Autowired
    UsuarioRepository;

    @Autowired
    FileUploadService fileUploadService;

    @GetMapping
    public ResponseEntity<List<UsuarioModel>> listarUsuarios() {
        return ResponseEntity.status(HttpStatus.OK).body(Repositorio.findAll());
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Object> exibirUsuario(@PathVariable(value = "idUsuario") UUID id) {
        Optional<UsuarioModel> usuarioBuscado = Repositorio.findById(id);

        if (usuarioBuscado.isEmpty()) {
            // Retornar usuario não encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioBuscado.get());
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> cadastrarUsuario(@ModelAttribute @Valid UsuarioDto usuarioDto) {
        if (Repositorio.findByEmail(usuarioDto.email()) != null) {
            // Não pode cadastrar
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Esse email já está cadastrado!");
        }

        UsuarioModel usuario = new UsuarioModel();
        BeanUtils.copyProperties(usuarioDto, usuario);

        String urlImagem;

        try {
            urlImagem = fileUploadService.FazerUpload(usuarioDto.imagem());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        usuario.setUrl_img(urlImagem);

        return ResponseEntity.status(HttpStatus.CREATED).body(Repositorio.save(usuario));
    }

    @PutMapping(value = "/{idUsuario}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Object> editarUsuario(@PathVariable(value = "idUsuario") UUID id, @ModelAttribute @Valid UsuarioDto usuarioDto) {
        Optional<UsuarioModel> usuarioBuscado = Repositorio.findById(id);

        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }

        UsuarioModel usuario = usuarioBuscado.get();

        BeanUtils.copyProperties(usuarioDto, usuario);

        String urlImagem;

        try {
            urlImagem = fileUploadService.FazerUpload(usuarioDto.imagem());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        usuario.setUrl_img(urlImagem);

        return ResponseEntity.status(HttpStatus.CREATED).body(Repositorio.save(usuario));
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Object> deletarUsuario(@PathVariable(value = "idUsuario") UUID id) {
        Optional<UsuarioModel> usuarioBuscado = Repositorio.findById(id);

        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }

        Repositorio.delete(usuarioBuscado.get());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usuario deletado com sucesso!");
    }


}
