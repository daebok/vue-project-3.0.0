package com.rd.ifaes.statics.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JsonMapperTest {
	@Test
	public void testOne() throws Exception {
		new JsonMapper(null);
		
		JsonMapper.getInstance();
		JsonMapper.getInstance();
	}
	
	@Test
	public void testTwo() throws Exception {
		JsonMapper mapper = new JsonMapper();
		mapper.getMapper();
		mapper.enableJaxbAnnotation();
		mapper.enableEnumUseToString();
		mapper.toJsonP("ds", null);

		JsonMapper.nonDefaultMapper();
		JsonMapper.nonDefaultMapper();
		
		//toJsonString
		InputStream io = new ByteArrayInputStream(new byte[1]);
		JsonMapper.toJsonString(io);
		JsonMapper.toJsonString("dd");
		
		//fromJsonString
		TestPojo pojo = new TestPojo();
		JsonMapper.fromJsonString("", pojo.getClass());
		JsonMapper.fromJsonString("{\"name\":\"name\",\"age\":11}", pojo.getClass());
		JsonMapper.fromJsonString(io.toString(), pojo.getClass());
		
		mapper.update("{\"name\":\"name\",\"age\":11}", pojo);
		mapper.update("{,\"age\":11}", pojo);
		
		//createCollectionType
		List<TestPojo> list = new ArrayList<TestPojo>();
		//fromJson
		mapper.fromJson("", mapper.createCollectionType(list.getClass(), pojo.getClass()));
		mapper.fromJson("{\"name\":\"name\",\"age\":11}", mapper.createCollectionType(List.class, TestPojo.class));
		
	}
}
