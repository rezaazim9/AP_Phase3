package model;

import com.fasterxml.jackson.core.JsonEncoding;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

}
