package dev.appkr.demo.tcp.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class MyObjectFactory extends BasePooledObjectFactory<MyObject> {


  @Override
  public MyObject create() throws Exception {
    return new MyObject();
  }

  @Override
  public PooledObject<MyObject> wrap(MyObject obj) {
    return new DefaultPooledObject<>(obj);
  }
}
