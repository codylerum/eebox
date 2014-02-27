package com.outjected.eebox.scopes;

public class ViewScopeEventListener implements ViewMapListener {

  // Actions --------------------------------------------------------------------------------------------------------

  /**
   * Returns <code>true</code> if given source is an instance of {@link UIViewRoot}.
   */
  @Override
  public boolean isListenerForSource(Object source) {
          return (source instanceof UIViewRoot);
  }

  /**
   * If the event is an instance of {@link PreDestroyViewMapEvent}, which means that the JSF view scope is about to
   * be destroyed, then find the current instance of {@link ViewScopeProvider} and invoke its
   * {@link ViewScopeProvider#preDestroyView()} method.
   */
  @Override
  public void processEvent(SystemEvent event) throws AbortProcessingException {
          if (event instanceof PreDestroyViewMapEvent) {
                  ViewScopeProvider instance = BeanManager.INSTANCE.getReference(ViewScopeProvider.class);

                  if (instance != null) { // May be null when CDI isn't supported on this environment.
                          instance.preDestroyView();
                  }
          }
  }

}


