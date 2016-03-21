package mainPackage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The windowed interface of the program.
 * Warning /!\ Resizable=false !
 * Created by Alexandre on 17/03/2016.
 */
public class MainInterface {

    private static JLabel infoBannier = new JLabel();
    private JTextArea coverMessage = new JTextArea();
    private JTextArea secretMessage = new JTextArea();
    private JTextField userDest = new JTextField();
    private static Steganographie steghide = new Steganographie();
    private String p = steghide.p;
    public static JTextArea connectedUsers;
    public static JTextArea tchat;
    public JFrame f;

    public MainInterface() {
        Dimension dim = new Dimension(1000, 800);

        f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setSize(dim);

        JPanel p = new JPanel();
        p.setLayout(null);

        infoBannier.setBounds(400, 727, 800, 30);
        infoBannier.setForeground(Color.RED);
        infoBannier.setFont(new Font("", Font.PLAIN, 12));
        p.add(infoBannier);

        tchat = new JTextArea();
        tchat.setBounds(5, 2, 600, 600);
        tchat.setBackground(Color.WHITE);
        tchat.setFocusable(false);
        tchat.setForeground(Color.BLACK);
        tchat.setLineWrap(true);
        tchat.setFont(new Font("", Font.BOLD, 20));
        p.add(tchat);

        connectedUsers = new JTextArea("Connected Users :");
        connectedUsers.setBounds(650, 2, 327, 600);
        connectedUsers.setBackground(Color.WHITE);
        connectedUsers.setFocusable(false);
        connectedUsers.setForeground(Color.BLACK);
        connectedUsers.setLineWrap(true);
        connectedUsers.setFont(new Font("", Font.BOLD, 15));
        p.add(connectedUsers);

        JLabel coverIndicator = new JLabel("The visible message");
        coverIndicator.setBounds(50, 590, 300, 45);
        coverIndicator.setForeground(Color.GRAY);
        coverIndicator.setFont(new Font("", Font.BOLD, 10));
        p.add(coverIndicator);

        coverMessage.setBounds(25, 625, 350, 100);
        coverMessage.setBackground(Color.white);
        coverMessage.setForeground(Color.BLACK);
        coverMessage.setFont(new Font("", Font.BOLD, 15));
        coverMessage.setLineWrap(true);
        p.add(coverMessage);

        JLabel secretIndicator = new JLabel("The secret message (Optional)");
        secretIndicator.setBounds(425, 590, 300, 45);
        secretIndicator.setForeground(Color.LIGHT_GRAY);
        secretIndicator.setFont(new Font("", Font.BOLD, 10));
        p.add(secretIndicator);

        secretMessage.setBounds(400, 625, 350, 100);
        secretMessage.setBackground(Color.WHITE);
        secretMessage.setForeground(Color.GRAY);
        secretMessage.setFont(new Font("", Font.BOLD, 15));
        secretMessage.setLineWrap(true);
        p.add(secretMessage);

        JLabel userDestIndicator = new JLabel("The specific receiver (Optional)");
        userDestIndicator.setBounds(799, 618, 200, 20);
        userDestIndicator.setForeground(Color.LIGHT_GRAY);
        userDestIndicator.setFont(new Font("", Font.BOLD, 10));
        p.add(userDestIndicator);

        userDest.setBounds(800, 640, 150, 20);
        userDest.setBackground(Color.WHITE);
        userDest.setForeground(Color.BLACK);
        userDest.setFont(new Font("", Font.BOLD, 12));
        p.add(userDest);

        JButton send = new JButton("Send");
        send.setBounds(800, 680, 150, 20);
        send.setFont(new Font("", Font.BOLD, 12));
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sendable()) {
                    addText(tchat, Server.encrypt(Server.myPseudo)+": "+coverMessage.getText());
                    if (secretMessage.getText().isEmpty()) {
                        send(coverMessage.getText(), Server.encrypt(Server.myPseudo));
                        printInfoLog(false, "The message has been correctly send !");
                    } else {
                        send(coverMessage.getText(), secretMessage.getText(), userDest.getText(), Server.encrypt(Server.myPseudo));
                        printInfoLog(false, "The secret message has been correctly send !");
                    }
                } else if (coverMessage.getText().isEmpty()) {
                    printInfoLog(true, "Must input a clear message !");
                } else if (secretMessage.getText().isEmpty()) {
                    printInfoLog(true, "Please specify the secret message, or erase the receiver name.");
                } else if (userDest.getText().isEmpty()) {
                    printInfoLog(true, "Please specify the receiver name.");
                }
            }
        });
        p.add(send);

        f.setContentPane(p);
    }

    /**
     * Add a String to the TextArea automatically
     *
     * @param txtArea the TextArea that has to be updated
     * @param add     The text to be add
     */
    public static void addText(JTextArea txtArea, String add) {
        String tmp = txtArea.getText();
        txtArea.setText(tmp + "\r\n" + add);
    }

    /**
     * Print an informative message on the bottom of the window
     *
     * @param error is it an error ?
     * @param txt   the Message to be display
     */
    private static void printInfoLog(boolean error, String txt) {
        Color colorMsg;
        if (!error) {
            colorMsg = Color.BLUE;
        } else {
            colorMsg = Color.RED;
        }
        infoBannier.setForeground(colorMsg);
        infoBannier.setText(txt);
    }

    /**
     * Checking if the message is sendable.
     *
     * @return boolean
     */
    private boolean sendable() {
        if (coverMessage.getText().isEmpty()) {
            printInfoLog(true, "Please write a message.");
            return false;
        }
        if (!secretMessage.getText().isEmpty()) {
            if (userDest.getText().isEmpty()) {
                printInfoLog(true, "Please write the name of the receiver");
                return false;
            }
            if (coverMessage.getText().isEmpty()) {
                printInfoLog(true, "Please, write a clear message.");
                return false;
            }
            if (secretMessage.getText().contains("|") || secretMessage.getText().contains("@")) {
                printInfoLog(true, "Don't use following characters : | @");
                return false;
            }
            if (Server.isUserConnected(userDest.getText())) {
                printInfoLog(true, "User isn't connected.");
         //       return false;
            }
        }
        if (!userDest.getText().isEmpty()) {
            if (coverMessage.getText().isEmpty()) {
                printInfoLog(true, "Please write a secret message to send to the receiver, or erase the input");
                return false;
            }
        }
        return true;
    }

    /**
     * Send simple msg
     *
     * @param msg
     */
    public static void send(String msg, String from) {

        File dest = steghide.insert("From:"+from+"@"+msg, "MESSAGE COMMUN");
        Server.send(dest);
        dest.delete();
        printInfoLog(false, "Message has been correctly sent the all users");
    }

    /**
     * Send an steganed msg
     *
     * @param msg          The secret message
     * @param coverMsg     The clear message
     * @param destinataire The receiver of the secret msg
     */
    private void send(String msg, String coverMsg, String destinataire, String from) {
        String send = "From:"+from+"@"+msg+"|"+coverMsg;
        File dest = steghide.insert(send, destinataire);
        Server.send(dest);
        dest.delete();
        printInfoLog(false, "Message has been correctly sent. Secret message has been sent to "+destinataire);
    }

}
