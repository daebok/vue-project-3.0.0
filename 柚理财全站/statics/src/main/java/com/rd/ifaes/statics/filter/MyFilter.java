package com.rd.ifaes.statics.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
/**
 * 
 * @author lh
 * @version 3.0
 * @since 2016-6-24
 *
 */
public class MyFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String url = request.getRequestURI();
		
		if("/imageUp.jsp".equals(url)){
			filterChain.doFilter(request, response);
		}else{
			super.doFilter(request, response, filterChain);
		}
	}
}
