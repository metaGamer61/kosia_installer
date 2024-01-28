package fr.segazium.kosiaInstaller.front;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class TextInput extends JTextField {
    public TextInput() {
        super();
        setBackground(Colors.BG);
        setBorder(BorderFactory.createLineBorder(Colors.borders, 3));
        setFont(Colors.chars.deriveFont(Font.PLAIN, 18));
        setCaretColor(Colors.font);
        setForeground(Colors.font);
    }
}