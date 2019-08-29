package com.github.attemper.web.service.application;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.IdParam;
import com.github.attemper.common.param.IdsParam;
import com.github.attemper.common.param.app.program.ProgramNameParam;
import com.github.attemper.common.param.app.program.ProgramNamesParam;
import com.github.attemper.common.param.app.program.ProgramSaveParam;
import com.github.attemper.common.result.app.program.Program;
import com.github.attemper.common.result.app.program.ProgramPackage;
import com.github.attemper.core.dao.application.ProgramMapper;
import com.github.attemper.core.service.application.ProgramService;
import com.github.attemper.core.service.application.ProjectService;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.FileUtil;
import com.github.attemper.web.ext.app.ExecutorHandler;
import com.google.common.net.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.camunda.commons.utils.IoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class ProgramOperatedService extends BaseServiceAdapter {

    @Autowired
    private ProgramMapper mapper;

    @Autowired
    private ProgramService programService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ExecutorHandler executorHandler;

    @Autowired
    private ProjectService projectService;

    public Program add(ProgramSaveParam param) {
        Program program = programService.get(new ProgramNameParam().setProgramName(param.getProgramName()));
        if (program != null) {
            throw new DuplicateKeyException(param.getProgramName());
        }
        program = toProgram(param);
        program.setCreateTime(new Date());
        mapper.add(program);
        return program;
    }

    public Program update(ProgramSaveParam param) {
        Program oldProgram = programService.get(new ProgramNameParam().setProgramName(param.getProgramName()));
        if (oldProgram == null) {
            return add(param);
        }
        Program updatedProgram = toProgram(param);
        mapper.update(updatedProgram);
        return updatedProgram;
    }

    public Void remove(ProgramNamesParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        mapper.delete(paramMap);
        return null;
    }

    private Program toProgram(ProgramSaveParam param) {
        return new Program()
                .setProgramName(param.getProgramName())
                .setInjectOrder(param.getInjectOrder())
                .setTenantId(injectTenantId());
    }

    public ProgramPackage uploadPackage(String programName, MultipartFile file) {
        Program program = programService.get(new ProgramNameParam().setProgramName(programName));
        if (program == null) {
            throw new RTException(6650);
        }
        if (file == null) {
            throw new RTException(6651);
        }
        ProgramPackage programPackage;
        try {
            programPackage = new ProgramPackage()
                    .setId(idGenerator.getNextId())
                    .setProgramName(programName)
                    .setPackageName(file.getResource().getFilename())
                    .setUploadTime(new Date())
                    .setTenantId(injectTenantId())
                    .setContent(file.getBytes());
        } catch (IOException e) {
            throw new RTException(1100, e);
        }
        mapper.addPackage(programPackage);
        return programPackage;
    }

    public void downloadPackage(HttpServletResponse response, IdParam param) {
        ProgramPackage programPackage = programService.getPackage(param);
        response.setHeader("Content-Disposition", "attachment; filename=" + programPackage.getPackageName());
        response.setContentType(MediaType.ZIP.toString());
        try {
            FileCopyUtils.copy(programPackage.getContent(), response.getOutputStream());
        } catch (IOException e) {
            throw new RTException(1100, e);
        }
    }

    public ProgramPackage loadPackage(IdParam param) {
        ProgramPackage programPackage = validateAndGet(param);
        if (programPackage.getLoadTime() != null && programPackage.getUnloadTime() == null) {
            throw new RTException(2008);
        }
        List<ProgramPackage> loadedPackages = programService.listLoadedPackage(new ProgramNameParam().setProgramName(programPackage.getProgramName()));
        if (loadedPackages.size() != 0) {
            throw new RTException(2009);
        }
        for (String url : projectService.toExecutorUrls()) {
            executorHandler.load(url, param);
        }
        programPackage.setLoadTime(new Date());
        programPackage.setUnloadTime(null);
        mapper.updatePackage(programPackage);
        return null;
    }

    public ProgramPackage unloadPackage(IdParam param) {
        ProgramPackage loadedPackage = validateAndGet(param);
        if (loadedPackage.getLoadTime() == null) {
            throw new RTException(2006);
        }
        if (loadedPackage.getUnloadTime() != null) {
            throw new RTException(2007);
        }
        for (String url : projectService.toExecutorUrls()) {
            executorHandler.unload(url, new IdParam().setId(loadedPackage.getId()));
        }
        loadedPackage.setUnloadTime(new Date());
        mapper.updatePackage(loadedPackage);
        return null;
    }

    public Void removePackage(IdsParam param) {
        List<ProgramPackage> packages = mapper.listPackageByIds(param.getIds());
        long count = packages.stream()
                .filter(item -> item.getLoadTime() != null && item.getUnloadTime() == null)
                .count();
        if (count > 0) {
            throw new RTException(6652);
        }
        mapper.deletePackage(param.getIds());
        for (String id : param.getIds()) {
            File dir = new File(FileUtil.joinUserFolder(id));
            dir.delete();
        }
        return null;
    }

    private ProgramPackage validateAndGet(IdParam param) {
        ProgramPackage programPackage = programService.getPackage(param);
        if (programPackage == null) {
            throw new RTException(6650);
        }
        return programPackage;
    }

    public String viewFile(String filePath) {
        File file = new File(filePath);
        return IoUtil.fileAsString(file);
    }

    public void downloadFile(HttpServletResponse response, String filePath) {
        File file = new File(filePath);
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.setContentType(MediaType.FORM_DATA.toString());
        try {
            FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
        } catch (IOException e) {
            throw new RTException(1100, e);
        }
    }

}
