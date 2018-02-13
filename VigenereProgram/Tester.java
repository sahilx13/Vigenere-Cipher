
/**
 * Write a description of Tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import java.util.*;
public class Tester {
    public void testCaesarCipher() {
        CaesarCipher cc = new CaesarCipher(2);
        FileResource fr = new FileResource();
        String input = fr.asString();
        System.out.println(cc.encrypt(input));
        System.out.println(cc.encryptLetter('a'));
    }
    public void testCaesarCracker() {
        CaesarCracker cc = new CaesarCracker('a');
        FileResource fr = new FileResource();
        String input = fr.asString();
        int key = cc.getKey(input);
        CaesarCipher cc1 = new CaesarCipher(26 - key);
        System.out.println(cc1.encrypt(input));
    }
    public void testVigenereCipher() {
        int []cipher = {17,14,12,4};
        VigenereCipher vc = new VigenereCipher(cipher);
        FileResource fr = new FileResource();
        String input = fr.asString();
        String encrypted = vc.encrypt(input);
        System.out.println(encrypted);
        System.out.println(vc.decrypt(encrypted));
    }
    public void testKeyLength() {
        VigenereBreaker vb = new VigenereBreaker();
        FileResource fr = new FileResource();
        String encrypted = fr.asString();
        FileResource fr1 = new FileResource();
        HashSet<String> dictionary = vb.readDicitionary(fr1);
        int key[] = vb.tryKeyLength(encrypted, 38, 'e');
        VigenereCipher vc = new VigenereCipher(key);
        String decrypted = vc.decrypt(encrypted);
        System.out.println("Valid words : "+vb.countWords(decrypted, dictionary));
    }
    public void testVigenereBreaker() {
        VigenereBreaker vb = new VigenereBreaker();
        vb.breakVigenere();
    }
}
