// PasswordResetTokenRepository.java
package com.gestor.repository;

import com.gestor.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByToken(UUID token);
    void deleteByUsuario_IdUsuario(Long idUsuario);
}
