package Portfolio;
import View.View;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Handles user creation, logging in and creating portfolio. Contains
 * the main.
 */
public class UserAuthentication {
    //Todo - file gathered in checkPassword is thrown away but could instead be saved if the
        //Todo checkpassword and getPOFromId were both called in a login function

    /**
     * Takes in plain text password and checks it against portfolio file hashed password
     * @param id The portfolio file that is being checked
     * @param pass the plaintext password to check
     * @return bool true if the hashed plaintext matches the first line in portfolio
     */
    public boolean checkPassword(String id, String pass){
        String hashedPass = hash(pass);
        ArrayList<String> portfolio = getPOFromId(id);
        //check that the portfolio has the same hash
        return (portfolio.get(0).equals(hashedPass));
    }

    /**
     * Reads in a file and returns each line as an arraylist
     * @param id The id / filename of the portfolio that needs to be read in
     * @return arraylist of strings for each line in the file
     */
    public ArrayList<String> getPOFromId(String id){
        ArrayList<String> portfolio = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get("./portfolios/" + id + ".txt"), Charset.defaultCharset())) {
            lines.forEach(portfolio::add);
        } catch(IOException e){
            System.out.println(e.getMessage());
        } catch (SecurityException e){
            //access to the file is denied
            System.out.println(e.getMessage());
        }
        return portfolio;
    }

    /**
     * Responsible for deleting the portfolio file associated with a ggiven id
     * @param id the filename / identifier of the  portfolio to be deleted
     */
    public void deleteId(String id){
        Path path = Paths.get("./portfolios/" + id + ".txt");
        try {
            Files.delete(path);
         } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", path);
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", path);
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }
    }

    /**
     * Creates a new file and sets the password as the  first line in that file
     * @param id the filename / identifier
     * @param pass the plaintext password to be hashed and written in
     */
    public void createId(String id, String pass){
        String filepath = "./portfolios/" + id + ".txt";
        File newFile = new File(filepath);
        if(!newFile.isFile()) {
            ArrayList<String> lines = new ArrayList<>();
            lines.add(hash(pass));
            try {
                Files.createFile(Paths.get("./portfolios/" + id + ".txt"));
                Files.write(Paths.get("./portfolios/" + id + ".txt"), lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * hashes a string using SHA-256  and returns the hex equivalent
     * @param pass the string to be hashed
     * @return the hashed string after it has been converted to hex then a string
     */
    public String hash(String pass){
        try {
            //using Secure Hashing Algorithm 256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //give the digester the password string in UTF-8 encoding
            digest.update(pass.getBytes("UTF-8"));
            //rung the algorithm and save the bytes
            byte[] hash = digest.digest();

            //lookup array for converting the bytes to hex
            char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

            StringBuilder sb = new StringBuilder(hash.length * 2);
            //run through bytes and convert to hex with bit shifting
            for (byte b : hash) {
                sb.append(HEX_CHARS[(b & 0xF0) >> 4]);
                sb.append(HEX_CHARS[b & 0x0F]);
            }
            //return as string
            return sb.toString();
        } catch (NoSuchAlgorithmException e){
            System.out.println(e.getMessage());
        } catch (UnsupportedEncodingException e){
            System.out.println(e.getMessage());
        }
        //the conversion failed
        return null;
    }
    


}