/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package clarisign;

import clarisign.vista.LoginView;

public class ClariSign {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
