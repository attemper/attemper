package com.thor.core.dao.repo;

import com.thor.core.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ldang
 */
public interface TenantRepository extends JpaRepository<Tenant, Long>, JpaSpecificationExecutor<Tenant>{

    Tenant findById(String id);

    @Modifying
    @Transactional
    @Query(value="delete from Tenant e where e.id in (:ids) ")
    void deleteByIds(@Param("ids") String[] ids);
}
