package com.fidegamestore.repository;

import com.fidegamestore.domain.Region;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    @Query(nativeQuery = true,
                value = "SELECT * from region r;")
    public List<Region> selectAllRegions();

    public List<Region> findByActivoTrue();
}
