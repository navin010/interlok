package com.adaptris.core.common;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.adaptris.core.util.Args;
import com.adaptris.interlok.config.DataInputParameter;
import com.adaptris.interlok.config.DataOutputParameter;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("standard-execution")
public class Execution {
  
  @NotNull
  @Valid
  private DataInputParameter<String> source;
  
  @NotNull
  @Valid
  private DataOutputParameter<String> target;
  
  public Execution() {
    
  }

  public Execution(DataInputParameter<String> src, DataOutputParameter<String> t) {
    this();
    setSource(src);
    setTarget(t);
  }

  public DataInputParameter<String> getSource() {
    return source;
  }

  public void setSource(DataInputParameter<String> sourceXpathExpression) {
    this.source = Args.notNull(sourceXpathExpression, "src");
  }

  public DataOutputParameter<String> getTarget() {
    return target;
  }

  public void setTarget(DataOutputParameter<String> targetDataDestination) {
    this.target = Args.notNull(targetDataDestination, "target");
  }

}