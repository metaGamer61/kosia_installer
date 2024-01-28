package fr.segazium.kosiaInstaller.front;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.Timer;

import fr.segazium.kosiaInstaller.Main;

public class Frame extends JFrame implements ActionListener {
    private static Frame f;
    private static boolean move;
    private static int xOffSet, yOffSet;
    public static int mouseX, mouseY;
    public static Rectangle closeRect = null;
    public static boolean goToClose;
    public static int barH = 50;
    public static int bordersize = 3;
    private Timer renderer = null;
    public Frame() {
        super("Kosia installer");
        renderer = new Timer(5, this);
    }
    public static void init() {
        f = new Frame();
        f.setUndecorated(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(700, 700);
        f.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == 1) {
                    if(e.getX() >= closeRect.getX() && e.getX() <= closeRect.getX()+closeRect.getWidth() && e.getY() >= closeRect.getY() && e.getY() <= closeRect.getY()+closeRect.getHeight()) {
                        goToClose = true;
                    } else if(e.getY() <= bordersize+barH) {
                        move = true;
                        xOffSet = e.getXOnScreen()-f.getX();
                        yOffSet = e.getYOnScreen()-f.getY();
                    }
                }
            };
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == 1) {
                    if(e.getX() >= closeRect.getX() && e.getX() <= closeRect.getX()+closeRect.getWidth() && e.getY() >= closeRect.getY() && e.getY() <= closeRect.getY()+closeRect.getHeight() && goToClose) {
                        Main.close();
                    }
                    move = false;
                    goToClose = false;
                }
            }
        });
        f.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                if(move) {
                    int nx = e.getXOnScreen()-xOffSet;
                    int ny = e.getYOnScreen()-yOffSet;
                    f.setLocation(nx, ny);
                }
            }
        });
        f.setLocationRelativeTo(null);
        int btnSize = 30;
        closeRect = new Rectangle(f.getWidth()-btnSize-10, 13, btnSize, btnSize);
        f.getContentPane().setLayout(null);
        Text path = new Text("Path to mod folder");
        path.setBounds(f.getWidth()/2-path.getPreferredSize().width/2, barH+bordersize*2+10, (int)path.getPreferredSize().getWidth(), (int)path.getPreferredSize().getHeight());
        f.getContentPane().add(path);
        TextInput ti = new TextInput();
        if(Main.targetUseFolder != null) ti.setText(Main.targetUseFolder.getAbsolutePath());
        int w = 500;
        ti.setBounds(f.getWidth()/2-w/2, path.getY()+path.getHeight()+10, w, 30);
        f.getContentPane().add(ti);
        Button install = new Button("Install (Clear folder)");
        w = install.getPreferredSize().width+40;
        int h = install.getPreferredSize().height+20;
        install.setBounds(f.getWidth()/2-w/2, f.getHeight()-10-h, w, h);
        install.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                install.setBackground(Colors.borders);
            }
            public void mouseExited(MouseEvent e) {
                install.setBackground(Colors.BG);
            }
        });
        install.addActionListener((e) -> {
            Main.install(path.getText(), f);
        });
        install.setFocusable(false);
        f.getContentPane().add(install);
        f.setVisible(true);
    }
    public void paint(Graphics gt) {
        BufferedImage i = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = i.getGraphics();
        g.setColor(Colors.borders);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Colors.BG);
        g.fillRect(bordersize, bordersize, getWidth()-bordersize-3, getHeight()-bordersize-3);
        g.setColor(Colors.borders);
        g.fillRect(0, bordersize+barH, getWidth(), bordersize);
        g.setColor(Colors.font);
        if(Colors.chars != null) {
            g.setFont(Colors.chars.deriveFont(Font.PLAIN, 22));
        }
        g.drawString(getTitle(), bordersize+7, g.getFontMetrics().getHeight()/2+barH/2-bordersize-1);
        if(Colors.closebtn != null) {
            g.drawImage(Colors.closebtn, (int)closeRect.getX(), (int)closeRect.getY(), (int)closeRect.getWidth(), (int)closeRect.getHeight(), null);
        }
        for(Component comp : getContentPane().getComponents()) {
            BufferedImage img = new BufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_INT_ARGB);
            comp.paint(img.getGraphics());
            g.drawImage(img, comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight(), null);
        }
        renderer.start();
        gt.drawImage(i, 0, 0, getWidth(), getHeight(), null);
    }
    public void actionPerformed(ActionEvent e) {
        renderer.stop();
        repaint();
    }
}