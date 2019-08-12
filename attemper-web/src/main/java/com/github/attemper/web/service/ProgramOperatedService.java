package com.github.attemper.web.service;

import com.github.attemper.common.constant.GlobalConstants;
import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.IdParam;
import com.github.attemper.common.param.IdsParam;
import com.github.attemper.common.param.app.program.*;
import com.github.attemper.common.result.app.program.CategoryResult;
import com.github.attemper.common.result.app.program.Program;
import com.github.attemper.common.result.app.program.ProgramPackage;
import com.github.attemper.config.base.property.AppProperties;
import com.github.attemper.core.dao.mapper.program.ProgramMapper;
import com.github.attemper.core.service.project.ProjectService;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.sys.util.FileUtil;
import com.github.attemper.sys.util.PageUtil;
import com.github.attemper.web.ext.app.ExecutorHandler;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.net.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.camunda.commons.utils.IoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
    private IdGenerator idGenerator;

    @Autowired
    private ExecutorHandler executorHandler;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private AppProperties appProperties;

    public Program get(ProgramNameParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        return mapper.get(paramMap);
    }

    public Program add(ProgramSaveParam param) {
        Program program = get(new ProgramNameParam().setProgramName(param.getProgramName()));
        if (program != null) {
            throw new DuplicateKeyException(param.getProgramName());
        }
        program = toProgram(param);
        program.setCreateTime(new Date());
        mapper.add(program);
        return program;
    }

    public Program update(ProgramSaveParam param) {
        Program oldProgram = get(new ProgramNameParam().setProgramName(param.getProgramName()));
        if (oldProgram == null) {
            return add(param);
        }
        Program updatedProgram = toProgram(param);
        mapper.update(updatedProgram);
        return updatedProgram;
    }

    public Map<String, Object> list(ProgramListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<Program> list = (Page<Program>) mapper.list(paramMap);
        return PageUtil.toResultMap(list);
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

    public Map<String, Object> listPackage(ProgramPackageListParam param) {
        Map<String, Object> paramMap = injectTenantIdToMap(param);
        PageHelper.startPage(param.getCurrentPage(), param.getPageSize());
        Page<ProgramPackage> list = (Page<ProgramPackage>) mapper.listPackage(paramMap);
        return PageUtil.toResultMap(list);
    }

    public ProgramPackage uploadPackage(String programName, MultipartFile file) {
        Program program = get(new ProgramNameParam().setProgramName(programName));
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
                    .setPackageContent(file.getBytes());
        } catch (IOException e) {
            throw new RTException(1100, e);
        }
        mapper.addPackage(programPackage);
        return programPackage;
    }

    public void downloadPackage(HttpServletResponse response, IdParam param) {
        ProgramPackage programPackage = mapper.getPackage(param.getId());
        response.setHeader("Content-Disposition", "attachment; filename=" + programPackage.getPackageName());
        response.setContentType(MediaType.ZIP.toString());
        try {
            FileCopyUtils.copy(programPackage.getPackageContent(), response.getOutputStream());
        } catch (IOException e) {
            throw new RTException(1100, e);
        }
    }

    public ProgramPackage loadPackage(IdParam param) {
        for (String url : toExecutorUrls()) {
            executorHandler.load(url, param);
        }
        return null;
    }

    public ProgramPackage unloadPackage(IdParam param) {
        for (String url : toExecutorUrls()) {
            executorHandler.unload(url, param);
        }
        return null;
    }

    private List<String> toExecutorUrls() {
        List<String> list = projectService.listExecutor(null);
        if (list.size() == 0) {
            List<ServiceInstance> instances = discoveryClient.getInstances(appProperties.getExecutor().getName());
            instances.forEach(item -> list.add(item.getUri().toString()));
        }
        return list;
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

    public List<CategoryResult> listPackageCategory(IdParam param) {
        ProgramPackage programPackage = mapper.getPackage(param.getId());
        File dir = new File(folder(param.getId()));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File jarFile = new File(dir.getAbsolutePath() + File.separator + programPackage.getPackageName());
        if (log.isDebugEnabled()) {
            log.debug("jar file path: {}", jarFile.getAbsolutePath());
        }
        try {
            FileUtil.byteArray2File(programPackage.getPackageContent(), jarFile);
        } catch (IOException e) {
            throw new RTException(1100, e);
        }
        File jarFolderFile = new File(dir.getAbsolutePath() + File.separator +
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
                .setDir(true));
        recurseCategory(list, jarFolderFile);
        return list;
    }

    public Void removePackage(IdsParam param) {
        mapper.deletePackage(param.getIds());
        for (String id : param.getIds()) {
            File dir = new File(folder(id));
            dir.delete();
        }
        return null;
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
                category.setDir(true);
                recurseCategory(list, subFile);
            }
        }
    }

    /**
     * xxx/usr/xxx/attemper/{id}
     * @param id
     * @return
     */
    private String folder(String id) {
        return FileUtil.getUserHomePath() + File.separatorChar + GlobalConstants.defaultContextPath + File.separatorChar + id;
    }
}
