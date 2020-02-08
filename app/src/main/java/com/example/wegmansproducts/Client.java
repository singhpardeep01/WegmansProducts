package com.example.wegmansproducts;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Client{
    private NetworkClient serverConn;
    private String user;
    private Scanner userIn;
    private PrintWriter userOut;

    /**
     * initializes the GUI
     * @throws Exception
     */
    public Client(String[] params){
        serverConn = new NetworkClient(params[0],Integer.parseInt(params[1]),params[2]);
        this.user = params[2];
    }
    public void listen(){
        serverConn.listen();
    }
    public synchronized void go(Scanner consoleIn, PrintWriter consoleOut) {
        this.userIn = consoleIn;
        this.userOut = consoleOut;
        this.refresh();
    }

    /**
     * closes all the connections
     */
    public void stop() {
        this.userIn.close();
        this.userOut.close();
        this.serverConn.close();
    }

    /**
     * refreshes console repeatedly for user input
     */
    private void refresh() {
        while (true) {
            this.userOut.println("Command");
            this.userOut.flush();
            int row = this.userIn.nextInt();
            if (row == -1) {
                this.stop();
                System.exit(0);
            }
            if (row == 1) {
                this.userOut.println(this.userIn.nextLine());
                this.serverConn.send(row);
            }
        }
    }
    public void run() {
        // We don't put the PrintWriter in try-with-resources because
        // we don't want it to be closed. The Scanner can close.
        PrintWriter out = null;
        try ( Scanner consoleIn = new Scanner( System.in ) ) {
            do {
                try {
                    out = new PrintWriter(
                            new OutputStreamWriter( System.out ), true );
                    go( consoleIn, out );
                    out = null;
                }
                catch( Exception e ) {
                    e.printStackTrace();
                    if ( out != null ) {
                        out.println( "\nRESTARTING...\n" );
                    }
                }
            } while ( out != null );
        }
    }
    /**
     * the main function launches the application
     * @param args the input
     */
    public static void main( String[] args ) {
        Client client = new Client(args);
        client.listen();
        client.run();
    }

}