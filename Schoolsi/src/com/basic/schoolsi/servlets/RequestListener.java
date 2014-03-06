package com.basic.schoolsi.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class RequestListener implements Filter {

	private static final Logger log = Logger.getLogger(RequestListener.class.getName());
	
	private FilterConfig filterConfig;

	@Override
	public void init(FilterConfig config) throws ServletException {
		filterConfig = config;
	}

	@Override
	public void destroy() {
	}

	private boolean shouldContinueFilterChain(HttpServletRequest request, HttpServletResponse response) {
		String requestUri = request.getRequestURI();
		if (requestUri.endsWith(".css") ||
			requestUri.endsWith(".eot") ||
			requestUri.endsWith(".gif") ||
			requestUri.endsWith(".ico") ||
			requestUri.endsWith(".jpg") ||
			requestUri.endsWith(".js") ||
			requestUri.endsWith(".png") ||
			requestUri.endsWith(".svg") ||
			requestUri.endsWith(".swf") ||
			requestUri.endsWith(".ttf") ||
			requestUri.endsWith(".woff")) {
			return true;
		}

		return false;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		if (shouldContinueFilterChain(request, response)) {
			chain.doFilter(req, resp);
			return;
		}
		chain.doFilter(req, resp);
	}
}
