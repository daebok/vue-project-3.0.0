package com.rd.ifaes.core.risk.service;

import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.risk.domain.Papers;
import com.rd.ifaes.core.user.domain.User;

/**
 * Service Interface:PapersService
 * @author QianPengZhan
 * @version 3.0
 * @date 2016-7-12
 */
public interface PapersService extends BaseService<Papers>{
	
	/**
	 * 直接执行发布
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param ids
	 * @param publish
	 * @return
	 */
	void doPapersPublish(final String[] ids,final String publish);
	
	/**
	 *  整卷添加试题和答案
	 * @author QianPengZhan
	 * @date 2016年7月27日
	 * @param papers
	 * @param questionContent
	 * @param riskContent
	 * @return
	 */
	 void insertAllPapers(Papers papers,String[] questionContent,String[] riskContent);
	
	
	/**
	 *  整卷修改试题和答案
	 * @author QianPengZhan
	 * @date 2016年7月27日
	 * @param papers
	 * @param questionContent
	 * @param riskContent
	 * @return
	 */
	 void updateAllPapers(Papers papers,String[] questionContent,String[] riskContent);
	
	/**
	 * @Title: findPublishPapers   
	 * @Description: 找到发布了的试卷
	 * @param: @return      
	 * @return: Papers      
	 * @throws
	 */
	Papers findPublishPapers();
	
	
	/**
	 * @Title: doUserRiskPapers   
	 * @Description: 用户评测
	 * @param: @param papers
	 * @param: @param questionContent      
	 * @return: void      
	 * @throws
	 */
	Map<String,Object> doUserRiskPapers(Papers papers,String[] questionContent,User user );
	
	/**
	 * 添加试卷和等级
	 * @param entity
	 * @param scoreConfig
	 * @return
	 */
	void insertPapersAndConfig(Papers entity,String[][] scoreConfig) ;
	
	/**
	 * 修改试卷和等级
	 * @param entity
	 * @param scoreConfig
	 * @return
	 */
	void updatePapersAndConfig(Papers entity,String[][] scoreConfig) ;
	
	/**
	 * modifyStatus:(发布和取消发布). <br/> 
	 * @author QianPengZhan
	 * @param uuid
	 * @param publish
	 * @return
	 */
	int modifyStatus(String uuid,String publish);
}