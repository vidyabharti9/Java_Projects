import java.io.*;
import java.util.*;

public class Analysis{
    public static void main(String args[]) throws IOException {
        try {
            Map<String, String> map = new HashMap<>();
            FileReader fr = new FileReader("newScore.txt");
            Scanner scnr = new Scanner(fr);
            scnr.nextLine();
            String l;
            while (scnr.hasNextLine()) {
                l = scnr.nextLine();
                String[] parts = l.split("\t");
                map.put(parts[0], parts[1]);
            }
            scnr.close();
            int correctPrediction = 0;
            FileReader dataset = new FileReader("IMDB Dataset.csv");
            Scanner sc = new Scanner(dataset);
            sc.nextLine();
            ArrayList<String> al;
            StringTokenizer tokenizer;
            String sen;
            String row;
            String sentiment;
            while (sc.hasNextLine()) {
                row = sc.nextLine();
                if (row.length() < 8){
                    continue;
                }
                sen = row.substring(0, row.length() - 8);
                sentiment = row.substring(row.length()- 8, row.length());
                al = new ArrayList<>();
                tokenizer = new StringTokenizer(sen, " \t\n\r\f,.!?:;\"()[]{}<>");
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken().toLowerCase();
                    al.add(token);
                }
                float tweetscore = 0;
                int i = 0;
                String word;
                while (i < al.size()){
                    word = al.get(i);                    
                    if ("not".equals(word) || "nor".equals(word) || "neither".equals(word) ||  "none".equals(word) || "can't".equals(word) || "won't".equals(word) || "don't".equals(word) || "didn't".equals(word)) {
                        if (i < al.size() - 1) i++;
                        word = al.get(i);
                        if (map.containsKey(word)) {
                            String wordScore = map.get(word);
                            float score = Float.parseFloat(wordScore);
                            tweetscore -= score;
                        }
                    }
                    else {
                        if (map.containsKey(word)) {
                            String wordScore = map.get(word);
                            float score = Float.parseFloat(wordScore);
                            tweetscore += score;
                        }
                    }
                    i++;
                }
                String prediction;
                if (tweetscore >= 0) {
                    prediction = "positive";
                }
                else {
                    prediction = "negative";
                }
                if (prediction.equals(sentiment)) {
                        correctPrediction += 1;
                }
            }
            sc.close();
            System.out.println("Accuracy: " + correctPrediction / 500 + "%");
            // System.out.println(correctPrediction);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}