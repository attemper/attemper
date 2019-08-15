package com.github.attemper.config.base.service;

import com.github.attemper.config.base.annotation.MultiDataSource;
import com.github.attemper.config.base.dao.ApiLogMapper;
import com.github.attemper.config.base.entity.ApiLog;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @auth ldang
 */
@Service
public class ApiLogService {

	@Autowired
	private IdGenerator idGenerator;

	@Autowired
	private ApiLogMapper mapper;
	
	@Transactional
	@Async
	@MultiDataSource
	public void save(ApiLog apiLog){
		if (StringUtils.length(apiLog.getParam()) > 2000) {
			apiLog.setParam(apiLog.getParam().substring(0, 2000));
		}
		if (StringUtils.length(apiLog.getResult()) > 2000) {
			apiLog.setResult(apiLog.getResult().substring(0, 2000));
		}
		if (StringUtils.length(apiLog.getMsg()) > 2000) {
			apiLog.setMsg(apiLog.getMsg().substring(0, 2000));
		}
		apiLog.setId(idGenerator.getNextId());
		mapper.add(apiLog);
	}
}
