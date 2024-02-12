package com.example.startapp.repository;

import com.example.startapp.entity.Role;
import com.example.startapp.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    Users findByRole(Role role);
}
