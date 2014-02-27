package com.outjected.eebox.scopes;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.PassivationCapable;

public class BeanStorage implements Serializable {

  private static final long serialVersionUID = 1L;

  private final ConcurrentMap<String, Bean<?>> beans;

  public BeanStorage(int initialCapacity) {
    beans = new ConcurrentHashMap<String, Bean<?>>(initialCapacity);
  }

  public <T> T createBean(Contextual<T> type, CreationalContext<T> context) {
    Bean<T> bean = new Bean<T>(type, context);
    beans.put(((PassivationCapable) type).getId(), bean);
    return bean.getInstance();
  }

  public <T> T getBean(Contextual<T> type, BeanManager manager) {
    @SuppressWarnings("unchecked")
    Bean<T> bean = (Bean<T>) beans.get(((PassivationCapable) type).getId());

    if (bean == null) {
      return null;
    }

    if (!bean.hasContext()) {
      bean.setContext(type, manager.createCreationalContext(type));
    }

    return bean.getInstance();
  }
  
  public synchronized void destroyBeans() { // Not sure if synchronization is absolutely necessary. Just to be on safe side.
    for (Bean<?> bean : beans.values()) {
            bean.destroy();
    }
    beans.clear();
}

  static class Bean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient Contextual<T> type;
    private transient CreationalContext<T> context;
    private final T instance;

    public Bean(Contextual<T> type, CreationalContext<T> context) {
      setContext(type, context);
      instance = type.create(context);
    }

    public void setContext(Contextual<T> type, CreationalContext<T> context) {
      this.type = type;
      this.context = context;
    }

    public boolean hasContext() {
      return type != null && context != null;
    }

    public T getInstance() {
      return instance;
    }

    public void destroy() {
      if (hasContext()) {
        type.destroy(instance, context);
      }
    }
  }
}
