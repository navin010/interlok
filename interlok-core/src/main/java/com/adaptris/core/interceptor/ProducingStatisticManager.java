package com.adaptris.core.interceptor;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.core.AdaptrisMarshaller;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.DefaultMessageFactory;
import com.adaptris.core.ProduceException;
import com.adaptris.core.StandaloneProducer;
import com.adaptris.core.XStreamJsonMarshaller;
import com.adaptris.core.util.LifecycleHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("producing-statistic-manager")
@AdapterComponent
@ComponentProfile(summary = "Statistic manager that allows configuration of a standalone producer for each timeslice when they expire.", tag = "interceptor")
public class ProducingStatisticManager extends BaseStatisticManager {

  private StandaloneProducer producer;
  
  private AdaptrisMarshaller marshaller;
  
  public ProducingStatisticManager() {
    marshaller = new XStreamJsonMarshaller();
  }
  
  public ProducingStatisticManager(int maxHistoryCount) {
    super(maxHistoryCount);
  }
  
  @Override
  public void produce(InterceptorStatistic interceptorStatistic) throws ProduceException {
    try {
      String marshalledTimeslice = this.getMarshaller().marshal(interceptorStatistic);
      AdaptrisMessage newMessage = DefaultMessageFactory.getDefaultInstance().newMessage(marshalledTimeslice);
      
      this.getProducer().produce(newMessage);
      
    } catch (CoreException e) {
      log.error("Failed to produce timeslice.", e);
    }
  }
  
  @Override
  public void init() throws CoreException {
    LifecycleHelper.init(this.getProducer());
  }

  @Override
  public void start() throws CoreException {
    LifecycleHelper.start(this.getProducer());
  }

  @Override
  public void stop() {
    LifecycleHelper.stop(this.getProducer());
  }

  @Override
  public void close() {
    LifecycleHelper.close(this.getProducer());
  }

  public StandaloneProducer getProducer() {
    return producer;
  }

  public void setProducer(StandaloneProducer producer) {
    this.producer = producer;
  }

  public AdaptrisMarshaller getMarshaller() {
    return marshaller;
  }

  public void setMarshaller(AdaptrisMarshaller marshaller) {
    this.marshaller = marshaller;
  }

}
