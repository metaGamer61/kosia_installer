package fr.segazium.kosiaInstaller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import fr.segazium.kosiaInstaller.front.Colors;
import fr.segazium.kosiaInstaller.front.Frame;

public class Main {
    public static File targetUseFolder = null;
    public static File configFile = null;
    public static ArrayList<String> mods_urls;
    public static void main(String[] args) throws IOException {
        mods_urls = new ArrayList<String>();
        File configFolder = new File(System.getProperty("user.home")+"/AppData/Roaming/kosia");
        if(!configFolder.exists()) configFolder.mkdirs();
        configFile = new File(configFolder, "installer_conf.txt");
        try {
            if(!configFile.exists()) {
                configFile.createNewFile();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
            String line = br.readLine();
            if(line != null) {
                File tmf = new File(line);
                if(tmf.exists()) {
                    if(!tmf.isFile()) {
                        targetUseFolder = tmf;
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Colors.init();
        Frame.init();
    }
    public static void close() {
        System.exit(0);
    }
    public static void install(String path, JFrame parent) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
            bw.write(path);
            bw.newLine();
            bw.close();
            File folder = new File(path);
            if(folder.exists()) {
                if(!folder.isFile()) {

                } else {
                    JOptionPane.showMessageDialog(parent, "The target path need to be a folder", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(parent, "The target folder doesn't exist", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}