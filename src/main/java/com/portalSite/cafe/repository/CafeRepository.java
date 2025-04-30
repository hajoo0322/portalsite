package com.portalSite.cafe.repository;

import com.portalSite.cafe.entity.Cafe;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long> {



    Optional<Cafe> findByCafeName(String cafeName);

    boolean existsByCafeName(String cafeName);
}
