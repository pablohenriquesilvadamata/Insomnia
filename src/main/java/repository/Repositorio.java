package repository;

import com.Integradesh.controllers.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface Repositorio extends JpaRepository<UsuarioModel, UUID> {
    UsuarioModel findByEmail(String email);
}
