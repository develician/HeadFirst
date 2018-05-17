package Socketio;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Server {
    static DateFormat format = new SimpleDateFormat("[hh:mm:ss]");

    public Server() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(3000);
            System.out.println(getTime() + " 서버가 준비 되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Socket client = null;
        DataOutputStream dos = null;

        while (true) {
            try {
                Scanner s = new Scanner(System.in);
                String message = s.nextLine();
                System.out.println(getTime() + " 서버가 연결을 기다리는중.");

                client = serverSocket.accept();
                System.out.println(getTime() + client.getInetAddress()
                        + " 로 부터 연결 요청이 들어왔습니다. ");
                dos = new DataOutputStream(client.getOutputStream());
                dos.writeUTF(message);
                System.out.println(getTime() + " 데이터를 전송 했습니다.");


            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String getTime() {
        return format.format(new Date());
    }

    public static void main(String[] args){
        new Server();
    }
}
