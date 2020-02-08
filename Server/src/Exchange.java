import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Exchange {

    /**
     * sends a PlaceRequest over the specifiec output stream
     * @param request Request to be sent out
     * @param out ObjectOutputStream to be sent over
     * @throws IOException
     */
    public static void send(Request<?> request, ObjectOutputStream out) throws IOException {
        try {
            out.writeUnshared(request);
            out.flush();
        } catch (IOException e) {
            throw new IOException();
        }
    }

    /**
     * reads in a Request over an input stream
     * @param in ObjectInputStream to be read
     * @return the Request read in
     * @throws IOException
     */
    public static Request<?> read(ObjectInputStream in) throws IOException {
        Request<?> request;
        try {
            request = (Request<?>) in.readUnshared();
            return request;
        } catch (IOException e) {
            throw new IOException();
        } catch (ClassNotFoundException e) {
            throw new IOException();
        }
    }

}
