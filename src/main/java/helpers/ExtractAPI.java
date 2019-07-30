package helpers;

import java.util.Set;
import java.util.HashSet;
import org.apache.commons.lang3.StringUtils;

public class ExtractAPI {

    public static String extractBully(String bullyWords, String input) {
        String result = "";
        if (input == null) {
            return result;
        }
        try {
            String keys = "";
            System.out.println("key, bully");
            keys = RestExtract.ExtractFeaturesRest("http://d2dcrc.cse.unsw.edu.au:9091/ExtractionAPI-0.0.1-SNAPSHOT/rest/keyword","sentence",input);
            if (keys.length() > 0) {
                result = findBully(keys,bullyWords);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String findBully(String keys, String bullyWords){
        String result;
        String[] kk = keys.split(",");
        Set<String> bullySet = new HashSet<String>();
        String[] bully = bullyWords.split("\n");
        int i;
        for (String k : kk) {
            System.out.println("key:"+k);
            for(i=0; i<bully.length; i++){
                System.out.println(i + bully[i]);
                if(StringUtils.containsIgnoreCase(k, bully[i])){
                    bullySet.add(k);
                    System.out.println("bully:"+k);
                    break;
                }
            }
        }
        result = String.join(", ", bullySet);
        return result;
    }


}