package dycs.common.infrastructure.persistence.hibernate;

import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Value("${spring.datasource.driver-class-name}")
	private String DRIVER_CLASS_NAME;
	
	@Value("${spring.datasource.url}")
	private String URL;
	
	@Value("${spring.datasource.username}")
	private String USER_NAME;
	
	@Value("${spring.datasource.password}")
	private String PASSWORD;
	
	@Value("${spring.jpa.properties.hibernate.dialect}")
	private String HIBERNATE_DIALECT;
	
	@Value("${spring.jpa.properties.hibernate.ddl-auto}")
	private String HIBERNATE_DDL_AUTO;
	
	@Value("${spring.jpa.show-sql}")
	private String HIBERNATE_SHOW_SQL;
	
	@Value("${spring.jpa.properties.hibernate.current_session_context_class}")
	private String HIBERNATE_SESSION_CONTEXT;
	
	@Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
	private String HIBERNATE_JDBC_BATCH_SIZE;
	
	@Value("${spring.jpa.properties.hibernate.order_inserts}")
	private String HIBERNATE_ORDER_INSERTS;
	
	@Value("${spring.jpa.properties.hibernate.order_updates}")
	private String HIBERNATE_ORDER_UPDATES;
	
	@Value("${spring.jpa.properties.hibernate.jdbc.batch_versioned_data}")
	private String HIBERNATE_JDBC_VERSIONED_DATA;
	
	@Value("${spring.jpa.properties.hibernate.generate_statistics}")
	private String HIBERNATE_GENERATE_STATICS;
	
	@Bean(name = "dataSource")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setUrl(URL);
        dataSource.setUsername(USER_NAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }
	
	@Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) throws Exception {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMappingLocations(getMappingLocations());
        sessionFactoryBean.setHibernateProperties(getHibernateProperties());
        sessionFactoryBean.afterPropertiesSet();
        SessionFactory sessionFactory = sessionFactoryBean.getObject();
        return sessionFactory;
    }
	
	private Resource[] getMappingLocations() {
		Resource[] resources = null;
		try {
			String hbnXml = "classpath:/hibernate/*.hbm.xml";
			resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(hbnXml);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resources;
	}
	
	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", HIBERNATE_DIALECT);
		properties.put("hibernate.hbm2ddl.auto", HIBERNATE_DDL_AUTO);
		properties.put("hibernate.show_sql", HIBERNATE_SHOW_SQL);
		properties.put("hibernate.current_session_context_class", HIBERNATE_SESSION_CONTEXT);
		properties.put("hibernate.jdbc.batch_size", HIBERNATE_JDBC_BATCH_SIZE);
		properties.put("hibernate.order_inserts", HIBERNATE_ORDER_INSERTS);
		properties.put("hibernate.order_updates", HIBERNATE_ORDER_UPDATES);
		properties.put("hibernate.jdbc.batch_versioned_data", HIBERNATE_JDBC_VERSIONED_DATA);
		properties.put("hibernate.generate_statistics", HIBERNATE_GENERATE_STATICS);
		return properties;
	}
	
	@Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
        return transactionManager;
    }
	
	@SuppressWarnings("unchecked")
	@Bean
	public Jackson2ObjectMapperBuilder configureObjectMapper() {
		return new Jackson2ObjectMapperBuilder().modulesToInstall(Hibernate5Module.class);
	}
}
