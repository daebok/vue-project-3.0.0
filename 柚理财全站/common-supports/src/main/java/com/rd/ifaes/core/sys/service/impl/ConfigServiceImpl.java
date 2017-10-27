package com.rd.ifaes.core.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.annotation.CacheEvict;
import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.sys.domain.Config;
import com.rd.ifaes.core.sys.mapper.ConfigMapper;
import com.rd.ifaes.core.sys.service.ConfigService;

/**
 * ServiceImpl:ConfigServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-5-17
 */
@Service("configService") 
public class ConfigServiceImpl  extends BaseServiceImpl<ConfigMapper, Config> implements ConfigService{

    @Override
    @Cacheable(key = CacheConstant.KEY_PREFIX_CONFIG_CODE+"{code}", expire=ExpireTime.NONE)
    public Config findByCode(String code) {
    	return getByCode(code);
    }
    
    @Override
    @Transactional
    public void update(Config entity) {
    	if(StringUtils.isNotBlank(entity.getCode())){
    		CacheUtils.del(CacheConstant.KEY_PREFIX_CONFIG_CODE.concat(entity.getCode()));
    	}
    	super.update(entity);
    }
    
    @Override
	@CacheEvict(keys = CacheConstant.KEY_PREFIX_CONFIG_CODE+"{code}")
    public int updateValueByCode(String code, String value) {
    	Config config = getByCode(code);
    	if(config != null){
    		config.setConfigValue(value);
    		return dao.update(config);
    	}    	
    	return 0;
    }
    
    @Override
    public int checkCode(Config config) {
    	return dao.checkCode(config);
    }
    
    private Config getByCode(String code){
    	return dao.getByCode(code);
    }

}