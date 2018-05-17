package MultiChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MultichatGUIClient implements ActionListener {
    Frame f;
    Panel p;
    Button b1;
    TextField tf;
    TextArea ta;
    // ===============================
    String name;
    ClientSender sender;


    public MultichatGUIClient(String name) {
        this.name = name;
        f = new Frame(name);
        p = new Panel();
        b1 = new Button("전송");
        tf = new TextField();
        ta = new TextArea(20, 50);

    }

    public void launchTest(){
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        f.add(BorderLayout.SOUTH, p);
        b1.addActionListener(this);
        b1.setBackground(Color.yellow);

        tf.setColumns(40);
        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                char keyCode = e.getKeyChar();
                if(keyCode == KeyEvent.VK_ENTER)
                    actionPerformed(null);
            }
        });

        p.setBackground(Color.green);
        p.add(tf);
        p.add(b1);

        f.add(BorderLayout.CENTER, ta);

        f.setVisible(true);
        f.pack();

        // =================================================
        Socket socket = null;

        try {
            String serverIP = "127.0.0.1";

            socket = new Socket(serverIP, 7777);
            System.out.println("서버에 연결되었습니다");
            sender = new ClientSender(socket);
            Thread receiver = new Thread(new ClientReceiver(socket));
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sender.send(tf.getText());
        // =========================
        tf.setText("");
        tf.requestFocus();


    }

    public void setMessage(String message) {
        ta.append(message);
        ta.append("\n");
    }

    class ClientSender {
        Socket socket;
        DataOutputStream out;

        ClientSender(Socket socket) {
            this.socket = socket;

            try {
                this.out = new DataOutputStream(socket.getOutputStream());

                // 시작하자 마자, 자신의 대화명을 서버로 전송
                if (out != null) {
                    out.writeUTF(name);
                }

            } catch (Exception e) {
            }
        }

        public void send(String message) {
            if (out != null) {
                try {
                    // 키보드로 입력받은 데이터를 서버로 전송
                    out.writeUTF("[" + name + "] " + message);
                } catch (IOException e) {
                }
            }
        }
    }

    // 메시지 수신용 Thread
    class ClientReceiver implements Runnable {
        Socket socket;
        DataInputStream in;

        // 생성자
        ClientReceiver(Socket socket) {
            this.socket = socket;

            try {
                // 서버로 부터 데이터를 받을 수 있도록 DataInputStream 생성
                this.in = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
            }
        }

        public void run() {
            while (in != null) {
                try {
                    // 서버로 부터 전송되는 데이터를 출력
                    MultichatGUIClient.this.setMessage(in.readUTF());
                } catch (IOException e) {
                }
            }
        }
    }
    public static void main(String[] args) {

        String name = null;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("대화명을 입력하세요.");
            System.out.print(">>> ");
            name = scanner.nextLine();
            if (name.isEmpty()) {
                System.out.println("대화명은 한글자 이상 입력해야 합니다.\n\n");
            }
        } while (name.isEmpty());

        new MultichatGUIClient(name).launchTest();
    }




}
