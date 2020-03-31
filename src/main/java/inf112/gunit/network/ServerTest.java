package inf112.gunit.network;

/**
 * Test-class for networking
 */
public class ServerTest {

    public static void main(String[] args) {
        new ServerTest().run();
    }

    private void run() {
        ServerThread server = new ServerThread();
        Thread threadServer = new Thread(server);
        threadServer.setName("Server");
        threadServer.start();

        Thread threadClient = new Thread(new ClientThread());
        threadClient.setName("Client");
        threadClient.setDaemon(true);
        while(!server.isReady()){
            Thread.yield();
        }
        threadClient.start();
    }
}
