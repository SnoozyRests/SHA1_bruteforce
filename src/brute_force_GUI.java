import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class brute_force_GUI {
    private JTextArea txtOutput;
    private JButton btnEncrypt;
    private JButton btnForce;
    private JTextField txtEncrypt;
    private JTextField txtForce;
    private JPanel bfpanel;
    private JLabel lblEncrypt;
    private JLabel lblforce;
    private boolean fin;
    private long timer = 0;

    public brute_force_GUI() {
        btnEncrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtOutput.setText(hashWord(txtEncrypt.getText().trim()));
            }
        });

        btnForce.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bruteForce();

            }
        });
    }

    private String hashWord(String input){
        try{
            input = SHA1(input);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e){
            //System.err.println(e);
        }
        return input;
    }

    private void bruteForce(){
        String input = txtForce.getText().trim();
        if(!input.trim().isEmpty()){
            fin = false;
            timeStart();
            btnEncrypt.setEnabled(false);
            btnForce.setEnabled(false);
            new Thread(() -> {
                for (int i = 1; i < 7; i++) {
                    if (!fin) {
                        try {
                            char index[] = new char[i];
                            permutation(index, 0, "abcdefghijklmnopqrstuvwxyz0123456789", input);
                        } catch (Exception e) {
                            //System.out.println(e);
                        }
                    }
                }
                btnEncrypt.setEnabled(true);
                btnForce.setEnabled(true);
                txtOutput.append(timeStop());
            }).start();
        }
    }

    private void permutation(char[] index, int pos, String str, String input)
            throws NoSuchAlgorithmException, UnsupportedEncodingException{
        String passhash = txtForce.getText().trim().toLowerCase();
        String passTest, passTestHash;

        if(pos == index.length && !fin){
            passTest = new String(index);
            passTestHash = SHA1(passTest);

            if(passTestHash.equals(input)){
                txtOutput.setText("Encoded word was: " + passTest);
                fin = true;
            }

        } else {
            for(int i = 0; i < str.length(); i++){
                index[pos] = str.charAt(i);
                permutation(index, pos+1, str, input);
            }
        }
    }

    private static String convertToHex(byte[] data){
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < data.length; i++){
            int halfbtye = (data[i] >>> 4) & 0x0F;
            int two_halves = 0;
            do{
                if((0 <= halfbtye) && (halfbtye <= 9)){
                    buf.append((char)('0' + halfbtye));
                } else {
                    buf.append((char)('a'+ (halfbtye - 10)));
                }
                halfbtye = data[i] & 0x0F;
            } while(two_halves++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash = new byte[40];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    private void timeStart(){ timer = System.currentTimeMillis();}

    private String timeStop(){
        timer = System.currentTimeMillis() - timer;
        return String.format("\n%d minutes, \n%d seconds, \n%d milliseconds\n",
                TimeUnit.MILLISECONDS.toMinutes(timer),
                TimeUnit.MILLISECONDS.toSeconds(timer) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timer)),
                timer - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(timer))
        );
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Brute Force");
        frame.setContentPane(new brute_force_GUI().bfpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
