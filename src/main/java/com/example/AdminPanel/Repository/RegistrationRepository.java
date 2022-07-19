package com.example.AdminPanel.Repository;

import com.example.AdminPanel.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<UserEntity,Long>
{
    @Query("SELECT m FROM UserEntity m WHERE m.email =:email")
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT m FROM UserEntity  m WHERE m.id= :adminId AND m.role= :adminROle")
    Optional<UserEntity> findByAdminRoleOrId(Long adminId, String adminROle);

    Optional<UserEntity> findOneByEmailIgnoreCase(String username);

}
