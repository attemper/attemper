package com.github.attemper.core.dao.application;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.app.program.Program;
import com.github.attemper.common.result.app.program.ProgramPackage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ProgramMapper extends BaseMapper<Program> {

    List<ProgramPackage> listPackage(Map<String, Object> paramMap);

    void addPackage(ProgramPackage programPackage);

    ProgramPackage getPackage(Map<String, Object> paramMap);

    void updatePackage(ProgramPackage programPackage);

    void deletePackage(List<String> ids);

    List<ProgramPackage> listPackageByIds(List<String> ids);

    List<ProgramPackage> listLoadedPackage(Map<String, Object> paramMap);
}