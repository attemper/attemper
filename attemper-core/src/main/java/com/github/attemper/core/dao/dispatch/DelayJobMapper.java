package com.github.attemper.core.dao.dispatch;

import com.github.attemper.common.base.BaseMapper;
import com.github.attemper.common.result.dispatch.delay.DelayJob;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DelayJobMapper extends BaseMapper<DelayJob> {

}
