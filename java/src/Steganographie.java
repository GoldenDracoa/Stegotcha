package mainPackage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The main Operating class. Using the steghide command to embed or extract the messages.
 *
 *  5 octets txt FOR 1,51 ko bmp    (coef = 0.302)
 *
 * Created by Alexandre on 16/03/2016.
 */
public class Steganographie {

    String os;
    String p;
    Runtime runtime;

    public Steganographie() {
        os = System.getProperty("os.name");
        runtime = Runtime.getRuntime();
        if (os.contains("Windows")) {
            p="\\";
        } else {
            p="/";
        }
    }
    //TODO : change the insert() function, the files to be used have to be size checked.

    /**
     * Embed the embedTxt into cover.bmp using the passphrase param
     * @param embedTxt the text that has to be embed
     * @param passphrase the passphrase
     * @return coverFileName
     */
    protected File insert(String embedTxt, String passphrase) {
        System.out.println("Starting Insertion");
        File tmp = new File ("resources"+p+"embedMsg.txt");

        try {
            FileWriter fw = new FileWriter(tmp);
            fw.write(embedTxt);
            fw.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du message : "+e.getMessage());
        }

        String[] cmd;
        File coverFile = getBestWeightFile(tmp.length());

        if (coverFile != null) {
            System.out.println("Copying file");
            if (os.contains("Windows")) {
                cmd = new String[] {"xcopy", "/Y", "/C", "resources"+p+"covering files"+p+coverFile.getName(), "resources"+p};

            } else {
                cmd = new String[] {"cp", "resources"+p+"covering files"+p+coverFile.getName(), "resources"+p};
            }

            for (String s : cmd) {
                System.out.println(s);
            }

            try {
                Process process = runtime.exec(cmd);
                process.waitFor();
            } catch (InterruptedException e) {
                System.out.println("Copy has been interrupted");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Copy failed");
                e.printStackTrace();
            }
            File sendFile = new File("resources"+p+coverFile.getName());

            System.out.println("Embed text has a correct length.");
            cmd = new String[]{"", "embed", "-ef resources" + p + "embedMsg.txt", "-cf " + "resources" + p +sendFile.getName(), "-p " + passphrase, "-z 9"};

            if (os.contains("Windows")) {
                System.out.println("Windows OS detected");
                cmd[0] = "resources" + p + "steghide" + p + "steghide.exe";
            } else {
                System.out.println("Other OS detected");
                cmd[0] = "steghide";
            }

            try {
                Process process = runtime.exec(cmd);
                process.waitFor();
            } catch (InterruptedException e) {
                System.out.println("Embed execution has been interrupted");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Embed execution failed");
                e.printStackTrace();
            }
            System.out.println("Insertion correctly ended");
            return sendFile;
        }
        return null;
    }

    /**
     * Extract msg from recep.bmp using passphrase param.
     * @param passphrase the passphrase. Here : the owner username
     * @return Embeded text
     */
    protected String extract(String passphrase) {
        System.out.println("Starting Extraction");
        File tmp = new File ("resources"+p+"embedMsg.txt");

        String[] cmd = new String[]{"","extract", "-sf resources"+p+"receiv.bmp", "-xf resources"+p+"extractMsg.txt", "-p "+passphrase};
        if (os.contains("Windows")) {
            System.out.println("Windows OS detected");
            cmd[0]="resources"+p+"steghide"+p+"steghide.exe";
        } else {
            System.out.println("Other OS detected");
            cmd[0] = "steghide";
        }

        try {
            Process process = runtime.exec(cmd);
            process.waitFor();
        } catch (InterruptedException e) {
            System.out.println("Extract execution has been interrupted");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Extract execution failed");
            e.printStackTrace();
        }
        String result="";
        try {
            FileReader fr = new FileReader(tmp);
            int c = fr.read();
            while (c != -1) {
                result+=(char)c;
                c = fr.read();
            }
            fr.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du message : "+e.getMessage());
        }

        /** TODO:
         *  Traitement message d'erreur !
         */
        System.out.println("Exctraction correclty ended");
        tmp.delete();
        return result;
    }

    private File getBestWeightFile(long required) {
        System.out.println("Looking for the best weight file for "+required+" totalSpace.");
        String filePath = "";
        File coveringFiles = new File("resources"+p+"covering files");
        File[] listFiles = coveringFiles.listFiles();
        for (File f : listFiles) {
            if (f.length()>=required*0.302) {
                System.out.println("The file "+f.getName()+" is the most appropriated file.");
                return f;
            }
        }
        System.out.println("ERROR ! No file has been found.");
        return null;
    }



}

