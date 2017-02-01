#spring-java-config

这是一个使用spring中Javaconfig进行项目配置的一个工程，目的是为了熟悉spring零xml配置过程，并在此进行记录，以方便日后使用。

##配置
从spring3.0以后，spring就开始提供javaconfig配置，也就是零XML配置。在这个工程工程中，我们加入了持久层使用的是mybatis框架，下面的介绍中，我只截取部分代码进行介绍。
###1、spring-mybaties配置
**jdbc.properties文件**

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/passbox
jdbc.username=user
jdbc.password=****
```

**DataSourceConfig**

DataSourceConfig类，主要是配置dataSource Bean

```java

@Configuration
@PropertySource({"classpath:/properties/jdbc.properties"})
public class DataSourceConfig {

    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Value("${jdbc.driver}")
    String driverClass;
    @Value("${jdbc.url}")
    String url;
    @Value("${jdbc.username}")
    String userName;
    @Value("${jdbc.password}")
    String passWord;


    @Bean(name="dataSource")
    public DataSource dataSource(){

        logger.info("DataSource");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(passWord);
        return dataSource;
    }

}
```

```@PropertySource```引入properties文件，@Value注入配置，```@Bean(name="dataSource")```指这是一个ID为***dataSource***的Bean

**MybatisConfig**

MybatisConfig类，用到了之前的 ***dataSource***这个Bean

```java
@Configuration
@MapperScan("com.yzkj.dao")
public class MybatisConfig {

    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Resource(name = "dataSource")
    public DataSource dataSource;


    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
        logger.info("sqlSessionFactory 加载");
        final SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();

        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setTypeAliasesPackage("com.yzkj.model");
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/mapping/*.xml"));
        return sqlSessionFactory;

    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }
}
```

```@MapperScan("com.yzkj.dao")``` 这里是扫描mapper接口，```@Resource(name = "dataSource")```注入***dataSource***

###2、spring-mvc配置

```
@Configuration
@ComponentScan(basePackages = {"com.yzkj.controller"})
public class AppConfig extends WebMvcConfigurationSupport {

    private static final String MESSAGE_SOURCE = "/WEB-INF/i18n/messages_CN.properties";

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJacksonHttpMessageConverter());
        addDefaultHttpMessageConverters(converters);
    }


    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping requestMappingHandlerMapping = super.requestMappingHandlerMapping();
        requestMappingHandlerMapping.setUseSuffixPatternMatch(false);
        requestMappingHandlerMapping.setUseTrailingSlashMatch(false);
        return requestMappingHandlerMapping;
    }

    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(MESSAGE_SOURCE);
        messageSource.setCacheSeconds(5);
        return messageSource;
    }

    //避免IE执行AJAX时，返回JSON出现下载文件
    @Bean(name = "mappingJacksonHttpMessageConverter")
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();      mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
        return mappingJackson2HttpMessageConverter;
    }


    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource());
        return validator;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
```

```@ComponentScan(basePackages = {"com.yzkj.controller"})```扫描controller

###3、web.xml配置
在这个工程中，没有使用web.xml文件进行配置，采用javaconfig的方式进行配置。

```
@Order(1)
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{DataSourceConfig.class,MybatisConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{AppConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[] {characterEncodingFilter};
    }
}

```

以上就是关于使用javaconfig方式进行工程配置，总体来说，正如开篇所说，确实可以达到零xml配置，但是总体上没有简单多少，原来在xml中配置，在javaconfig中也需要配置。

