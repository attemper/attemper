package com.github.attemper.config.base.service;

import com.github.attemper.config.base.annotation.MultiDataSource;
import com.github.attemper.config.base.dao.repo.ApiLogRepository;
import com.github.attemper.config.base.entity.ApiLog;
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
	private ApiLogRepository repository;
	
	@Transactional
	@Async
	@MultiDataSource
	public void save(ApiLog apiLog){
		if(apiLog.getParam() != null && apiLog.getParam().length() > 2000) {
			apiLog.setParam(apiLog.getParam().substring(0, 2000));
		}
		if(apiLog.getResult() != null && apiLog.getResult().length() > 2000) {
			apiLog.setResult(apiLog.getResult().substring(0, 2000));
		}
		if(apiLog.getMsg() != null && apiLog.getMsg().length() > 2000) {
			apiLog.setMsg(apiLog.getMsg().substring(0, 2000));
		}
		repository.save(apiLog);
	}
}
