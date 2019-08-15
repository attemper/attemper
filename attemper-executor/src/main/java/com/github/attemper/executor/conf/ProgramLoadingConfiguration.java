package com.github.attemper.executor.conf;

import com.github.attemper.common.exception.RTException;
import com.github.attemper.common.result.app.program.ProgramPackage;
import com.github.attemper.core.service.application.ProgramService;
import com.github.attemper.executor.util.CustomURLClassLoader;
import com.github.attemper.sys.util.FileUtil;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Component
public class ProgramLoadingConfiguration {

    @Autowired
    private ProgramService programService;

    @Autowired
    private ProcessEngineConfigurationImpl processEngineConfiguration;

    @PostConstruct
    public void load() {
        List<ProgramPackage> programPackages = programService.listLoadedPackage(null);
        CustomURLClassLoader customURLClassLoader = null;
        for (ProgramPackage programPackage : programPackages) {
            String userFolderPath = FileUtil.joinUserFolder(programPackage.getId());
            File jarFile = programService.writeJarFile(userFolderPath, programPackage);
            URL url;
            try {
                url = jarFile.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RTException(2004, e);
            }
            if (customURLClassLoader == null) {
                customURLClassLoader = new CustomURLClassLoader(new URL[]{url});
            } else {
                customURLClassLoader.uploadURL(url);
            }
        }
        processEngineConfiguration.setClassLoader(customURLClassLoader);
    }
}
