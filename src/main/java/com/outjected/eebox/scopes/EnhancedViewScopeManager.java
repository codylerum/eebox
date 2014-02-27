package com.outjected.eebox.scopes;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import java.io.Serializable;

@SessionScoped
public class EnhancedViewScopeManager implements Serializable, EnhancedViewScopeProvider {

  private static final long serialVersionUID = 1L;

  private static final int INTIAL_BEAN_STORAGE_CAPACITY = 10;

  @Inject
  private BeanManager manager;

  private ConcurrentMap<UUID, BeanStorage> activeViewScopes;

  @PostConstruct
  public void postConstructSession() {
    activeViewScopes = new ConcurrentHashMap<>();
  }

  @Override
  public void preDestroyView() {
    BeanStorage storage = activeViewScopes.remove(getBeanStorageId(false));
    if (storage != null) {
      storage.destroyBeans();
    }
  }

  @PreDestroy
  public void preDestroySession() {
    for (BeanStorage storage : activeViewScopes.values()) {
      storage.destroyBeans();
    }
  }

  public <T> T getBean(Contextual<T> type) {
    return activeViewScopes.get(getBeanStorageId(true)).getBean(type, manager);
  }

  public <T> T createBean(Contextual<T> type, CreationalContext<T> context) {
    return activeViewScopes.get(getBeanStorageId(true)).createBean(type, context);
  }

  private UUID getBeanStorageId(boolean create) {
    UUID uuid =
        (UUID) FacesContext.getCurrentInstance().getViewRoot().getViewMap()
            .get(EnhancedViewScopeManager.class.getName());

    if (uuid == null || activeViewScopes.get(uuid) == null) {
      uuid = UUID.randomUUID();

      if (create) {
        activeViewScopes.put(uuid, new BeanStorage(INTIAL_BEAN_STORAGE_CAPACITY));
      }

      FacesContext.getCurrentInstance().getViewRoot().getViewMap()
          .put(EnhancedViewScopeManager.class.getName(), uuid);
    }

    return uuid;
  }
}
