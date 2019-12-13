package com.github.attemper.core.dao.application;

import com.github.attemper.common.result.app.program.Program;
import com.github.attemper.common.result.app.program.ProgramPackage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ProgramMapper {

    void add(Program model);

    void update(Program model);

    Program get(Map<String, Object> paramMap);

    List<Program> list(Map<String, Object> paramMap);

    void delete(Map<String, Object> paramMap);

    List<ProgramPackage> listPackage(Map<String, Object> paramMap);

    void addPackage(ProgramPackage programPackage);

    ProgramPackage getPackage(String id);

    void updatePackage(ProgramPackage programPackage);

    void deletePackage(List<String> ids);

    List<ProgramPackage> listPackageByIds(List<String> ids);

    List<ProgramPackage> listLoadedPackage(Map<String, Object> paramMap);
}