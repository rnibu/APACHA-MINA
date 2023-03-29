/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.tcpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.mina.core.buffer.IoBuffer;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.example.sumup.message.AddMessage;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * An TCP server used for performance tests.
 * 
 * It does nothing fancy, except receiving the messages, and counting the number of
 * received messages.
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class TcpServer extends IoHandlerAdapter {
    /** The listening port (check that it's not already in use) */
    public static final int PORT = 9000;

    /** The number of message to receive */
    public static final int MAX_RECEIVED = 100000;

    /** The starting point, set when we receive the first message */
    private static long t0;

    /** A counter incremented for every received message */
    private AtomicInteger nbReceived = new AtomicInteger(0);

    /**
     * {@inheritDoc}
     */
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        session.close(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        

        int nb = nbReceived.incrementAndGet();
        //String m = session.getReadBytes()
        System.out.println("message received");
        //System.out.println(m);
        //System.out.println(session.getLastWriteTime());
        String close;
        IoBuffer buffer = (IoBuffer) message;
            System.out.println(buffer.getString(Charset.forName("UTF-8").newDecoder()));
            
            close = buffer.getString(Charset.forName("UTF-8").newDecoder());
//            if(close.equals("+ACK:GTRTO,000000,866884045645652,L101Class3,REBOOT,000B,20220729194206,0001$")){
//            	session.close(true);
//            }
            //buffer.putObject(new pojo.POJO());
            buffer.setAutoExpand(true);
            buffer.clear();
//            String command = "AT+GTRTO=AIR11,10,,,,5,,,,,,,,,000B$";
//            buffer.put(command.getBytes(StandardCharsets.UTF_8));
//            buffer.flip();
//            //wbuffer.
//            session.write(buffer);
//            System.out.println(buffer);
        
//        if (nb == 1) {
//            t0 = System.currentTimeMillis();
//        }
//
//        if (nb == MAX_RECEIVED) {
//            long t1 = System.currentTimeMillis();
//            System.out.println("-------------> end " + (t1 - t0));
//        }
//
//        if (nb % 10000 == 0) {
//            System.out.println("Received " + nb + " messages");
//        }

        // If we want to test the write operation, uncomment this line
        //session.write(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("Session closed...");

        // Reinitialize the counter and expose the number of received messages
        System.out.println("Nb message received : " + nbReceived.get());
        nbReceived.set(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("Session created...");
        //Response response = new DefaultResponse(220, "Service ready for new user.");
        //session.write(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("Session idle...");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("Session Opened...");
        IoBuffer buffer = IoBuffer.allocate(2);
        buffer.setAutoExpand(true);
        //String command = "AT+GTRTO=AIR11,10,,,,1,,,,,,,,,000B$";
        String command = "AT+GTRTO=AIR11,B,,,,,,,,,,,,,000B$";
        //String command = "messaged recieved you are ready for lift off";
            buffer.put(command.getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            //wbuffer.
            session.write(buffer);
        
//        AddMessage message = new AddMessage();
//        message.setValue(21);
        
    }

    /**
     * Create the TCP server
     */
    public TcpServer() throws IOException {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setHandler(this);

        // The logger, if needed. Commented atm
        //DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        //chain.addLast("logger", new LoggingFilter());

        acceptor.bind(new InetSocketAddress(PORT));

        System.out.println("Server started...");
    }

    /**
     * The entry point.
     */
    public static void main(String[] args) throws IOException {
        new TcpServer();
    }
}
