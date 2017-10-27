package com.rd.ifaes.core.risk.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.RiskEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.resource.RiskResource;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.domain.PapersScore;
import com.rd.ifaes.core.risk.mapper.LevelConfigMapper;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.risk.service.PapersScoreService;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserCacheService;

/**
 * ServiceImpl:LevelConfigServiceImpl
 * 
 * @author QianPengZhan
 * @version 3.0
 * @date 2016-7-12
 */
@Service("levelConfigService")
public class LevelConfigServiceImpl extends
		BaseServiceImpl<LevelConfigMapper, LevelConfig> implements
		LevelConfigService {
	/**
	 * papersScoreService业务
	 */
	@Resource
	private transient PapersScoreService papersScoreService;
	/**
	 * ProjectService业务
	 */
	@Resource
	private transient ProjectService projectService;
	/**
	 * UserCacheService	业务
	 */
	@Resource
	private transient UserCacheService userCacheService;
	/**
	 * 批量删除
	 */
	@Override
	@Transactional(readOnly=false)
	public void deleteBatch(final String[] ids) {
		if (ids == null || ids.length == 0) {
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_SELECTED_DELETE_ERROR),BussinessException.TYPE_CLOSE);
		}
		List<UserCache> list = new ArrayList<UserCache>();
		for (int i = Constant.INT_ZERO; i < ids.length; i++) {
			final String id = ids[i];
			//若对应风险等级下已有产品，则提示“已有对应风险等级的产品，不允许删除”
			final LevelConfig levelConfig = dao.get(id);
			final int num = projectService.getProjectCount(levelConfig.getRiskLevelVal().toString());
			if(num > Constant.INT_ZERO){
				throw new BussinessException(ResourceUtils.get(RiskResource.RISK_CONFIG_IN_PROJECT_IS_YES),BussinessException.TYPE_CLOSE);
			}
			//若没有对应产品则可成功删除，已评测为对应风险承受能力的用户自动降级；
			final UserCache model = new UserCache();
			model.setRiskLevel(levelConfig.getUserRiskLevelVal().toString());
			final List<UserCache>  userCacheList = userCacheService.findList(model);
			final List<LevelConfig> configList = dao.findLevelListByOrder(CommonEnum.NO.getValue());
			Integer minConfig = Constant.INT_ZERO;
			if(CollectionUtils.isNotEmpty(configList)){
				minConfig = configList.get(Constant.INT_ZERO).getUserRiskLevelVal();
			}
			if(CollectionUtils.isNotEmpty(userCacheList)){
				for(final UserCache userCache:userCacheList){
					Integer riskLelve = levelConfig.getUserRiskLevelVal()-Constant.INT_ONE;
					if(riskLelve < Constant.INT_ZERO){
						riskLelve = minConfig;
					}
					UserCache newUserCache = new UserCache();
					newUserCache.setUuid(userCache.getUuid());
					newUserCache.setRiskLevel(riskLelve.toString());
					CacheUtils.del(CacheConstant.KEY_PREFIX_USER_CACHE_USER_ID +userCache.getUserId());
					list.add(newUserCache);
				}
			}
		}
		if(CollectionUtils.isNotEmpty(list)){
			userCacheService.updateBatch(list);
		}
		
		dao.deleteBatch(ids);
	}
	
	/**
	 * 添加  修改的校验
	 * @author QianPengZhan
	 * @date 2016年10月21日
	 * @param entity
	 */
	private void validEntity(final LevelConfig entity){
		int flag = dao.isExistsConfig(entity.getRiskLevelVal().toString(), entity.getUuid());
		if(flag > Constant.INT_ZERO){
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_CONFIG_RISKVAL_ISEXISTS),BussinessException.TYPE_CLOSE);
		}
		if(entity.getRiskLevelVal()-entity.getUserRiskLevelVal() != Constant.INT_ONE){
			entity.setUserRiskLevelVal(entity.getRiskLevelVal()-Constant.INT_ONE);
		}
	}
	/**
	 * 插入数据
	 */
	@Override
	@Transactional(readOnly = false)
	public void insert(final LevelConfig entity) {
		BeanValidators.validate(entity);// 校验
		entity.preInsert();
		validEntity(entity);
		dao.insert(entity);
	}

	/**
	 * 根据试卷ID查询列表
	 */
	@Override
	@Transactional(readOnly = false)
	public List<LevelConfig> findByPapersId(final String papersId) {
		final List<PapersScore> psList = papersScoreService.findPapersScoreByPapersId(papersId);
		List<LevelConfig> lList = dao.findAll();
		final List<String> uuids = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(lList)) {
			for (int i = 0; i < psList.size(); i++) {
				final PapersScore papersScore = psList.get(i);
				uuids.add(papersScore.getRiskId());
			}
			lList = dao.findNoClude(CommonEnum.NO.getValue(), uuids);
		}
		if (CollectionUtils.isNotEmpty(lList)) {
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_LEVEL_ALREADY_SET_ERROR),BussinessException.TYPE_JSON);
		}
		return lList;
	}

	/**
	 * 查询数据列表，如果需要分页，请设置分页对象，如：model.setPage(new Page());
	 */
	@Override
	public List<LevelConfig> findList(LevelConfig levelConfig) {
		if(levelConfig == null){
			levelConfig = new LevelConfig();
		}
		levelConfig.setDeleteFlag(Integer.parseInt(RiskEnum.CONFIG_IS_NOT_DELETE_FLAG.getValue()));
		return super.findList(levelConfig);
	}

	/**
	 * findListByPapersId:(根据试卷查询该试卷设定的等级).
	 */
	@Override
	public List<LevelConfig> findListByPapersId(final String papersId) {
		return dao.findLevelListByPapersId(CommonEnum.NO.getValue(), papersId);
	}

	/**
	 * 按照类型顺序正序查询风险类型
	 */
	@Override
	public List<LevelConfig> findByOrder() {
		return dao.findLevelListByOrder(CommonEnum.NO.getValue());
	}

	/**
	 * 根据用户风险等级值查询记录
	 */
	@Override
	public LevelConfig findListByUserRiskLevelVal(final int userRiskLevelVal) {
		final LevelConfig model = new LevelConfig();
		model.setUserRiskLevelVal(userRiskLevelVal);
		final List<LevelConfig> list = findList(model);
		LevelConfig config = null;
		if (!list.isEmpty()) {
			config = list.get(Constant.INT_ZERO);
		}
		return config;
	}

	/**
	 * 加入缓存
	 */
	@Override
	@Cacheable(key = CacheConstant.KEY_PREFIX_LEVEL_CONFIG, expire = ExpireTime.FIVE_MIN)
	public LevelConfig findByRiskLevel(final String riskLevel) {
		LevelConfig levelConfig = new LevelConfig();
		levelConfig.setRiskLevelVal(Integer.parseInt(riskLevel));
		final List<LevelConfig> list = dao.findList(levelConfig);
		if (CollectionUtils.isNotEmpty(list)) {
			levelConfig = list.get(Constant.INT_ZERO);
		}
		return levelConfig;
	}

	@Override
	public LevelConfig getMaxConfig() {
		return dao.getMaxConfig();
	}

	@Override
	public void update(LevelConfig entity) {
		BeanValidators.validate(entity);
		validEntity(entity);
		super.update(entity);
	}
	
	
	
	

}