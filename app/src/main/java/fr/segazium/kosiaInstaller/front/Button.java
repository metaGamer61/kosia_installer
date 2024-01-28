package fr.segazium.kosiaInstaller.front;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class Button extends JButton {
    public Button(String text) {
        super(text);
        setForeground(Colors.font);
        setBorder(BorderFactory.createLineBorder(Colors.borders, 3));
        setBackground(Colors.BG);
        setFont(Colors.chars.deriveFont(Font.PLAIN, 22));
    }
}