package fr.segazium.kosiaInstaller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import fr.segazium.kosiaInstaller.front.Colors;
import fr.segazium.kosiaInstaller.front.Frame;

public class Main {
    public static File targetUseFolder = null;
    public static File configFile = null;
    public static ArrayList<String> mods_required, mods_optionals;
    public static void main(String[] args) throws IOException {
        mods_required = new ArrayList<String>();
        mods_optionals = new ArrayList<String>();
        URL url = new URL("https://raw.githubusercontent.com/metaGamer61/kosia_installer/main/app/mods.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String l = in.readLine();
        while(l != null) {
            if(!l.startsWith("#")) {
                String[] datas = l.split("///");
                System.out.println(l);
                if(datas[0].equals("r")) {
                    mods_required.add(datas[1]);
                } else if(datas[0].equals("o")) {
                    mods_optionals.add(datas[1]);
                }
            }
            l = in.readLine();
        }
        in.close();
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
    public static void install(String path, JFrame parent, ArrayList<String> mods_urls) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(configFile));
            bw.write(path);
            bw.newLine();
            bw.close();
            File folder = new File(path);
            if(folder.exists()) {
                if(folder.isDirectory()) {
                    if(folder.listFiles().length != 0) {
                        for(File f : folder.listFiles()) {
                            f.delete();
                        }
                    }
                    boolean success = true;
                    for(String uri : mods_urls) {
                        URL url = new URL(uri);
                        try {
                            String[] seg = url.getPath().split("/");
                            Path p = Path.of(path+"/"+seg[seg.length-1]);
                            Files.copy(url.openStream(), p, StandardCopyOption.REPLACE_EXISTING);
                        } catch (Exception e) {
                            e.printStackTrace();
                            success = false;
                            JOptionPane.showMessageDialog(parent, "Unable to download file : "+url.getPath()+"\nCause : "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    }
                    if(success) {
                        JOptionPane.showMessageDialog(parent, "All mods are downloaded successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(parent, "The target path need to be a folder", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(parent, "The target folder doesn't exist", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Frame.enableAll();
    }
}