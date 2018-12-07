package org.ono.support.spring;

import org.ono.utils.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by ono on 2018/11/19.
 */
public class ConfigBeanDefinitionParser implements BeanDefinitionParser {


    private Class<?> clazz;

    private String simpleName = "";

    public ConfigBeanDefinitionParser(Class<?> clazz) {
        this.clazz = clazz;
        simpleName = StringUtils.toLowerCaseFirstChar(clazz.getSimpleName());
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(clazz);
        rootBeanDefinition.setLazyInit(false);
        String id = element.getAttribute("id");
        if (!StringUtils.isBlank(id)){
            if (parserContext.getRegistry().containsBeanDefinition(id)){
                throw new IllegalStateException("Dulicate spring bean id " + id);
            }
            rootBeanDefinition.getPropertyValues().addPropertyValue("id", id);
        }else {
            id  = simpleName;
            int count = 0;
            while (parserContext.getRegistry().containsBeanDefinition(id)){
                id += (count++);
            }
        }

        parserContext.getRegistry().registerBeanDefinition(id, rootBeanDefinition);

        for (Method setter: clazz.getMethods()){
            String name = setter.getName();
            if (name.length() > 3 && name.startsWith("set") &&
                    Modifier.isPublic(setter.getModifiers()) && 1 == setter.getParameterTypes().length){
                name = name.substring(3, 4).toLowerCase() + name.substring(4);
                String value = element.getAttribute(name);
                if (!StringUtils.isBlank(value)){
                    rootBeanDefinition.getPropertyValues().add(name,value);
                }
            }
        }

        return rootBeanDefinition;

    }
}
