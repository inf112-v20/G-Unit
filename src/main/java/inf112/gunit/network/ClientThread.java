package inf112.gunit.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.JsonSerialization;
import com.esotericsoftware.kryonet.Listener;
import inf112.gunit.player.card.MovementCard;

public class ClientThread implements Runnable {
    public final static String HOST = "127.0.0.1";
    public final static int TIMEOUT = 1000;
    public final static int WRITE_BUFFER = 256 * 1024;
    public final static int READ_BUFFER = 256 * 1024;
    public final static int PORT_TCP = 56555;
    public final static int PORT_UDP = 56777;

    private Client client;
    private Listener listener;

    @Override
    public void run() {
        listener = new ClientListener();

        client = new Client(WRITE_BUFFER, READ_BUFFER, new JsonSerialization());
        client.start();
        client.addListener(listener);
        try {
            client.connect(TIMEOUT, HOST, PORT_TCP, PORT_UDP);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                client.sendUDP(new Pong());
            }
        }, 1000, 1000);
    }

    class ClientListener extends Listener {
        @Override
        public void received(Connection connection, Object object) {
            System.out.println("Server response: " + object);
        }
    }
}
