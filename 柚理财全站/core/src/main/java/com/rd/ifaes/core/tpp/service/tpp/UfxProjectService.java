package com.rd.ifaes.core.tpp.service.tpp;

import com.rd.ifaes.core.tpp.model.ufx.UfxProjectApplyModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxProjectAttachInfoModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxProjectUpdateModel;

/**
 * 项目管理服务接口
 * @author lh
 * @version 3.0
 * @since 2016-7-6
 */
public interface UfxProjectService {
	
	/**
	 * 发布项目
	 * @param uuid
	 * @return
	 */
	UfxProjectApplyModel tppProjectApply(String uuid);
	
	
	/**
	 * 项目信息更新
	 * @param uuid
	 * @param projectStatus 项目状态<br>
	 * 0：开标 1：投标中 2：还款中
	 * 3：已还款 4：结束 -1：流标
	 * @return
	 */
	UfxProjectUpdateModel tppProjectUpdate(String uuid, String projectStatus);
	
	/**
	 * 撤销项目信息
	 * @param uuid
	 */
	void tppProjectRevoke(String uuid);
	
	/**
	 * 补录项目信息
	 * @param uuid
	 * @return
	 */
	UfxProjectAttachInfoModel tppProjectAttachInfo(String uuid);

}
