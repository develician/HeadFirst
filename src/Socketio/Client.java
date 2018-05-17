package Socketio;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        String serverIP = "127.0.0.1";
        Socket socket = null;

        DataInputStream dis = null;
        System.out.println("Connecting on the Server. Server IP : " + serverIP);

        try {


            while(true){
                socket = new Socket(serverIP, 3000);
                dis = new DataInputStream(socket.getInputStream());
                System.out.println("Message from the server : " + dis.readUTF());
                if(socket.isClosed()){
                    System.out.println("Close!");
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("연결 종료!");
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }
}
