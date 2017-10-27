package com.rd.account.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.account.domain.AccountLog;
import com.rd.account.mapper.AccountLogMapper;
import com.rd.account.service.AccountLogService;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseServiceImpl;

/**
 * ServiceImpl:AccountLogServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-6-14
 */
@Service("accountLogService") 
public class AccountLogServiceImpl  extends BaseServiceImpl<AccountLogMapper, AccountLog> implements AccountLogService{

	/**
	 * 资金日志处理类
	 */
	@Resource
	private transient AccountLogMapper accountLogMapper;

    /**
     * 保存记录
     * @author xhf
     * @date 2016年8月29日
     * @param entity
     */
    @Override
    @Transactional(readOnly = false)
    public void save(final AccountLog entity) {
    	entity.preInsert();
        accountLogMapper.insert(entity);
    }

    /**
     * 后台查询记录
     * @author xhf
     * @date 2016年8月29日
     * @param entity
     * @return
     */
	@Override
	public Page<AccountLog> findManagePage(final AccountLog entity) {
		Page<AccountLog> page = entity.getPage();
		if(page==null){
			page=new Page<AccountLog>();
		}
		page.setRows(dao.findManageList(entity));
		return page;
	}
	
	/**
	 * 根据日期查询记录
	 * @author xhf
	 * @date 2016年8月29日
	 * @param log
	 * @return
	 */
	@Override
	public Page<AccountLog> findByDate(final AccountLog log) {
		Page<AccountLog> page = log.getPage();
		if(page==null){
			page=new Page<AccountLog>();
		}
		page.setRows(dao.findByDate(log));
		return page;
	}
	
	/**
	 * 根据日期查询记录
	 * @author xhf
	 * @date 2016年8月29日
	 * @param log
	 * @return
	 */
	@Override
	public List<AccountLog> findListByDate(final AccountLog log) {
		return dao.findByDate(log);
	}

	/**
	 * 根据条件查询所有记录
	 * @author xhf
	 * @date 2016年8月29日
	 * @param model
	 * @return
	 */
	@Override
	public List<AccountLog> findManageList(final AccountLog model) {
		model.setPage(null);
		return dao.findManageList(model);
	}
	
	@Override
	public List<AccountLog> findManageListExportExcel(AccountLog model) {
		return dao.findManageList(model);
	}
	
	/**
	 * 查看昨日收益列表
	 * @author xhf
	 * @date 2016年8月29日
	 * @param model
	 * @return
	 */
	@Override
	public List<AccountLog> yesterdayEarnAmount(final AccountLog model){
		return dao.yesterdayEarnAmount(model);
	}

	@Override
	public List<String> findListForScanner(AccountLog model) {
		return dao.findListForScanner(model);
	}

	
	@Override
	public int getManageCount(AccountLog model) {
		return dao.getManageCount(model);
	}
	
}