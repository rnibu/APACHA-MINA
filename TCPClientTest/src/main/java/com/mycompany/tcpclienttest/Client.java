  package com.mycompany.tcpclienttest;
  
  import java.net.InetSocketAddress;
  
  import org.apache.mina.core.RuntimeIoException;
  import org.apache.mina.core.future.ConnectFuture;
  import org.apache.mina.core.session.IoSession;
  import org.apache.mina.example.sumup.ClientSessionHandler;
  import org.apache.mina.example.sumup.codec.SumUpProtocolCodecFactory;
  import org.apache.mina.filter.codec.ProtocolCodecFilter;
  import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
  import org.apache.mina.filter.logging.LoggingFilter;
  import org.apache.mina.transport.socket.nio.NioSocketConnector;
  
  /**
   * (<strong>Entry Point</strong>) Starts SumUp client.
35   *
36   * @author <a href="http://mina.apache.org">Apache MINA Project</a>
37   */
  public class Client {
      private static final String HOSTNAME = "00000"; //server ip
  
      private static final int PORT = 0000;
  
      private static final long CONNECT_TIMEOUT = 30*1000L; // 30 seconds
  
      // Set this to false to use object serialization instead of custom codec.
      private static final boolean USE_CUSTOM_CODEC = true;
      static int [] values = {1,2,3};
  
      public static void main(String[] args) throws Throwable {
          
    NioSocketConnector connector = new NioSocketConnector();
    connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);

    if (USE_CUSTOM_CODEC) {
       connector.getFilterChain().addLast("codec",
        new ProtocolCodecFilter(new SumUpProtocolCodecFactory(false)));
    } else {
        connector.getFilterChain().addLast("codec",
            new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
    }
    
    connector.getFilterChain().addLast("logger", new LoggingFilter());
    connector.setHandler(new ClientSessionHandler(values));
    IoSession session;
    
    for (;;) {
        try {
            ConnectFuture future = connector.connect(new InetSocketAddress(HOSTNAME, PORT));
            future.awaitUninterruptibly();
            session = future.getSession();
            break;
        } catch (RuntimeIoException e) {
            System.err.println("Failed to connect.");
            e.printStackTrace();
            Thread.sleep(5000);
        }
    }
        
    // wait until the summation is done
    session.getCloseFuture().awaitUninterruptibly();
    connector.dispose();
}
  }