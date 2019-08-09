package com.github.attemper.core.dao.mapper.program;

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

    ProgramPackage getPackage(String id);

    void deletePackage(List<String> ids);
}