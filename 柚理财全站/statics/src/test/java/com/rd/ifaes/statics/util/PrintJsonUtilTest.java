package com.rd.ifaes.statics.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RequestContextHolder.class})
public class PrintJsonUtilTest {
	
	@Test
	public void testPrintSuccessJson() throws IOException {
		IMocksControl control = EasyMock.createControl();
		
		int count = 2;
		final PrintWriter write = control.createMock(PrintWriter.class);
		write.print(EasyMock.anyString());
		EasyMock.expectLastCall().times(count);
		write.close();
		EasyMock.expectLastCall().times(count);
		
		HttpServletResponse response = new HttpServletResponse() {
			
			@Override
			public void setLocale(Locale loc) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setContentType(String type) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setContentLengthLong(long len) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setContentLength(int len) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setCharacterEncoding(String charset) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setBufferSize(int size) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void resetBuffer() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isCommitted() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public PrintWriter getWriter() throws IOException {
				// TODO Auto-generated method stub
				return write;
			}
			
			@Override
			public ServletOutputStream getOutputStream() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Locale getLocale() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getContentType() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getCharacterEncoding() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getBufferSize() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public void flushBuffer() throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setStatus(int sc, String sm) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setStatus(int sc) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setIntHeader(String name, int value) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setHeader(String name, String value) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setDateHeader(String name, long date) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void sendRedirect(String location) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void sendError(int sc, String msg) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void sendError(int sc) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public int getStatus() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Collection<String> getHeaders(String name) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Collection<String> getHeaderNames() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getHeader(String name) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String encodeUrl(String url) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String encodeURL(String url) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String encodeRedirectUrl(String url) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String encodeRedirectURL(String url) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean containsHeader(String name) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void addIntHeader(String name, int value) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addHeader(String name, String value) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addDateHeader(String name, long date) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addCookie(Cookie cookie) {
				// TODO Auto-generated method stub
				
			}
		};
		
		ServletRequestAttributes requestAttr = new ServletRequestAttributes(request,response);
		control.replay();
		
		PowerMock.mockStatic(RequestContextHolder.class);
		EasyMock.expect(RequestContextHolder.getRequestAttributes()).andReturn(requestAttr).times(2);
		PowerMock.replay(RequestContextHolder.class);
		
		//验证
		PrintJsonUtil.printSuccessJson(null,null,null);
		PrintJsonUtil.printSuccessJson(null,"123","123");
		control.verify();
	}
	
	HttpServletRequest request = new HttpServletRequest() {
		
		@Override
		public AsyncContext startAsync(ServletRequest servletRequest,
				ServletResponse servletResponse) throws IllegalStateException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public AsyncContext startAsync() throws IllegalStateException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void setCharacterEncoding(String env)
				throws UnsupportedEncodingException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void setAttribute(String name, Object o) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void removeAttribute(String name) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean isSecure() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean isAsyncSupported() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean isAsyncStarted() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public ServletContext getServletContext() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getServerPort() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public String getServerName() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getScheme() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public RequestDispatcher getRequestDispatcher(String path) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getRemotePort() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public String getRemoteHost() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getRemoteAddr() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getRealPath(String path) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public BufferedReader getReader() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getProtocol() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String[] getParameterValues(String name) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Enumeration<String> getParameterNames() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Map<String, String[]> getParameterMap() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getParameter(String name) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Enumeration<Locale> getLocales() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Locale getLocale() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getLocalPort() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public String getLocalName() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getLocalAddr() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public ServletInputStream getInputStream() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public DispatcherType getDispatcherType() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getContentType() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public long getContentLengthLong() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public int getContentLength() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public String getCharacterEncoding() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Enumeration<String> getAttributeNames() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Object getAttribute(String name) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public AsyncContext getAsyncContext() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass)
				throws IOException, ServletException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void logout() throws ServletException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void login(String username, String password) throws ServletException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean isUserInRole(String role) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean isRequestedSessionIdValid() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean isRequestedSessionIdFromUrl() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean isRequestedSessionIdFromURL() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean isRequestedSessionIdFromCookie() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public Principal getUserPrincipal() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public HttpSession getSession(boolean create) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public HttpSession getSession() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getServletPath() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getRequestedSessionId() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public StringBuffer getRequestURL() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getRequestURI() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getRemoteUser() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getQueryString() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getPathTranslated() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getPathInfo() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Collection<Part> getParts() throws IOException, ServletException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Part getPart(String name) throws IOException, ServletException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getMethod() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getIntHeader(String name) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public Enumeration<String> getHeaders(String name) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Enumeration<String> getHeaderNames() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getHeader(String name) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public long getDateHeader(String name) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public Cookie[] getCookies() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getContextPath() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String getAuthType() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public String changeSessionId() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public boolean authenticate(HttpServletResponse response)
				throws IOException, ServletException {
			// TODO Auto-generated method stub
			return false;
		}
	};
	

	@Test
	public void testBuildJSON(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(null, "1");
		params.put(null, "");
		params.put("2", "");
		params.put("3", "3");
		PrintJsonUtil.buildJSON(params);
	}
	
	@Test
	public void testPrint() throws IOException{
		PrintWriter write = EasyMock.createMock(PrintWriter.class);
		write.print(EasyMock.anyString());
		EasyMock.expectLastCall();
		write.close();
		EasyMock.expectLastCall();
		EasyMock.replay(write);
		
		HttpServletResponse res = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.expect(res.getWriter()).andThrow(new IOException("msg"));
		EasyMock.expect(res.getWriter()).andReturn(write);
		EasyMock.replay(res);
		//测试
		PrintJsonUtil.print(res, "123");
		PrintJsonUtil.print(res, "123");
		//验证
		EasyMock.verify(write);
		EasyMock.verify(res);
	}
}
