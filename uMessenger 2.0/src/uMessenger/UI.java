package uMessenger;

import java.awt.Color;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.text.StyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

public class UI extends JFrame {
    
    private static DataOutputStream dos;
    
//////CONSTRUCTOR///////////////////////////////////////////////////////////////
    public UI(){
        initComponents();
        
        doc = textPane.getStyledDocument();
        
        //Setting the font colors
        CYAN = textPane.addStyle(null, null);
        GREEN = textPane.addStyle(null, null);
        YELLOW = textPane.addStyle(null, null);
        MAGENTA = textPane.addStyle(null, null);
        RED = textPane.addStyle(null, null);
        BLUE = textPane.addStyle(null, null);
        PINK = textPane.addStyle(null, null);
        WHITE = textPane.addStyle(null, null);
        BLACK = textPane.addStyle(null, null);
        GRAY = textPane.addStyle(null, null);
        StyleConstants.setForeground(CYAN, Color.cyan);
        StyleConstants.setForeground(GREEN, Color.green);
        StyleConstants.setForeground(YELLOW, Color.yellow);
        StyleConstants.setForeground(MAGENTA, Color.magenta);
        StyleConstants.setForeground(RED, Color.red);
        StyleConstants.setForeground(BLUE, Color.blue);
        StyleConstants.setForeground(PINK, Color.pink);
        StyleConstants.setForeground(WHITE, Color.WHITE);
        StyleConstants.setForeground(BLACK, Color.BLACK);
        StyleConstants.setForeground(GRAY, Color.DARK_GRAY);
    }
    
    public static void main(DataOutputStream dos) {
        
        UI.dos = dos;
        
        // Create and display the form
        java.awt.EventQueue.invokeLater(() -> {
            new UI().setVisible(true);
        });
    }
//////METHODS///////////////////////////////////////////////////////////////////

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(textPane);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(0, 6, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        try {
            dos.writeUTF(evt.getActionCommand() );
            
        } catch (IOException ex) {
            System.out.println("Error sending message: " + ex);
        }
        jTextField1.setText("");
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane textPane;
    // End of variables declaration//GEN-END:variables
    private final StyledDocument doc;
    private final Style CYAN;
    private final Style GREEN;
    private final Style YELLOW;
    private final Style MAGENTA;
    private final Style RED;
    private final Style BLUE;
    private final Style PINK;
    private final Style WHITE;
    private final Style BLACK;
    private final Style GRAY;
}
