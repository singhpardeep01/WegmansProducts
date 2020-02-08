package com.example.wegmansproducts;

import java.io.Serializable;

public class Request<E extends Serializable> implements Serializable {
    public enum RequestType {
        ERROR,
        LOGIN,
        LOGIN_SUCCESS,
        FAN_POWER
    }

    /** The request type */
    private RequestType type;
    /** The data associated with the request */
    private E data;

    /**
     * Create a new request.
     *
     * @param type request type
     * @param data the data
     */
    public Request(RequestType type, E data) {
        this.type = type;
        this.data = data;
    }

    /**
     * Get the type of request.
     *
     * @return request type
     */
    public RequestType getType() { return type; }

    /**
     * Get the data associated with the request.
     *
     * @return the data
     */
    public E getData() { return data; }

    /**
     * Utility method for debugging only.
     *
     * @return the tile as a string
     */
    @Override
    public String toString() {
        return "PlaceRequest{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}
