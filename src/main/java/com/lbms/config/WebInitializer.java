package com.lbms.config;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class WebInitializer implements WebApplicationInitializer {

	
	public void onStartup(ServletContext container) throws ServletException {
		// 创建 Spring root application Context
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		DelegatingFilterProxy shiroFilter=new DelegatingFilterProxy();
		shiroFilter.setTargetFilterLifecycle(true);
		rootContext.register(ServiceConfig.class, DaoConfig.class,ShiroConfig.class);
		container.addFilter("shiroFilter", shiroFilter).addMappingForUrlPatterns(null, false, "/*");
		container.addFilter("/", new CharacterEncodingFilter("UTF-8"));
		container.addListener(new ContextLoaderListener(rootContext));
		rootContext.setServletContext(container);
		
		
		
		// 管理Spring root application Context生命周期
		AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
		dispatcherContext.register(MvcConfig.class);
		Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
		servlet.setInitParameter("contextConfigLocation", "com.lbms.config");
		servlet.addMapping("/");
		servlet.setLoadOnStartup(1);
	}

}