package com.fidegamestore.repository;

import com.fidegamestore.domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    @Query(nativeQuery = true,
        value = "SELECT * from usuario u WHERE u.username = :username and u.activo=True;")
    public Optional<Usuario> findByUsernameAndActivoTrue(@Param("username") String username);
    
    @Query(nativeQuery = true,
        value = "SELECT * from usuario u WHERE u.activo=True;")
    public List<Usuario> findByActivoTrue();
    
    @Query(nativeQuery = true,
        value = "SELECT * from usuario u WHERE u.username = :username ;")
    public Optional<Usuario> findByUsername(@Param("username") String username);
    
    @Query(nativeQuery = true,
        value = "SELECT * from usuario u WHERE u.username = :username and u.password = :password ;")
    public Optional<Usuario> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
    
    @Query(nativeQuery = true,
        value = "SELECT * from usuario u WHERE u.username = :username or u.correo= :correo ;")
    public Optional<Usuario> findByUsernameOrCorreo(@Param("username") String username, @Param("correo") String correo);
    
    public boolean existsByUsernameOrCorreo(String username, String correo);
}
