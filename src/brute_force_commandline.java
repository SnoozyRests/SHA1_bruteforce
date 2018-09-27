/*
Author: Jacob John Williams
Function: Generates and encodes in order to crack a supplied encoded word in SHA1.
Date Created: 11 / 10 / 2017
Date Modified: 23 / 09 / 2018
                Reviewed methodology.
                Intention to upload to github to help others learning Cryptography.
Notes: n/a
 */
import java.io.UnsupportedEncodingException;
import java.nio.channels.SelectableChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class brute_force_commandline {
    static boolean fin;
    static long timer = 0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Please input the value of your function: \n1. Encode\n2. Decode\n3. Exit");
            int func = sc.nextInt();

            if(func == 1){
                hashWord();
            } else if(func == 2){
                bruteForce();
            }else if (func == 3 ){
                break;
            } else {
                System.out.println("Invalid Input.");
            }
        }
    }

    private static void bruteForce(){

            System.out.println("Please enter the hash to decode: ");
            String input = sc.next();
            int pos = 0;
            String str = "abcdefghijklmnopqrstuvwxyz0123456789";
            System.out.println("Beginning Decode, this could take multiple minutes.");
            fin = false;
            timeStart();
            for(int i = 1; i < 7; i++) {
                if (!fin) {
                    try {
                        char index[] = new char[i];
                        permutation(index, pos, str, input);
                    } catch (Exception e) {
                        //System.err.println(e);
                    }
                }
            }
            timeStop();
    }

    private static void permutation(char[] index, int pos, String str, String input) throws NoSuchAlgorithmException,
            UnsupportedEncodingException{
        String passTest, passTestHash;

            if (pos == index.length && !fin) {
                passTest = new String(index);
                passTestHash = SHA1(passTest);

                if (passTestHash.equals(input)) {
                    System.out.println("The encoded word was: " + passTest);
                    fin = true;
                }
                //This will print each step it checks but drastically increase runtime.
                //System.out.println(passTest);
            } else {
                //if not found continue cycling string positions
                for (int i = 0; i < str.length(); i++) {
                    index[pos] = str.charAt(i);
                    permutation(index, pos + 1, str, input);
                }
            }
    }

    private static void hashWord(){
        System.out.println("Please enter the word to encode: ");
        String input = sc.next();

        try{
            input = SHA1(input);
        } catch(NoSuchAlgorithmException | UnsupportedEncodingException e){
            //System.err.println(e);
        }
        System.out.println("\nYour word encoded is: \n" + input);
    }

    /*
    -Function to covert byte value to hex value.
    -Supplied by Rong Yang.
    */
    private static String convertToHex(byte[] data){
        StringBuffer buf = new StringBuffer();
        for (byte aData : data) {
            int halfbyte = (aData >>> 4) & 0x0F;
            int two_halves = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = aData & 0x0F;
            } while (two_halves++ < 1);
        }
        return buf.toString();
    }

    /*
    -Converts the string inputted into a hashed string, passes to covert to
        hex afterward.
    -Supplied by Rong Yang.
    */
    static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] hash;
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        hash = md.digest();
        return(convertToHex(hash));
    }


    private static void timeStart(){
        timer = System.currentTimeMillis();
    }

    private static void timeStop(){
        timer = System.currentTimeMillis() - timer;
        System.out.println(String.format("\n%d minutes, \n%d seconds, \n%d milliseconds\n",
                TimeUnit.MILLISECONDS.toMinutes(timer),
                TimeUnit.MILLISECONDS.toSeconds(timer) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timer)),
                timer - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(timer))
                ));
    }


}
