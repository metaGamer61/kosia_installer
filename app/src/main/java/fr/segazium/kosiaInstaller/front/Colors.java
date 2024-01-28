package fr.segazium.kosiaInstaller.front;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import fr.segazium.kosiaInstaller.Main;

public class Colors {
    public static final Color BG = new Color(0x82145A);
    public static final Color borders = new Color(0x30000A);
    public static final Color font = new Color(0xE8EEF4);
    public static Font chars = null;
    public static BufferedImage closebtn = null;
    public static void init() {
        try {
            chars = Font.createFont(Font.PLAIN, Main.class.getClassLoader().getResourceAsStream("impact.ttf"));
            closebtn = ImageIO.read(Main.class.getClassLoader().getResourceAsStream("close.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}