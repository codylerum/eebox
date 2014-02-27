package com.outjected.eebox;

import java.util.UUID;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@ViewScoped
@Named
public class ViewScopedBackingBean {

  private Logger log;

  private UUID uuid;

  private String foo = "Default";

  @PostConstruct
  private void postCostruct() {
    log = Logger.getLogger(ViewScopedBackingBean.class.getName());
    uuid = UUID.randomUUID();
    log.info(String.format("Created new View Scoped Backing Bean: %s", uuid.toString()));
  }

  @PreDestroy
  private void preDestroy() {
    log.info(String.format("Destroying View Scoped Backing Bean: %s", uuid.toString()));
  }

  public void submitValue() {
    log.info(String.format("Set Value: %s", foo));
  }

  public void immediate() {
    log.info(String.format("Didn't Submit"));
  }

  public String submitAndNavigate() {
    return "vs.xhtml&faces-redirect=true";
  }

  public String getFoo() {
    return foo;
  }

  public void setFoo(String foo) {
    this.foo = foo;
  }
}
