package com.example.AdminPanel.Repository;

import com.example.AdminPanel.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>
{
 //   @Query("SELECT m FROM UserEntity m WHERE m.role =:role")
    @Query("SELECT m.name FROM UserEntity m WHERE m.role =:role AND  m.name LIKE %:searchText%")
    List<String> findByRoleOrName(String role,String searchText);


    @Modifying
    @Transactional
    @Query("DELETE FROM UserEntity m WHERE m.id =:id")
    void deleteUser(Long id);

    @Query("SELECT m FROM UserEntity m WHERE m.name LIKE %:searchText%")
    List<String> findBySerchtaxt(String searchText);
    @Query("SELECT m FROM UserEntity  m WHERE m.id= :adminId AND m.role= :adminRole")
    Optional<UserEntity> findByAdminRoleOrId(Long adminId, String adminRole);


    //    @Modifying
//    @Transactional
//    @Query("SELECT m FROM UserEntity  m WHERE m.id= :id AND m.profile=:url")
//    void Update(Long id, String url);
//    @Query("SELECT m FROM UserEntity m WHERE m.id =:id AND  m.profile=:m.profile")
//    UserEntity getBy(Long id, String profile);


}
