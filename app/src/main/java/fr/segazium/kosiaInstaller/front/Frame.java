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
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
    private static TextInput ti;
    private static Button install;
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
        ti = new TextInput();
        if(Main.targetUseFolder != null) ti.setText(Main.targetUseFolder.getAbsolutePath());
        int w = 500;
        ti.setBounds(f.getWidth()/2-w/2, path.getY()+path.getHeight()+10, w, 30);
        f.getContentPane().add(ti);

        JTable table = new JTable(Main.mods_optionals.size()+Main.mods_required.size(), 3) {
            public Class<?> getColumnClass(int column) {
                if(column == 0) return String.class;
                if(column == 1) return String.class;
                if(column == 2) return Boolean.class;
                return super.getColumnClass(column);
            }
            public String getColumnName(int column) {
                if(column == 0) return "Name";
                if(column == 1) return "Link";
                if(column == 2) return "Download";
                return super.getColumnName(column);
            }
            public boolean editCellAt(int row, int column, EventObject e) {
                if(column == 0 || column == 1) return false;
                String link = (String)getValueAt(row, 1);
                return Main.mods_optionals.contains(link);
            }
        };
        int row = 0;
        for(String mod : Main.mods_optionals) {
            String[] d = mod.split("/");
            table.setValueAt(d[d.length-1], row, 0);
            table.setValueAt(mod, row, 1);
            if(Main.targetUseFolder != null && Main.targetUseFolder.exists()) {
                table.setValueAt(new File(Main.targetUseFolder, d[d.length-1]).exists(), row, 2);
            } else {
                table.setValueAt(false, row, 2);
            }
            row++;
        }
        for(String mod : Main.mods_required) {
            String[] d = mod.split("/");
            table.setValueAt(d[d.length-1], row, 0);
            table.setValueAt(mod, row, 1);
            table.setValueAt(true, row, 2);
            row++;
        }
        JScrollPane scroller = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        int y = ti.getY()+ti.getHeight()+30;
        scroller.setBounds(50+bordersize, y, f.getWidth()-100-bordersize, f.getHeight()-y-bordersize-80);
        table.setBounds(0, 0, scroller.getWidth(), scroller.getHeight());
        f.getContentPane().add(scroller);

        install = new Button("Install (Move all files in a subfolder)");
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
            disableAll();
            Thread th = new Thread(() -> {
                ArrayList<String> mods = new ArrayList<String>();
                //calculates urls
                Main.install(ti.getText(), f, mods);
            });
            th.start();
        });
        install.setFocusable(false);
        f.getContentPane().add(install);
        f.setVisible(true);
    }
    public static void disableAll() {
        install.setEnabled(false);
        ti.setEnabled(false);
    }
    public static void enableAll() {
        install.setEnabled(true);
        ti.setEnabled(true);
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