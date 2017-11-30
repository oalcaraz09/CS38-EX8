

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Oscar Alcaraz
 * 
 *  CS 380 Networks
 *  Exercise 8
 *
 */

public class WebServer {

    public static void main(String[] args) {
    	
        System.out.println("\nServer Started: Port 8080");
        
        try {
        	
            ServerSocket socket = new ServerSocket( 8080 );

            while(true) {
            	
                Socket listener = socket.accept();
                httpRequest(listener);
                listener.close();
            }

        } catch (Exception e) { }
    }
 
    
    public static void httpRequest(Socket socket) throws Exception {
    	
        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String request = "";
        String filePath = "";

        while((request = br.readLine()) != null) {
        	
            if(request.startsWith("GET")) {
                String[] split = request.split(" ");
                filePath = split[1];
                httpResponse(socket, filePath);

                break;
            }
            
        }
    }
    
    
    public static void httpResponse(Socket socket, String filePath) throws Exception {

        File file = new File("www" + filePath );

        if(file.exists()) {
        	
            System.out.println("\n\n\t" + file.getAbsolutePath());
            loadPage(socket, file, "200 OK");
            
        } else {
        	
            System.out.println("\n\n\tDOES NOT EXIST: " + filePath);
            File notFound = new File("www/fileNotFound.html");
            loadPage(socket, notFound, "404 Not Found");
        }

    }


    public static void loadPage(Socket socket, File file, String status) throws Exception {

        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        BufferedReader br = new BufferedReader(new FileReader(file));

        pw.println("HTTP/1.1 " + status);
        pw.println("Content-type: text/html");
        pw.println("Content-length: " + file.length());
        pw.println("\r\n");

        for(String line; (line = br.readLine()) != null; ) {
        	
            pw.println(line);
        }
        
        pw.flush();
        System.out.println("\tResponse Sent: " + status);
    }

}