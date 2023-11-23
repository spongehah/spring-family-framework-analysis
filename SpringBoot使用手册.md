# SpringBoot使用手册

> author：spongehah	https://blog.hahhome.top/

[TOC]

# 1 容器功能

## 1.1 组件添加

### 1 @Configuration

用法：加在类上，告诉SpringBoot这是一个配置类 == 配置文件

属性：@Configuration(**proxyBeanMethods** = false)

> - 1、配置类里面使用@Bean标注在方法上给容器注册组件，默认也是单实例的
>
> - 2、配置类本身也是组件
>
> - 3、proxyBeanMethods：代理bean的方法，**默认为true**
>   - Full(proxyBeanMethods = **true**)、【保证每个@Bean方法被调用多少次返回的组件都是**单实例**的】
>   
>   - Lite(proxyBeanMethods = **false**)【每个@Bean方法被调用多少次返回的组件都是**新创建**的】
>
>   - 组件依赖必须使用Full模式默认。其他默认是否Lite模式
>   
>     - 配置类组件之间**无依赖**关系用Lite模式**加速容器启动过程，减少判断**
>   
>     - 配置类组件之间**有依赖**关系，方法会被调用得到之前单实例组件，用Full模式

### 2 @Import

用法：@Import({Xxx.class, Xxx.class}) //加在类上，给容器中自动创建出导入的全部类型的组件、默认组件的名字就是全类名

### 3 @Bean、@Component、@Controller、@Service、@Repository

都是用于声明Bean的注解，其中@Bean是加在方法上的，其它四个是加在类上的

@Bean注解修饰的方法还需要在@Configuration修饰的类下，才能被注册到容器中

### 4 @ConditionalOnXxx

包含@ConditionalOnBean、@ConditionalOnMissingBean...

条件装配：满足Conditional指定的条件，则进行组件注入

## 1.2 原生配置文件引入

### 1 @ImportResource

用法：@ImportResource("classpath:beans.xml") //加在配置类上，可以引入Spring等的原生配置文件，可以兼容只使用Spring的应用，避免手动将xml配置换为注解

## 1.3 配置绑定

### 1 @ConfigurationProperties

用法：

1. @EnableConfigurationProperties(Xxx.class)：加在配置类上
   	1、开启Xxx配置绑定功能
   	2、把这个Xxx这个组件自动注册到容器中
   @ConfigurationProperties(prefix = "xxx")：加在需要进行配置绑定的类Xxx上
2. @Component //只有在容器中的组件，才会拥有SpringBoot提供的强大功能
   @ConfigurationProperties(prefix = "xxx") //都加在需要进行配置绑定的类Xxx上

作用：可以在application.properties或application.yml配置文件中以xxx为前缀，定义改组件的属性，并通过以上注解进行绑定，封装成一个Bean

```xml
    <!-- 自定义的前缀在配置文件中配置时没有提示，加入这个依赖就可以有提示了 -->
	<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
    </dependency>


	<build>
        <plugins>
            <plugin>
                <!-- maven打包插件 -->
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <!-- 打包时忽略configuration-processor -->
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-configuration-processor</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

## 1.4 自动装配原理

在笔记[《Spring源码解读》](https://blog.hahhome.top/blog/Spring%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB)中



# 2 静态资源访问

## 2.1 静态资源路径目录

**默认情况**下，将静态资源放在以下目录下，就能使用 **当前项目根路径/ + 静态资源名** 访问到对应的静态资源：

1. /static
2. /public
3. /resources
4. /META-INF/resources

> 这些目录可以是在resource目录下(即classpath)，也可以是在jar包(依赖引入的或打包的)下的这些目录下



修改默认的静态资源路径：

```yaml
spring:
  resources:
    static-locations: [classpath:/haha/]
    add-mappings: false   # 设置为false禁用所有静态资源规则，默认未true
```



> **原理：**根据SpringMVC处理请求，会先查询有无对应映射Controller，如果没有就交给静态资源处理器，看能否处理，若不能则返回404，若能静态资源处理器会先判断有无禁用所有静态资源规则，若未禁用，则①先判断是否是 `/webjars/**` 的映射规则，若是则匹配路径"classpath:/META-INF/resources/webjars/"下的资源；②再判断默认的 `/**` 的映射规则，去匹配默认的4个静态资源路径 /static 、 /public、 /resources、 /META-INF/resources 下的资源

## 2.2 静态资源访问前缀

**默认无前缀**，静态资源映射为/**

修改静态资源访问前缀：

```yaml
spring:
  mvc:
    static-path-pattern: /res/**
```

## 2.3 webjars

自动映射/webjars/**，如引入jquery依赖后，就有jquery的jar包，jar包下有目录/META-INF/resources/webjars/... ，所以访问路径 /webjars/xxx 时会自动映射到上述目录下查找

> 其实还是符合2.1的默认规则，在/META-INF/resources目录下

## 2.4 欢迎页和favicon

静态资源路径目录下放 index.html 和 favicon.ico

可以配置静态资源路径，但是不可以配置静态资源的访问前缀。否则导致 index.html 和 favicon.ico 不能被默认访问
欢迎页不能设置访问前缀是因为源码中写死了访问映射必须是 /** 



# 3 请求处理

## 3.1 请求映射

### 1 Rest表单提交：HiddenHttpMethodFilter

当我们使用**表单提交**时，由于表单中method只能写get和post两种请求，所以当我们想要发送put、delete、patch请求时：

1. 需要设置表单：method=post，隐藏域 _method=put/delete/patch
2. 配置文件手动开启HiddenHttpMethodFilter：

```yaml
spring:
  mvc:
    hiddenmethod:
      filter:
        enabled: true   #开启页面表单的Rest功能
```

> **原理：**当HiddenHttpMethodFilter拦截到**post请求**时，会获取到获取到**_method**的值，若获取到值并且是PUT、DELETE、PATCH三种请求的话，就使用包装模式requestWrapper重写了getMethod方法，**将请求的method修改为对应的_method的值**，以后的方法调用getMethod是**调用requestWrapper的重写的getMethod方法**



当使用**客户端**（如PostMan）时，会直接发送Put、delete等方式请求，无需Filter



修改隐藏域_method的名称：

```java
@Configuration
public class WebConfig {
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        hiddenHttpMethodFilter.setMethodParam("_m");
        return hiddenHttpMethodFilter;
    }
}
```



### 2 请求映射原理 

在笔记[《Spring源码解读》](https://blog.hahhome.top/blog/Spring%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB)中



## 3.2 获取请求参数

### 1 @RequestParam

示例：

访问url：localhost:8080/hello?id=1&age=18

```java
@GetMapping("/hello")
public Map<String, Object> hello(@RequestParam("id") Integer id, 
                                 @RequestParam Map<String, String> map) {
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("id", id);
    resultMap.put("map", map);
    return resultMap; 
}
```

### 2 @PathVarible

示例：

访问url：localhost:8080/hello/1/18

```java
@GetMapping("/hello/{id}/{age}")
public Map<String, Object> hello2(@PathVariable("id") Integer id,
                                  @PathVariable Map<String, String> map) {
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("id", id);
    resultMap.put("map", map);
    return resultMap;
}
```

### 3 @RequestBody

适用于表单提交

示例：

访问url：localhost:8080/hello?id=1&age=18	method=POST

```java
@PostMapping("/hello5")
public Map<String, Object> hello3(@RequestBody String content) {
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("content", content);
    return resultMap;
}
```

或是将@RequestBody的内容封装到某个具体的实体类当中

### 4 @RequestHeader

示例：

访问url：localhost:8080/hello4

```java
@GetMapping("/hello3")
public Map<String, Object> hello4(@RequestHeader("User-Agent") String userAgent,
                                  @RequestHeader Map<String, String> map) {
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("userAgent", userAgent);
    resultMap.put("map", map);
    return resultMap;
}
```

### 5 @CookieValue

示例：

访问url：localhost:8080/hello5

```java
@GetMapping("/hello4")
public Map<String, Object> hello5(@CookieValue("b-user-id") String buserid,
                                  @CookieValue("b-user-id") Cookie cookie) {
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("b-user-id", buserid);
    resultMap.put("cookie", cookie.getName() + cookie.getValue());
    return resultMap;
}
```

###  6 @RequestAttribute

示例：

访问url：localhost:8080/goto

```java
@Controller
public class RequestAttributeController {
    @GetMapping("/goto")
    public String gotoPage(HttpServletRequest request) {
        request.setAttribute("msg", "成功了。。。");
        return "forward:/success";
    }
    
    @ResponseBody
    @GetMapping("/success")
    public Map success(@RequestAttribute("msg") String msg, 
                       HttpServletRequest request) {
        Object msg1 = request.getAttribute("msg");
        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        map.put("msg1", msg1);
        return map;
    }
}
```

### 7 @MatrixVariable

矩阵变量

示例：

访问url：
localhost:8080/cars/sell;low=34;brand=byd,audi,yd
localhost:8080/boss/1;age=20/2;age=22

```java
@RestController
public class MatrixVariableController {
    //语法1： /cars/sell;low=34;brand=byd,audi,yd
    @GetMapping("/cars/{sell}")
    public Map carsSell(@MatrixVariable("low") Integer low,
                        @MatrixVariable("brand")List<String> brand) {
        Map<String, Object> map = new HashMap<>();
        map.put("low", low);
        map.put("brand", brand);
        return map;
    }

    //语法2： /boss/1;age=20/2;age=22
    @GetMapping("/boss/{bossId}/{empId}")
    public Map boss(@MatrixVariable(value = "age", pathVar = "bossId") Integer bossAge,
                    @MatrixVariable(value = "age", pathVar = "empId") Integer empAge) {
        Map<String, Object> map = new HashMap<>();
        map.put("bossAge", bossAge);
        map.put("empAge", empAge);
        return map;
    }
}

@Configuration
public class WebConfig implements WebMvcConfigurer {
    //开启矩阵变量方法1：WebConfig 实现 WebMvcConfigurer
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        //设置为不移除分号(;)后面的内容，即开启矩阵变量
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }
    
    //开启矩阵变量方法2：WebConfig 不实现 WebMvcConfigurer
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {
                UrlPathHelper urlPathHelper = new UrlPathHelper();
                //设置为不移除分号(;)后面的内容，即开启矩阵变量
                urlPathHelper.setRemoveSemicolonContent(false);
                configurer.setUrlPathHelper(urlPathHelper);
            }
        };
    }
}
```

### 8 复杂类型参数

例如HttpServletRequest、**Model、Map**、RedirectAttributes、ServletResponse...等等等等

其中Model 和 Map 类型的数据，若是向里面放值，**最终都会被保存到requestAttribute请求域中**

> 为什么？原理：在笔记[《Spring源码解读》](https://blog.hahhome.top/blog/Spring%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB)中

### 9 自定义对象类型参数

如自定义JavaBean 类型：Person、User等

> 自定义对象的参数解析原理：在笔记[《Spring源码解读》](https://blog.hahhome.top/blog/Spring%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB)中

### 10 自定义Converter

converter：用于将传入的请求中的参数，封装到形参的JavaBean中

自定义converter：在 9自定义对象类型参数 的基础上，可以将传入的 "小黑,3" 转换为Pet对象

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, Pet>() {
            @Override
            public Pet convert(String source) {
                // 小黑,3
                if (!StringUtils.isEmpty(source)) {
                    Pet pet = new Pet();
                    String[] split = source.split(",");
                    pet.setName(split[0]);
                    pet.setAge(split[1]);
                    return pet;
                }
                return null;
            }
        });
    }
}
```

### 11 参数处理原理

在笔记[《Spring源码解读》](https://blog.hahhome.top/blog/Spring%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB)中



# 4 响应处理

## 4.1 数据响应原理

在笔记[《Spring源码解读》](https://blog.hahhome.top/blog/Spring%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB)中

## 4.2 内容协商

内容协商即告诉服务器自己能够接收什么格式的数据

### 1 内容协商策略

**默认**情况下是**1 基于请求头的内容协商**，只能通过接收请求头中的Accept字段进行最佳匹配

但是使用浏览器时，我们无法修改请求头，于是可以**手动开启2 基于请求参数的内容协商功能**：

```yaml
spring:
    contentnegotiation:
      favor-parameter: true  #开启请求参数内容协商模式
```

请求格式：localhost:8080/goto?**format=xml**

在format中指定格式

要使用xml，还需要引入xml场景：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```



> 内容协商**原理**：在笔记[《Spring源码解读》](https://blog.hahhome.top/blog/Spring%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB)中

### 2 自定义MessageConverter

注意区分 MessageConverter 和 Converter 的区别

MessageConverter：用于写出数据，将指定的方法返回类型(JavaBean类型或简单数据类型)写出为指定格式的数据(如json、xml等)
Converter：用于读取数据，将传入的请求中的参数，封装到形参的JavaBean中

当我们需要自定义一种媒体格式MediaType(返回格式，常见有xml和json)时，
我们需要自定义一个MessageConverter，
然后添加配置注册该自定义MessageConverter，但是现在只能用于基于请求头的内容协商
如果还要使用基于参数的内容协商的话，还需要配置基于参数的内容协商策略支持的新MediaType：

```java
//自定义MessageConverter
public class CustomMessageConverter implements HttpMessageConverter<Person> {
    @Override
    public boolean canRead(Class<?> aClass, MediaType mediaType) {
        return false;
    }

    //写出场景
    @Override
    public boolean canWrite(Class<?> aClass, MediaType mediaType) {
        return aClass.isAssignableFrom(Person.class);
    }

    /**
     * 告诉服务器该MessageConverter能够写出的MediaType
     * 如json格式就是 application/json
     */
    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return MediaType.parseMediaTypes("application/x-hah");
    }

    @Override
    public Person read(Class<? extends Person> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    public void write(Person person, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        //自定义写出的格式
        String data = person.getUserName() + ";" + person.getAge();
        //写出
        OutputStream body = httpOutputMessage.getBody();
        body.write(data.getBytes());
    }
}

//添加配置
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 注册自定义MessageConverter
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new CustomMessageConverter());
    }

    // 配置基于参数的内容协商策略支持的新MediaType
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        HeaderContentNegotiationStrategy headerStrategy = new HeaderContentNegotiationStrategy();
        Map<String, MediaType> mediaTypes = new HashMap<>();
        mediaTypes.put("json", MediaType.APPLICATION_JSON);
        mediaTypes.put("xml", MediaType.APPLICATION_XML);
        // 上面是原来的功能，下面是新增的功能
        mediaTypes.put("hh", MediaType.parseMediaType("application/x-hah"));
        ParameterContentNegotiationStrategy parameterStrategy = new ParameterContentNegotiationStrategy(mediaTypes);
        configurer.strategies(Arrays.asList(headerStrategy, parameterStrategy));
    }
}
```





























# 附录

## 找常见依赖写法

![image-20231121193330985](image/SpringBoot使用手册.assets/image-20231121193330985.png)

在pom.xml中的parent，按住CTRL点击<artifactId>，然后搜索想要引入的依赖，就能找到对应的<dependency>的写法了



## 找配置文件前缀

方法1：

<img src="image/SpringBoot使用手册.assets/image-20231121194349118.png" alt="image-20231121194349118" style="zoom: 67%;" /><img src="image/SpringBoot使用手册.assets/image-20231121194557943.png" alt="image-20231121194557943" style="zoom: 50%;" />

在Maven管理的jar包中找到想要修改配置的xxxAutoConfiguration的源码，然后找到@EnableConfigurationProperties注解绑定的xxxProperties，就可以看到对应的配置prefix是什么了



方法2：在IDEA中直接CTRL+N，搜索xxxAutoConfiguration的源码，然后像方法1一样找到前缀

> 补充：在META-INF下可以找到spring.factories，里面有SpringBoot启动时自动加载的组件













