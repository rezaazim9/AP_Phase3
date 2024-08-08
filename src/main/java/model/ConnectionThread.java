package model;

import controller.GameLoop;

import java.io.IOException;

import static model.TCP.*;

public class ConnectionThread extends Thread {
    public static boolean connected;

    @Override
    public void run() {
        TCP tcp;
        while (true) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                new TCP();
                if (!connected) {
                    connectedMessage();
                    connected = true;
                }
            } catch (IOException ex) {
                if (connected) {
                    disconnectMessage();
                    connected = false;
                }
            }
            try {
                tcp = new TCP();
                if (connected && !GameLoop.getINSTANCE().isRunning()) {
                    tcp.sendObject(new Packet(JsonMaker(Profile.getCurrent()), "profile"));
                }
            } catch (IOException ignored) {}
        }
    }
}
