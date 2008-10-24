package modelet.core;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;

public class ApBeanFactory {

	private static final String CONTEXT_PATH = "config/spring/core.xml";
	
	private static ApplicationContext applicationContext;
	
	public static ApplicationContext getApplicationContext() {

		if (applicationContext == null) {
			//usage 1
			applicationContext = new GenericApplicationContext();
			XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader((GenericApplicationContext)applicationContext);
			xmlReader.loadBeanDefinitions(new FileSystemResource(CONTEXT_PATH));
			((GenericApplicationContext)applicationContext).refresh();
			//usage 2
			//applicationContext = new FileSystemXmlApplicationContext(contextPath);
			//usage 3
			//applicationContext = new ClassPathXmlApplicationContext("config/spring/core.xml");
		}
		return applicationContext;
	}

	public <T> T getBean(String id) {
		return (T) getApplicationContext().getBean(id);
	}
}
