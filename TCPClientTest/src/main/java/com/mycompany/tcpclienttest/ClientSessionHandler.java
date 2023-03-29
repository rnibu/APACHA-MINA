/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tcpclienttest;
  
  import org.apache.mina.core.service.IoHandler;
  import org.apache.mina.core.service.IoHandlerAdapter;
  import org.apache.mina.core.session.IoSession;
  import org.apache.mina.example.sumup.message.AddMessage;
 import org.apache.mina.example.sumup.message.ResultMessage;
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  
  /**
31   * {@link IoHandler} for SumUp client.
32   *
33   * @author <a href="http://mina.apache.org">Apache MINA Project</a>
34   */
  public class ClientSessionHandler extends IoHandlerAdapter {
  
      private final static Logger LOGGER = LoggerFactory.getLogger(ClientSessionHandler.class);
      
      private final int[] values;
  
      private boolean finished;
  
      public ClientSessionHandler(int[] values) {
          this.values = values;
      }
  
      public boolean isFinished() {
          return finished;
      }
  
      @Override
      public void sessionOpened(IoSession session) {
          // send summation requests
          for (int i = 0; i < values.length; i++) {
              AddMessage m = new AddMessage();
              m.setSequence(i);
              m.setValue(values[i]);
             session.write(m);
          }
      }
  
      @Override
      public void messageReceived(IoSession session, Object message) {
         // server only sends ResultMessage. otherwise, we will have to identify
          // its type using instanceof operator.
          ResultMessage rm = (ResultMessage) message;
          if (rm.isOk()) {
              // server returned OK code.
             // if received the result message which has the last sequence
              // number,
              // it is time to disconnect.
              if (rm.getSequence() == values.length - 1) {
                  // print the sum and disconnect.
                  LOGGER.info("The sum: " + rm.getValue());
                  session.closeNow();
                  finished = true;
              }
          } else {
              // seever returned error code because of overflow, etc.
              LOGGER.warn("Server error, disconnecting...");
              session.closeNow();
              finished = true;
          }
      }
  
      @Override
      public void exceptionCaught(IoSession session, Throwable cause) {
          session.closeNow();
      }
  }
