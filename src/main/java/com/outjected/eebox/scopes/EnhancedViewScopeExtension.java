package com.outjected.eebox.scopes;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessManagedBean;

public class EnhancedViewScopeExtension implements Extension {
  private Bean<EnhancedViewScopeManager> bean;

  protected void processManagedBeans(@Observes ProcessManagedBean<EnhancedViewScopeManager> event) {
    bean = event.getBean();
  }

  protected void afterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager manager) {
    event.addContext(new EnhancedViewScopeContext(manager, bean));
  }
}
