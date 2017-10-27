package com.rd.ifaes.core.risk.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.resource.RiskResource;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.risk.domain.PapersScore;
import com.rd.ifaes.core.risk.mapper.PapersScoreMapper;
import com.rd.ifaes.core.risk.service.PapersScoreService;

/**
 * 试卷等级业务类
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月6日
 */
@Service("papersScoreService")
public class PapersScoreServiceImpl extends
		BaseServiceImpl<PapersScoreMapper, PapersScore> implements
		PapersScoreService {

	/**
	 * 插入数据
	 */
	@Override
	@Transactional(readOnly = false)
	public void insert(final PapersScore entity) {
		BeanValidators.validate(entity);// 校验
		TokenUtils.validShiroToken(TokenConstants.TOKEN_PAPERSSCORE_ADD);
		entity.preInsert();
		dao.insert(entity);
	}

	/**
	 * 根据试卷ID查询等级设置
	 */
	@Override
	public List<PapersScore> findPapersScoreByPapersId(final String papersId) {
		final List<PapersScore> psList = dao.findPapersScoreByPapersId(papersId);
		if (CollectionUtils.isEmpty(psList)) {
			throw new BussinessException(ResourceUtils.get(RiskResource.RISK_PAPERS_LEVEL_IS_EXISTS),BussinessException.TYPE_CLOSE);
		}
		return psList;
	}

	/**
	 * 根据对象修改对象
	 */
	@Override
	@Transactional(readOnly = false)
	public int updatePapersScore(final PapersScore papersScore) {
		return dao.update(papersScore);
	}

	/**
	 * 插入对象
	 */
	@Override
	@Transactional(readOnly = false)
	public int insertPapersScore(final PapersScore papersScore) {
		return dao.insert(papersScore);
	}

	/**
	 * 批量插入
	 */
	@Override
	@Transactional(readOnly = false)
	public int insertBatchPapersScores(final List<PapersScore> list) {
		return dao.insertBatch(list);
	}

}