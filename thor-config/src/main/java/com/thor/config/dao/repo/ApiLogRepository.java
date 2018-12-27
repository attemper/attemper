package com.thor.config.dao.repo;

import com.thor.config.entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long>, JpaSpecificationExecutor<ApiLog> {

}
