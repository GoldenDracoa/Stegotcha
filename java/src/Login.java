package mainPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;


/**
 * The first windowed interface of the program.
 * Warning /!\ Resizable=false !
 * Created by the group on 20/03/2016.
 */
public class Login {

    private Server server;

    public Login() {

        server = new Server();
        MainInterface mi = new MainInterface();
        mi.f.setVisible(false);

        Dimension dim = new Dimension(1000, 800);

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setSize(dim);

        JPanel p = new JPanel();
        p.setLayout(null);


        /** Title **/
        JLabel tit = new JLabel("B I E N V E N U E");
        tit.setBounds(306, 50, 370, 70);
        tit.setFont(new Font("", Font.PLAIN, 40));
        p.add(tit);


        /** Label **/
        JLabel login = new JLabel("Connection");
        login.setBounds(100, 200, 250, 50);
        login.setFont(new Font("", Font.PLAIN, 35));
        p.add(login);


        /** Text Field Login **/
        JTextField log = new JTextField();
        log.setBounds(100, 280, 250, 50);
        log.setBackground(Color.WHITE);
        log.setForeground(Color.BLACK);
        log.setFont(new Font("", Font.BOLD, 12));
        p.add(log);

        addText(log, "Your login here");


        /** Button connection **/
        JButton seco = new JButton("Go");
        seco.setBounds(100, 350, 250, 30);
        seco.setBackground(Color.gray);
        seco.setFocusable(false);
        seco.setFont(new Font("", Font.ITALIC, 20));
        seco.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                String logStr = log.getText();
                if (server.userExist(logStr)) {
                    server.addConnectedUser(Server.encrypt(logStr));
                    server.myPseudo = logStr;
                    f.setVisible(false);
                    mi.f.setVisible(true);
                } else {
                    addText(log, "Bad Login !!");
                }


/*                BufferedReader in = null;
                try {
                    in = new BufferedReader(new FileReader("dataBank.csv"));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    if ((in.readLine()) != null)
                    {
                        // Afficher le contenu du fichier
                        System.out.println ("utilisateur connu");
                        new MainInterface();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/
            }
        });
        p.add(seco);


        /** Label **/
        JLabel in = new JLabel("Subscribe");
        in.setBounds(550, 200, 250, 50);
        in.setFont(new Font("", Font.PLAIN, 35));
        p.add(in);


        /** Text Field New Login **/
        JTextField nv = new JTextField();
        nv.setBounds(550, 280, 250, 50);
        nv.setBackground(Color.WHITE);
        nv.setForeground(Color.BLACK);
        nv.setFont(new Font("", Font.BOLD, 12));
        p.add(nv);

        addText(nv, "Your new login here");


        /** Button for subscribe **/
        JButton sins = new JButton("Create an account");
        sins.setBounds(550, 350, 250, 30);
        sins.setBackground(Color.gray);
        sins.setFocusable(false);
        sins.setFont(new Font("", Font.ITALIC, 20));
        sins.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                nv.setText(Server.encrypt(nv.getText()));
                nv.setText(nv.getText().replace('@', 'a'));
                nv.setText(nv.getText().replace('|', 'b'));
                try {
                    server.newUser(nv.getText(), InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException e) {
                    System.out.println(e);
                }
            }
        });
        p.add(sins);


        /** Label authors **/
        JLabel authors = new JLabel("Application created by Alexandre Cailliez, Quentin Spinnewyn & Valentin Kancurzewski.");
        authors.setBounds(150, 600, 650, 50);
        authors.setFont(new Font("", Font.ITALIC, 15));
        p.add(authors);


        f.setContentPane(p);
        f.setVisible(true);


    }


    /** Add text method **/
    private void addText(JTextField log, String add) {
        String tmp = log.getText();
        log.setText(tmp+"\r\n"+add);
    }


    /** Action performed **/
/*    public void actionPerformed(ActionEvent e)
    {
        System.out.println("New login created and add to csv file");
        new MainInterface();
        this.setVisible(false);
    }


    /** Add new user to csv **/
/*    private void writeTextFile(JTextField nv, String fileName) {
        try {
            FileWriter outStream =  new FileWriter (fileName);
            outStream.write (nv.getText());
            outStream.close();
            JOptionPane.showMessageDialog(null, "Vous avez bien été ajouté aux users");
        } catch (IOException e) {
            nv.setText("Erreur: " + e.getMessage() + " ");
            e.printStackTrace();
        }
    }


    private void setVisible(boolean b) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }*/

}
