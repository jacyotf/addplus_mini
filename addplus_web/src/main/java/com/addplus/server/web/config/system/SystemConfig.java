package com.addplus.server.web.config.system;

import com.addplus.server.api.model.base.ServiceMap;
import com.addplus.server.api.service.rest.demomodule.HelloService;
import com.addplus.server.api.utils.PackageUtil;
import com.addplus.server.api.utils.security.DecriptUtil;
import com.esotericsoftware.reflectasm.MethodAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by 特大碗拉面 on 2017/10/24 0024.
 */
@Configuration
public class SystemConfig {


    @Value("${service.package.name}")
    private String packageName;


    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader("content-type");
        corsConfiguration.addAllowedHeader("x-requested-with");
        corsConfiguration.addAllowedMethod("OPTIONS");
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedHeader("authorization");
        corsConfiguration.addAllowedHeader("token");
        corsConfiguration.setMaxAge(3600L);
        return corsConfiguration;
    }

    /**
     * 跨域过滤器
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    @Bean
    public Set<Class<?>> getDubboPackageClass() {
        // 获取所有service
        Set<Class<?>> allClasses = new HashSet<Class<?>>();
        for (String tmp : packageName.split(",")) {
            Set<Class<?>> classes = PackageUtil.getClasses(tmp);
            allClasses.addAll(classes);
        }
        return allClasses;
    }

    @Bean
    public ServiceMap getInfos(Set<Class<?>> getDubboPackageClass) {
        Map<String, String> decrtipetMap = new HashMap<>();
        Map<String, Class> serviceClassMap = new HashMap<>();
        Map<String, Parameter[]> serviceParameterMap = new HashMap<>();
        // 生成service所需要的容器环境
        for (Class tmp : getDubboPackageClass) {
            Method[] methods = tmp.getMethods();
            for (Method method : methods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                Parameter[] parameters = method.getParameters();
                serviceParameterMap.put(tmp.getName() + "_" + method.getName(), parameters);
                decrtipetMap.put(tmp.getName() + "_" + method.getName(), DecriptUtil.SHA1(method.getName()).substring(0, 16));
            }
            serviceClassMap.put(tmp.getName(), tmp);
        }
        ServiceMap serviceMap = new ServiceMap();
        serviceMap.setDecrtipetMap(decrtipetMap);
        serviceMap.setServiceClassMap(serviceClassMap);
        serviceMap.setServiceParameterMap(serviceParameterMap);
        return serviceMap;
    }

    @Bean
    public AddplusContainer getAddplusContainer(ServiceMap serviceMap) {
        AddplusContainer addplusContainer = AddplusContainer.newInstance(serviceMap, packageName);
        return addplusContainer;
    }


}
