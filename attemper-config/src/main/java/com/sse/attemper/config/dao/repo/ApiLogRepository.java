package com.sse.attemper.config.dao.repo;

import com.sse.attemper.config.entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long>, JpaSpecificationExecutor<ApiLog> {

}
