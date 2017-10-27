package com.rd.ifaes.mobile.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rd.ifaes.common.util.JsonMapper;


/**
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper.
 *
 */
public class MobileJsonMapper extends JsonMapper {
	public MobileJsonMapper() {
		super(Include.ALWAYS);
		 }
	}