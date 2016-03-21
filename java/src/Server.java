package mainPackage;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.crypto.*;
import javax.swing.*;

public class Server implements Runnable {

    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(8080);
        } catch (IOException e) {
            System.out.println(e);
        }
        Socket s;
        InputStream ins;
        OutputStream out;

        while(true)
        {
            try {
                s = ss.accept();
                ins = s.getInputStream();
                out = new FileOutputStream(new File("receiv.bmp"));
                Thread t=new Thread(this);
                t.start();
            } catch (IOException e) {
                System.out.println("Error in recepetion : "+e);
            }
            String extract = steghide.extract("MESSAGE COMMUN");
            String pseudo;
            if (!extract.isEmpty()) {
                pseudo = extract.substring(4, extract.indexOf("@"));
                String message = extract.substring(extract.indexOf("@")+1, extract.length());
                MainInterface.addText(MainInterface.tchat, pseudo+": "+message);
            } else {
                extract = steghide.extract(Server.encrypt(Server.myPseudo));
                pseudo = extract.substring(4, extract.indexOf("@"));
                String secretMessage = extract.substring(extract.indexOf("@")+1, extract.indexOf("|"));
                String clearMessage = extract.substring(extract.indexOf("|")+1, extract.length());
                MainInterface.addText(MainInterface.tchat, pseudo+": "+secretMessage);
                MainInterface.send(clearMessage, pseudo);
            }
        }

    }

    public Steganographie steghide = new Steganographie();
    public static ArrayList<String> connectedUsersArray = new ArrayList<>();
    public String p;
    public static String myPseudo;
    private static File dataBank;

    public Server() {
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            p="\\";
        } else {
            p="/";
        }
       dataBank = new File("resources"+p+"dataBank.txt");
    }

    public void newUser(String pseudo, String ip) {
        String result="";
        try {
            FileReader fr = new FileReader(dataBank);
            int c = fr.read();
            while (c != -1) {
                result+=(char)c;
                c = fr.read();
            }
            fr.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du message : "+e.getMessage());
        }
        try {
            FileWriter fw = new FileWriter(dataBank);
            fw.write(result+"\n"+pseudo+"@"+ip);
            fw.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du message : "+e.getMessage());
        }
    }

    private static String getIp(String pseudo) {
        String result="";
        try {
            FileReader fr = new FileReader(dataBank);
            int c = fr.read();
            while (c != -1) {
                result+=(char)c;
                c = fr.read();
            }
            fr.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du message : "+e.getMessage());
        }
        System.out.println(result);
        System.out.println(result.split("\n"));
        String tabResult[] = result.split("\n");
        int line=-1;
        for (int i=0; i<tabResult.length; i++) {
            System.out.println(tabResult[i]);
            if (tabResult[i].contains(pseudo)) {
                line = i;
                break;
            }
        }
            String tmp = tabResult[line].substring(0, tabResult[line].indexOf("@"));
            if (tmp.equals(pseudo)) {
                return tabResult[line].substring(tabResult[line].indexOf("@")+1, tabResult[line].length());
            }
        return null;
    }

    public boolean userExist(String unCryptedPseudo) {
        System.out.println(unCryptedPseudo);
        String result="";
        try {
            FileReader fr = new FileReader(dataBank);
            int c = fr.read();
            while (c != -1) {
                result+=(char)c;
                c = fr.read();
            }
            fr.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du message : "+e.getMessage());
        }
        System.out.println(result);
        String[] line = result.split("\n");
        for (int i=0; i<line.length; i++) {
            String[] loginPass = line[i].split("@");
            System.out.println(loginPass[0] +" <-> "+unCryptedPseudo+" -> "+encrypt(unCryptedPseudo));
            if (loginPass[0].equals(encrypt(unCryptedPseudo))) {
                return true;
            }
        }
        return false;
    }

    public static void send(File sendFile) {
        for (int cpt=0; cpt<connectedUsersArray.size(); cpt++) {
            String user = connectedUsersArray.get(cpt);
            try {
                InetAddress ipaddress = InetAddress.getByName(getIp(user));
                Socket socket = new Socket(ipaddress, 8080);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                FileInputStream in = new FileInputStream(sendFile);
                while (true) {
                    int i = in.read();
                    if (i == -1) break;
                    out.write(i);
                }
                in.close();
                out.close();
            } catch (UnknownHostException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public void addConnectedUser(String user) {
        connectedUsersArray.add(user);
        MainInterface.addText(MainInterface.connectedUsers, user);
    }

    public static String encrypt(String password){
        String crypte="";
        for (int i=0; i<password.length();i++)  {
            int c=password.charAt(i)^48;
            crypte=crypte+(char)c;
        }
        return crypte;
    }

    public static boolean isUserConnected(String user) {
        return connectedUsersArray.contains(user);
    }

}
