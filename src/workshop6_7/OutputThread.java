/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop6_7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author Gwi99
 */
public class OutputThread extends Thread {

    /**
     * @param args the command line arguments
     */
    Socket socket = null;
    JTextArea txt;
    BufferedReader br = null;
    String sender = "";
    String receiver = "";

     public OutputThread(Socket socket, JTextArea txt, String sender, String receiver) {
        super();
        this.socket = socket;
        this.txt = txt;
        this.sender = sender;
        this.receiver = receiver;
        // init BufferedReader object
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Network Error");
            System.exit(0);
        }
    }

    @Override
    public void run() {
        boolean flag = false;
        boolean active = false;
        boolean isFile = false;
        String contentFile = "";
        String fileName = "";
        while (true) {
            try {
                String msg = "";
                if (socket != null) {
                    if ((msg = br.readLine()) != null) {
                        if (msg.contains("*/#Messenger")) {// for chatting
                            msg = br.readLine();
                            txt.append("\n" + receiver + ": " + msg);
                            isFile = false;

                        } else {// for sending file .txt
                            isFile = true;
                            if (flag == false) {
                                int r = JOptionPane.showConfirmDialog(txt, "Do you want to receive this file ?",
                                        "Annoucement", JOptionPane.YES_NO_OPTION);
                                flag = true;
                                if (r == JOptionPane.YES_OPTION) {
                                    active = true;
                                }
                            }
                            // create file in server side
                            if (active) {
                                if (!"EndFile*$#".equals(msg)) {
                                    if (fileName.isEmpty()) {
                                        fileName = msg;

                                    } else {
                                        contentFile = contentFile + msg + "\n";
                                    }

                                } else {
                                    String destinationDirectory = "D:\\Java Desktop (PRJ311)\\"
                                            + "Workshop\\Workshop5-6\\";
                                    File file = new File(destinationDirectory + fileName);
                                    boolean existed = file.exists();
                                    if (!existed) {
                                        FileOutputStream fileReceive
                                                = new FileOutputStream(destinationDirectory + fileName);
                                        fileReceive.write(contentFile.getBytes());
                                        fileReceive.close();
                                    }
                                    active = false;
                                    flag = false;
                                    contentFile = "";
                                    txt.append("\n" + receiver + ": Vừa mới gửi cho bạn một file "
                                            + fileName + " ở địa chỉ " + destinationDirectory);
                                }
                            }
                        }
                    }
                }
                if(!isFile){
                    Thread.sleep(200);
                }

            } catch (IOException | InterruptedException e) {
                JOptionPane.showMessageDialog(this.txt, "Disconnect chatting!");
                break;
            }
        }
    }
}
