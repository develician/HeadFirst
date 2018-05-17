package MultiChat;

import com.sun.org.apache.xpath.internal.operations.Mult;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MultiChatServer {
    Map<String, DataOutputStream> clients;

    MultiChatServer() {
        clients = Collections.synchronizedMap(new HashMap<String, DataOutputStream>());

    }

    public void start() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(7777);
            System.out.println("Server started");

            while (true) {
                socket = serverSocket.accept();
                System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속하였습니다.");

                ServerReceiver thread = new ServerReceiver(socket);
                thread.start();


            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            SocketUtil.close(serverSocket);

        }


    }

    void sendToAll(String message) {
        Iterator<String> it = clients.keySet().iterator();

        while (it.hasNext()) {
            try {
                String name = it.next();
                DataOutputStream out = clients.get(name);
                out.writeUTF(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new MultiChatServer().start();
    }

    class ServerReceiver extends Thread {
        Socket socket;
        DataInputStream in;
        DataOutputStream out;

        ServerReceiver(Socket socket) {
            this.socket = socket;
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String name = "";

            try {
                name = in.readUTF();
                sendToAll("#" + name + "님이 들어오셨습니다.");

                clients.put(name, out);
                System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");
                while (in != null) {
                    sendToAll(in.readUTF());
                }//while


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // finally절이 실행된다는 것은 클라이언트가 빠져나간 것을 의미한다.
                sendToAll("#" + name + "님이 나가셨습니다.");

                // 대화방에서 객체 삭제
                clients.remove(name);
                System.out.println("[" + socket.getInetAddress() //
                        + ":" + socket.getPort() + "]" + "에서 접속을 종료하였습니다.");
                System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");
            }


        }
    }


}
