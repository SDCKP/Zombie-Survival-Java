package zombies;

import javax.swing.JOptionPane;


public class Main extends javax.swing.JFrame {

    public Main() {
        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btJugar = new javax.swing.JButton();
        btInstrucciones = new javax.swing.JButton();
        btSobre = new javax.swing.JButton();
        btSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 0, 48)); // NOI18N
        jLabel1.setText("Zombie Survival");

        btJugar.setText("Jugar");
        btJugar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btJugarActionPerformed(evt);
            }
        });

        btInstrucciones.setText("Instrucciones");
        btInstrucciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btInstruccionesActionPerformed(evt);
            }
        });

        btSobre.setText("Sobre");
        btSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSobreActionPerformed(evt);
            }
        });

        btSalir.setText("Salir");
        btSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btSobre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btInstrucciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btJugar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(92, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(btJugar, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btInstrucciones, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSobre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btSalirActionPerformed

    private void btSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSobreActionPerformed
        JOptionPane.showMessageDialog(this, "Programado por Bruno Sanz - 2ºDAM\nBase: Curso Invaders - Planetalia " + 
                "\nSprites de los zombies/personajes: www.spriters-resource.com" +
                "\nSonidos: www.soundbible.com\nItems/cubos: Bruno Sanz\n\nPara clase DI. Finalizado el 6 de Marzo del 2014", "Sobre Zombie Survival", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btSobreActionPerformed

    private void btInstruccionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btInstruccionesActionPerformed
        JOptionPane.showMessageDialog(this, "En un mundo apocalíptico nuestro héroe trata de sobrevivir "
                + "a las oleadas de zombis.\nEn su arsenal cuenta con bloques y minas. Los bloques se pueden apilar y "
                + "los zombies los rompen al cabo de un tiempo.\nLas minas se activan al tocar el suelo y cualquiera que las "
                + "pise morirá.\nSi las minas caen sobre un bloque se detonan, rompiendo parcialmente el bloque.\n "
                + "Si una mina cae sobre otra mina ambas explotan.\nSi un bloque cae sobre un zombie este muere aplastado, pero el bloque se rompe parcialmente"
                + "\nPausar el juego reinicia la duración del nivel (pero no re-spawnea los zombis)\n\n"
                + "Controles:\nEnter: Pausa\nIzquierda: Flecha izquierda\nDerecha: Flecha derecha\n" +
                "Salto: Barra espaciadora\nCambio de objeto: Control izquierdo\nUsar objeto: Shift(Mayúsculas)", "Información", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btInstruccionesActionPerformed

    private void btJugarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btJugarActionPerformed
        //Creamos un hilo con el juego
        new Thread(new Runnable() {
            @Override
            public void run() {
                Zombies in = new Zombies();
                in.game();
            }
        }).start();
        //Ocultamos el menu principal
        setVisible(false);
        
    }//GEN-LAST:event_btJugarActionPerformed

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btInstrucciones;
    private javax.swing.JButton btJugar;
    private javax.swing.JButton btSalir;
    private javax.swing.JButton btSobre;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

}
