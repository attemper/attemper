package com.github.attemper.executor.service;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.param.IdParam;
import com.github.attemper.common.result.app.program.ProgramPackage;
import com.github.attemper.core.service.application.ProgramService;
import com.github.attemper.executor.util.CustomURLClassLoader;
import com.github.attemper.sys.service.BaseServiceAdapter;
import com.github.attemper.core.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ProgramUpdatedService extends BaseServiceAdapter {

    @Autowired
    private ProgramService programService;

    @Autowired
    private ProcessEngineConfigurationImpl processEngineConfiguration;

    @Autowired
    private RepositoryService repositoryService;

    public Void loadPackage(IdParam param) {
        ProgramPackage programPackage = validateAndGet(param);
        load(programPackage);
        return null;
    }

    public Void unloadPackage(IdParam param) {
        ProgramPackage programPackage = programService.getPackage(param);
        unload(programPackage);
        return null;
    }

    public void load(ProgramPackage programPackage) {
        String userFolderPath = FileUtil.joinUserFolder(programPackage.getId());
        File jarFile = programService.writeJarFile(userFolderPath, programPackage);
        URL url;
        try {
            url = jarFile.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RTException(2004, e);
        }
        CustomURLClassLoader customURLClassLoader;
        if (processEngineConfiguration.getClassLoader() instanceof CustomURLClassLoader) {
            customURLClassLoader = (CustomURLClassLoader) processEngineConfiguration.getClassLoader();
            customURLClassLoader.uploadURL(url);
        } else {
            customURLClassLoader = new CustomURLClassLoader(new URL[]{url});
            processEngineConfiguration.setClassLoader(customURLClassLoader);
        }
    }

    public void unload(ProgramPackage programPackage) {
        String unloadingFilePath = FileUtil.joinUserFolder(programPackage.getId()) + '/' + programPackage.getPackageName();
        File unloadingFile = new File(unloadingFilePath);
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery()
                .tenantIdIn(programPackage.getTenantId())
                .list();
        definitions.forEach(item -> processEngineConfiguration.getDeploymentCache().removeProcessDefinition(item.getId()));
        CustomURLClassLoader customURLClassLoader = (CustomURLClassLoader) processEngineConfiguration.getClassLoader();
        if (customURLClassLoader != null) {
            URL[] leftURLs = new URL[customURLClassLoader.getURLs().length];
            int i = 0;
            for (URL url : customURLClassLoader.getURLs()) {
                if (url != null && !url.getPath().endsWith(unloadingFilePath)) {
                    leftURLs[i] = url;
                    i++;
                }
            }
            CustomURLClassLoader newCustomURLClassLoader = new CustomURLClassLoader(leftURLs.length == 0 ? new URL[]{} : leftURLs);
            processEngineConfiguration.setClassLoader(newCustomURLClassLoader);
        }
        if (unloadingFile.exists()) {
            if (log.isDebugEnabled()) {
                log.debug("deleting jar file:{}-{}", programPackage.getId(), unloadingFilePath);
            }
            unloadingFile.delete();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("no jar file to delete:{}-{}", programPackage.getId(), unloadingFilePath);
            }
        }
    }

    private ProgramPackage validateAndGet(IdParam param) {
        ProgramPackage programPackage = programService.getPackage(param);
        if (programPackage == null) {
            throw new RTException(6650);
        }
        return programPackage;
    }
}
