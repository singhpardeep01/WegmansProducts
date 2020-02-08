package com.example.wegmansproducts;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkClient {

    private Socket sock;
    private ObjectInputStream networkIn;
    private ObjectOutputStream networkOut;
    private String user;
    private boolean go;

    /**
     * checks if the connection is ready to go
     *
     * @return if everything is good to go
     */
    private synchronized boolean goodToGo() {
        return this.go;
    }

    /**
     * changes status of program being ready to go
     */
    private synchronized void stop() {
        this.go = false;
    }

    /**
     * constructor
     *
     * @param host host to be connected to
     * @param port the port on host ip to connect to
     * @param user the username to use to connect
     */
    public NetworkClient(String host, int port, String user) {
        try {
            this.sock = new Socket(host, port);
            this.networkOut = new ObjectOutputStream(sock.getOutputStream());
            networkOut.flush();
            this.networkIn = new ObjectInputStream(sock.getInputStream());
            this.user = user;
            Request<String> loginAttempt = new Request<>(Request.RequestType.LOGIN, user);
            Exchange.send(loginAttempt, networkOut);
            System.out.println("Attempting to Login");
            Request<?> request = Exchange.read(networkIn);
            if (request.getType() == Request.RequestType.LOGIN_SUCCESS) {
                System.out.println("SUCCESSFUL LOGIN");
            } else if (request.getType() == Request.RequestType.ERROR) {
                System.out.println("ERROR: " + request.getData());
                close();
                System.exit(0);
            }

        } catch (UnknownHostException e) {
            stop();
            this.close();
        } catch (IOException e) {
            stop();
            this.close();
        }
    }

    /**
     * creates a thread that constantly listens for new changes from server
     */
    public void listen() {
        Thread netThread = new Thread(() -> this.run());
        netThread.start();
    }

    /**
     * listens to the server for tile changes to make to the model
     */
    private void run() {
        try {
            while (this.goodToGo()) {
                Request<?> request = Exchange.read(networkIn);
//                if (request.getType() == Request.RequestType.TILE_CHANGED) {
//                    this.changeTile((PlaceTile) request.getData());
//                }
//                if (request.getType() == PlaceRequest.RequestType.ERROR){
//                    model.setStatus(ClientModel.Status.ERROR, (String) request.getData());
//                }
            }
        } catch (IOException e) {
            stop();
            close();
        }
    }

    public void send(int command){
        if (command == 1) {
            Request<String> loginAttempt = new Request<>(Request.RequestType.FAN_POWER, user);
            try{
                Exchange.send(loginAttempt, networkOut);
            }catch(Exception e){
                System.out.println(e.getStackTrace().toString());
            }
        }
    }

    /**
     * closes the connection
     */
    public void close() {
        try {
            this.sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}