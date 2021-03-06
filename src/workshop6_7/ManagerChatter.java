/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop6_7;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Gwi99
 */
public class ManagerChatter extends javax.swing.JFrame implements Runnable {

    ServerSocket svSocket = null;
    BufferedReader br = null;
    ChatPanel cp = null;
    Thread checkConnection = null;// thread for exploring connections from staffs
    boolean exit = false;// To manage the thread

    public ManagerChatter() {
        initComponents();
        txtServerPort.setText("12340");
        this.setLocationRelativeTo(null);
        this.setTitle("Chatting Program");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lbMessage = new javax.swing.JLabel();
        txtServerPort = new javax.swing.JTextField();
        btnBind = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(617, 600));

        jPanel1.setPreferredSize(new java.awt.Dimension(464, 30));

        lbMessage.setText("Manager Port: ");

        btnBind.setText("Bind");
        btnBind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBindActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(txtServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                .addComponent(btnBind))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtServerPort, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbMessage)
                    .addComponent(btnBind))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jTabbedPane2.setPreferredSize(new java.awt.Dimension(100, 730));
        getContentPane().add(jTabbedPane2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void unbind() throws IOException {
        exit = true;
        this.lbMessage.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        this.txtServerPort.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        this.btnBind.setText("Bind");
        this.lbMessage.setText("Manager Port: ");
        this.txtServerPort.setEditable(true);
        if (br != null) {
            br.close();
        }
        if (svSocket != null) {
            svSocket.close();
            svSocket = null;
        }
  
    }
    
    public void closeChattingInterface(){
        this.remove(cp);
        cp.updateUI();
    }

    private void btnBindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBindActionPerformed
        // TODO add your handling code here:
        String txtPort = txtServerPort.getText();
        if (txtPort.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please input connection port!");
            return;
        }
        int port = Integer.parseInt(txtPort);
        try {
            if (svSocket == null) {
                exit = false;
                svSocket = new ServerSocket(port);
                this.btnBind.setText("UnBind");
                this.lbMessage.setText("Server is running at the port: ");
                this.lbMessage.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
                this.txtServerPort.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
                this.txtServerPort.setEditable(false);
                // init thread
                checkConnection = new Thread(this);
                checkConnection.start();

            } else {
                unbind();
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Can not bind the server!\n" + ex.getMessage());
            System.exit(0);
        }

    }//GEN-LAST:event_btnBindActionPerformed

    @Override
    public void run() {
        while (!exit) { // check continuity ...
            try {
                if (svSocket.isBound()) {
                    Socket staffSocket = svSocket.accept();
                    if (staffSocket != null) {// some one connect to server
                        br = new BufferedReader(new InputStreamReader(staffSocket.getInputStream()));
                        // get staff name when connect to server
                        String text = br.readLine();
                        int pos = text.indexOf(":");
                        String staffName = text.substring(pos + 1);
                        cp = new ChatPanel(staffSocket, "Manager", staffName);
                        this.jTabbedPane2.add(staffName, cp);
                        cp.updateUI();
                    }
                    Thread.sleep(100);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManagerChatter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManagerChatter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManagerChatter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManagerChatter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManagerChatter().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBind;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel lbMessage;
    private javax.swing.JTextField txtServerPort;
    // End of variables declaration//GEN-END:variables

}
