import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.HashMap;


public class Server implements Closeable{
    private ServerSocket server;
    private HashMap<String, ObjectOutputStream> users;
    private PrintWriter writer;

    /**
     * adds username and user connection to repective hashmaps
     * @param username new username
     * @param out user's ourput stream
     * @param IP user's IP
     * @return string determining status of adding
     */
    public synchronized String addUser(String username, ObjectOutputStream out, String IP){
        if (users.containsKey(username)){
            return "USERNAME INVALID";
        }
        else if ((users.keySet()).size() >= 1000){
            return "MAX CLIENT LIMIT REACHED";
        }
        else{
            IP = IP.split(":")[0];
            users.put(username, out);
            log(username + " connecting from " + IP);
            System.out.println(username + " connecting from " + IP);
            return username;
        }
    }

    /**
     * removes user from both username and connection hashmap
     * @param username username to be removed
     * @param IP IP associated with username to be removed
     */
    public synchronized void removeUser(String username, String IP){
        IP = IP.split(":")[0];
        if (users.containsKey(username)) {
            log(username + " disconnecting from " + IP);
            System.out.println(username + " disconnecting from " + IP);
            users.remove(username);
        }
    }

    /**
     * constructor method
     * @param port port for server to be hosted on
     */
    public Server(int port)  {
        try {
            this.server = new ServerSocket(port);
            this.users = new HashMap<>();
            writer= new PrintWriter("PlaceLog.txt");
            log("Server Running on port: " + port);
            System.out.println("Server Running on port: " + port);
        } catch (IOException e) {
        }
    }

    /**
     * writes to the output file for log of the game
     * @param message message to be written
     */
    public void log(String message){
        writer.println(message);
        writer.flush();
    }
    /**
     * closes the server
     */
    public void close() {
        try {
            writer.close();
            this.server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * method that runs forever creating new client Threads and starting them
     */
    public void run(){
        try{
            while(true){
                ServerThread thread = new ServerThread(server.accept(), this);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * main function starting the server
     * @param args input as determined
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Server port");
            System.exit(1);
        }
        Server server = new Server(Integer.parseInt(args[0]));
        server.run();
    }
}
