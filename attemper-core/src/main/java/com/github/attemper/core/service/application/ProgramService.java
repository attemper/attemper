package com.github.attemper.core.service.application;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.IdParam;
import com.github.attemper.common.param.app.program.ProgramListParam;
import com.github.attemper.common.param.app.program.ProgramNameParam;
import com.github.attemper.common.param.app.program.ProgramPackageListParam;
import com.github.attemper.common.result.app.program.CategoryResult;
import com.github.attemper.common.result.app.program.Program;
import com.github.attemper.common.result.app.program.ProgramPackage;
import com.github.attemper.core.dao.application.ProgramMapper;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.core.util.FileUtil;
import com.github.attemper.sys.util.PageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class ProgramService extends BaseServiceAdapter {

    @Autowired
    private ProgramMapper mapper;

    public Program get(ProgramNameParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.get(paramMap);
    }

    public ProgramPackage getPackage(IdParam param) {
        return mapper.getPackage(param.getId());
    }

    public Map<String, Object> list(ProgramListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<Program> list = (Page<Program>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
    }

    public Map<String, Object> listPackage(ProgramPackageListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<ProgramPackage> list = (Page<ProgramPackage>) mapper.listPackage(paramMap);
        return PageUtil.toResultMap(list);
    }

    public List<CategoryResult> listPackageCategory(IdParam param) {
        ProgramPackage programPackage = getPackage(param);
        String userFolderPath = FileUtil.joinUserFolder(param.getId());
        File jarFile = writeJarFile(userFolderPath, programPackage);
        File jarFolderFile = new File(userFolderPath + '/' +
                programPackage.getPackageName().substring(0, programPackage.getPackageName().lastIndexOf(".")));
        try {
            FileUtil.unZip(jarFile, jarFolderFile);
        } catch (IOException e) {
            throw new RTException(1100, e);
        }
        List<CategoryResult> list = new ArrayList<>(32);
        list.add(new CategoryResult()
                .setFileName(jarFolderFile.getName())
                .setFilePath(jarFolderFile.getAbsolutePath())
                .setDir(1));
        recurseCategory(list, jarFolderFile);
        return list;
    }

    public List<ProgramPackage> listLoadedPackage(ProgramNameParam param) {
        return mapper.listLoadedPackage(injectTenantIdToMap(param));
    }

    public File writeJarFile(String userFolderPath, ProgramPackage programPackage) {
        File dir = new File(userFolderPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File jarFile = new File(dir.getAbsolutePath() + '/' + programPackage.getPackageName());
        if (!jarFile.exists()) {
            try {
                FileUtil.byteArray2File(programPackage.getContent(), jarFile);
            } catch (IOException e) {
                throw new RTException(1100, e);
            }
        }
        return jarFile;
    }

    private void recurseCategory(List<CategoryResult> list, File parentFolder) {
        File[] files = parentFolder.listFiles();
        for (File subFile : files) {
            CategoryResult category = new CategoryResult()
                    .setFileName(subFile.getName())
                    .setFilePath(subFile.getAbsolutePath())
                    .setParentFileName(parentFolder.getName());
            list.add(category);
            if (subFile.isDirectory()) {
                category.setDir(1);
                recurseCategory(list, subFile);
            }
        }
    }
}
