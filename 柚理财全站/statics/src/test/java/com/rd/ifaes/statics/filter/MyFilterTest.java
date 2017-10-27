package com.rd.ifaes.statics.filter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Test;

public class MyFilterTest {
	@Test
	public void testdoFilterInternalFirst() throws Exception {
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		FilterChain filterChain = EasyMock.createMock(FilterChain.class);
		EasyMock.expect(request.getRequestURI()).andReturn("/imageUp.jsp");
		
		filterChain.doFilter(EasyMock.anyObject(HttpServletRequest.class), EasyMock.anyObject(HttpServletResponse.class));
		EasyMock.expectLastCall();
		
		EasyMock.replay(request);
		EasyMock.replay(filterChain);
		EasyMock.replay(response);
		
		MyFilter filter = new MyFilter();
		
		filter.doFilterInternal(request, response, filterChain);
		
		EasyMock.verify(request);
		EasyMock.verify(filterChain);
		EasyMock.verify(response);
	}
	
	@Test
	public void testdoFilterInternalSecond() throws Exception {
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		FilterChain filterChain = EasyMock.createMock(FilterChain.class);
		EasyMock.expect(request.getRequestURI()).andReturn("sdds");
		EasyMock.expect(request.getAttribute(EasyMock.anyString())).andReturn("sdds");
		
		filterChain.doFilter(EasyMock.anyObject(HttpServletRequest.class), EasyMock.anyObject(HttpServletResponse.class));
		EasyMock.expectLastCall();
		
		EasyMock.replay(request);
		EasyMock.replay(filterChain);
		EasyMock.replay(response);
		
		MyFilter filter = new MyFilter();
		
		filter.doFilterInternal(request, response, filterChain);
		
		EasyMock.verify(request);
		EasyMock.verify(filterChain);
		EasyMock.verify(response);
	}
}
