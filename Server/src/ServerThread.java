import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread implements Closeable{
    private Server server;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String IP;
    private String user;

    /**
     * constructor
     * @param connection connection from client
     * @param server the server which this is a client thread for
     */
    public ServerThread (Socket connection, Server server){
        try{
            this.server = server;
            this.socket = connection;
            this.IP = socket.getRemoteSocketAddress().toString();
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * connects to the client and sends a successful login and board or an error with why connection
     * was refused
     * @return whether connection was made
     */
    public boolean connect(){
        try {
            Request<?> request = Exchange.read(in);
            if (request.getType() == Request.RequestType.LOGIN) {
                String reply = server.addUser((String) request.getData(), out, IP);
                if (reply.equals(request.getData())) {
                    Request<String> login = new Request<>(Request.RequestType.LOGIN_SUCCESS, (String) request.getData());
                    Exchange.send(login, out);
                    user = (String) request.getData();
                    return true;
                }
                else {
                    Request<String> loginFail = new Request<>(Request.RequestType.ERROR, reply);
                    Exchange.send(loginFail, out);
                    return false;
                }
            }
            else {
                Request<String> loginFail = new Request<>(Request.RequestType.ERROR, "INCORRECT CONNECTION ATTEMPT");
                Exchange.send(loginFail, out);
                return false;
            }
        } catch (IOException e) {
            this.close();
            return false;
        }
    }

    /**
     * checks for tile changes from client once connection is established
     */
    public void run(){
        if (connect()) {
            while (true) {
                try {
                    Request<?> request = Exchange.read(in);
                    server.fanControls(request);
                } catch (IOException e) {
                    this.close();
                    server.removeUser(user, IP);
                    break;
                }
            }
        }
        else{
            this.close();
        }
    }

    /**
     * closes the connection if an error arises
     */
    @Override
    public void close() {
        try {
            this.socket.close();
        }catch (IOException e) {

        }
    }
}