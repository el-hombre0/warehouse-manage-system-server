package ru.evendot.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.evendot.warehouse.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
}
