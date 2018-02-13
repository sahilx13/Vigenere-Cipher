import java.util.*;
import edu.duke.*;
import java.io.File;
public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        String s = "";
        for (int i = whichSlice; i<message.length(); i += totalSlices) {
            s += message.charAt(i);
        }
        return s;
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        CaesarCracker cc = new CaesarCracker(mostCommon);
        for (int i=0; i<klength; i++) {
            String s = sliceString(encrypted, i, klength);
            int k = cc.getKey(s);
            key[i] = k;
        }
        return key;
    }
    public HashSet<String> readDicitionary(FileResource fr) {
        HashSet<String> wordSet = new HashSet<String>();
        for (String word : fr.words()) {
            word = word.toLowerCase();
            wordSet.add(word);
        }
        return wordSet;
    }
    public int countWords(String message, HashSet<String> dictionary) {
        int count = 0;
        for (String word : message.split("\\W")) {
            word = word.toLowerCase();
            if(dictionary.contains(word)) {
                count ++;
            }
        }
        return count;
    }
    public char mostCommonCharIn(HashSet<String> dictionary) {
        int[] charcnt = new int[Character.MAX_VALUE + 1];
        char maxchar = ' ';
        int maxcnt = 0;
        for (String word : dictionary) {
            for (int i = word.length() - 1; i >= 0; i--) {
                char ch = word.charAt(i);
                if (++charcnt[ch] >= maxcnt) {
                    maxcnt = charcnt[ch];
                    maxchar = ch;
                }
            }
        }
        return maxchar;
    }
    public void breakForAllLanguages(String encrypted, HashMap<String, HashSet<String>> languages) {
        int maxCount = 0;
        String lang = "";
        String decrypted = "";
        for (String language : languages.keySet()) {
            System.out.println("Checking for "+language);
            HashSet<String> dictionary = languages.get(language);
            String tempDecrypted = breakForLanguage(encrypted, dictionary);
            int tempCount = countWords(tempDecrypted, dictionary);
            if (tempCount > maxCount) {
                maxCount = tempCount;
                lang = language;
                decrypted = tempDecrypted;
            }
        }
        System.out.println("Language chosen : "+lang);
        System.out.println("Words matched : "+maxCount);
        System.out.println("Decrypted message : "+decrypted);
    }
    public String breakForLanguage(String encrypted, HashSet<String> dictionary) {
        int max = -1;
        String decrypted = "";
        char mostCommonChar = mostCommonCharIn(dictionary);
        int keylength = -1;
        for (int i=1 ; i<=100 ; i++) {
            int key[] = tryKeyLength(encrypted, i, mostCommonChar);
            VigenereCipher vc = new VigenereCipher(key);
            String tempDecrypted = vc.decrypt(encrypted);
            int count = countWords(tempDecrypted, dictionary);
            if(count > max) {
                max = count;
                decrypted = tempDecrypted;
                keylength = i;
            }
        }
        System.out.println("Key found for given language : "+Arrays.toString(tryKeyLength(encrypted, keylength, mostCommonChar)));
        System.out.println("valid words : "+max);
        System.out.println("Key length : "+keylength);
        return decrypted;
    }
    public void breakVigenere () {
        FileResource fr = new FileResource();
        String encrypted = fr.asString();
        HashMap<String, HashSet<String>> languages = new HashMap<String, HashSet<String>>();
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource fr1 = new FileResource(f);
            languages.put(f.getName(), readDicitionary(fr1));
            System.out.println("Read dictionary for "+f.getName());
        }
        breakForAllLanguages(encrypted, languages);
    }
    
}
