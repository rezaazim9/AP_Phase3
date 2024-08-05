package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import view.containers.GlassFrame;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static controller.constants.UIMessageConstants.*;

public class TCP {
    Socket socket = new Socket("localhost", 11211);
    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

    public TCP() throws IOException {
    }

    public void sendObject(Object object) throws IOException {
        outputStream.writeObject(object);
    }
    public Object receiveObject() throws IOException, ClassNotFoundException {
        return inputStream.readObject();
    }
    public static void disconnectMessage() {
        JOptionPane.showOptionDialog(GlassFrame.getGlassFrame(), DISCONNECTED_MESSAGE, String.valueOf(DISCONNECTED_TITLE), JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[]{}, null);
    }
    public static void connectedMessage() {
        JOptionPane.showOptionDialog(GlassFrame.getGlassFrame(), CONNECTED_MESSAGE, String.valueOf(CONNECTED_TITLE), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
    }
    public static String JsonMaker(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
       return objectMapper.writeValueAsString(o);
    }
}
