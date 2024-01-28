package fr.segazium.kosiaInstaller.front;

import java.awt.Font;

import javax.swing.JLabel;

public class Text extends JLabel {
    public Text(String text) {
        super(text);
        setForeground(Colors.font);
        setFont(Colors.chars.deriveFont(Font.PLAIN, 26));
    }
}