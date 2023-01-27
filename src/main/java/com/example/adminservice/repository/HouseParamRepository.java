package com.example.adminservice.repository;

import com.example.adminservice.entity.HousesParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseParamRepository extends JpaRepository<HousesParam, Long> {
}
