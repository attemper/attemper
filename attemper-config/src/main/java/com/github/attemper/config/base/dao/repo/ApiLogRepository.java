package com.github.attemper.config.base.dao.repo;

import com.github.attemper.config.base.entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long>, JpaSpecificationExecutor<ApiLog> {

}
