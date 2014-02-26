package com.outjected.eebox;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.jboss.logging.Logger;

@ViewScoped
@Named
public class ViewScopedBackingBean {

  private Logger log;

  private String foo = "Default";

  @PostConstruct
  private void postCostruct() {
    log = Logger.getLogger(ViewScopedBackingBean.class);
    log.infof("Created new View Scoped Backing Bean");
  }

  public void submitValue() {
    log.infof("Set Value: %s", foo);
  }

  public void immediate() {
    log.infof("Didn't Submit");
  }

  public String getFoo() {
    return foo;
  }

  public void setFoo(String foo) {
    this.foo = foo;
  }
}
