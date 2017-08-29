/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class SendMessage {

    private static final Log log = LogFactory.getLog(SendMessage.class);
    String ip = "190.168.1.32"; //190.168.1.32
    int port = 18851;

    public SendMessage() {

    }

    public SendMessage(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void send(String message) {
        try {
            Socket socket = new Socket(ip, port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.write(message.getBytes());
            out.flush();
            out.close();
        } catch (IOException ex) {
            log.error("send message error", ex);
        }
    }
}
