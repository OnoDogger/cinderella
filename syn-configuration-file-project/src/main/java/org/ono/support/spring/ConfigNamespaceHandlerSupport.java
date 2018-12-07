package org.ono.support.spring;

import org.ono.services.impl.FileObjects;
import org.ono.services.impl.Reporter;
import org.ono.services.impl.Storage;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by ono on 2018/11/19.
 */
public class ConfigNamespaceHandlerSupport extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("files", new ConfigBeanDefinitionParser(FileObjects.class));
        registerBeanDefinitionParser("storage", new ConfigBeanDefinitionParser(Storage.class));
        registerBeanDefinitionParser("reporter", new ConfigBeanDefinitionParser(Reporter.class));
    }
}
