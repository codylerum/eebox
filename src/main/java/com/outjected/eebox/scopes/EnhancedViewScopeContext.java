package com.outjected.eebox.scopes;

import java.lang.annotation.Annotation;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;

public class EnhancedViewScopeContext implements Context {

  private BeanManager manager;
  private Bean<EnhancedViewScopeManager> bean;
  private EnhancedViewScopeManager enhancedViewScopeManager;

  public EnhancedViewScopeContext(BeanManager manager, Bean<EnhancedViewScopeManager> bean) {
    this.manager = manager;
    this.bean = bean;
  }

  @Override
  public Class<? extends Annotation> getScope() {
    return EnhancedViewScoped.class;
  }

  @Override
  public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
    checkActive();
    T bean = enhancedViewScopeManager.getBean(contextual);
    return (bean != null) ? bean : enhancedViewScopeManager.createBean(contextual,
        creationalContext);
  }

  @Override
  public <T> T get(Contextual<T> contextual) {
    checkActive();
    return enhancedViewScopeManager.getBean(contextual);
  }

  @Override
  public boolean isActive() {
    FacesContext context = FacesContext.getCurrentInstance();
    return context != null && context.getViewRoot() != null && isInitialized();
  }

  private boolean isInitialized() {
    if (enhancedViewScopeManager == null) {
      enhancedViewScopeManager =
          (EnhancedViewScopeManager) manager.getReference(bean, bean.getBeanClass(),
              manager.createCreationalContext(bean));
    }

    return enhancedViewScopeManager != null;
  }

  private void checkActive() throws ContextNotActiveException {
    if (!isActive()) {
      throw new ContextNotActiveException();
    }
  }
}
