package edu.neu.mainclasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class PageRanking {
	
	 public static Map<String, TreeMap<String, Integer>> inv_index = new TreeMap<String, TreeMap<String, Integer>>();
	    public Map<String, Integer> tokenCount = new TreeMap<String, Integer>();
	    public static Map<String, TreeMap<String, Integer>> query_index = new TreeMap<String, TreeMap<String, Integer>>();
	    public static Map<String, Double> BM25 = new TreeMap<String, Double>();
	    public final Double k1 = 1.2;
	    public final Double k2 = 100.0;
	    public final Double b = 0.75;
	    public final Double ri = 0.0;
	    public final Double R = 0.0;
	    
	    private LinkedList<Result> resultlist = new LinkedList<Result>();
	    public class DescendingOrder implements Comparator<String> {
	        Map<String, Double> map = new TreeMap<String, Double>();
	        public DescendingOrder(TreeMap map) {
	            this.map = map;
	        }
	        public DescendingOrder() {
	            super();
	        }
	        @Override
	        public int compare(String o1, String o2) {
	            if (map.get(o1) >= map.get(o2)) {
	                return -1;
	            } else {
	                return 1;
	            }
	        }
	    }
	    public LinkedList<Result> computeRank(String indexfile, String queryfile, String num) throws IOException, ClassNotFoundException {
//	        try {
	        int count = Integer.parseInt(num);
	        //String cur_dir = System.getProperty("user.dir");
	        FileInputStream fileIn = new FileInputStream("C:\\Users\\Anudeep\\Documents\\indexfile\\"+indexfile);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        inv_index = (TreeMap) in.readObject();
	        System.out.println(inv_index);
	        if (inv_index == null) {
	            //  System.out.println("invindex is null");
	        }
	        tokenCount = (TreeMap) in.readObject();
	        if (tokenCount == null) {
	            // System.out.println("tokenCount is null");
	        }
	        System.out.println(tokenCount);
	        int totalTokenCount = 0;
	        //Calculate the total number of tokens in the collection
	        for (Iterator i = tokenCount.entrySet().iterator(); i.hasNext();) {
	            Map.Entry next = (Map.Entry) i.next();
	            totalTokenCount = totalTokenCount + (Integer) next.getValue();
	            //System.out.println(next.getKey() + "!!!!!!!!!!!!!" + next.getValue());
	        }
	        Double avdl = totalTokenCount * 1.0 / tokenCount.size();
	        //Reading a file
	        File qf = new File("C:\\Users\\Anudeep\\Documents\\indexfile\\"+queryfile);
	        BufferedReader bf = new BufferedReader(new FileReader(qf));
	        String querytext;
	        int queryid = 1;
	        while ((querytext = bf.readLine()) != null) {
	            String[] querywords = querytext.split(" ");
	            //Step1: Retrieve all inverted lists corresponding to terms in a query.
	            for (String word1 : querywords) {
	                PorterStemmer ps = new PorterStemmer();
	                String word = ps.stem(word1);
	                System.out.println("my search word is " + word);
	                word = word.trim();
	                if (!word.equals("") && inv_index.containsKey(word)) {
	                    //System.out.println("in step one");
	                    query_index.put(word, inv_index.get(word));
	                    //System.out.println("in the step 1 of reteirval" + inv_index.get(word));
	                }
	            }
	            //Step2: Compute BM25 scores for documents in the lists.
	            for (Iterator iterator1 = query_index.entrySet().iterator(); iterator1.hasNext();) {
	                Map.Entry next = (Map.Entry) iterator1.next();
	                TreeMap indexes = (TreeMap) next.getValue();
	                for (Iterator iterator2 = indexes.entrySet().iterator(); iterator2.hasNext();) {
	                    Map.Entry next2 = (Map.Entry) iterator2.next();
	                    //    System.out.println(next2);
	                    int fi = (Integer) next2.getValue();
	                    //  System.out.println("hi" + next2.getKey() + " fi" + fi);
	                    int N = tokenCount.size();
	                    int ni = indexes.size();
	                    //System.out.println("sizeee" + ni);
	                    Double qfi = 0.0;
	                    for (int i = 0; i < querywords.length; i++) {
	                        if (querywords[i].equalsIgnoreCase((String) next.getKey())) {
	                            qfi++;
	                        }
	                    }
	                    //Computing K value
	                    Double K = k1 * ((1 - b) + b * (tokenCount.get(next2.getKey()) / avdl));
	                    Double first_term = (Math.log(((ri + 0.5) / (R - ri + 0.5))
	                            / ((ni - ri + 0.5) / (N - ni - R + ri + 0.5))));
	                    Double second_term = ((k1 + 1) * fi / (K + fi));
	                    Double third_term = ((k2 + 1) * qfi / (k2 + qfi));
	                    Double total = first_term * second_term * third_term;
	                    if (BM25.containsKey((String) next2.getKey())) {
	                        Double valueToPut = total + BM25.get((String) next2.getKey());
	                        BM25.put((String) next2.getKey(), valueToPut);
	                    } else {
	                        BM25.put((String) next2.getKey(), total);
	                    }
	                }
	            }
	            DescendingOrder comp = new DescendingOrder((TreeMap) BM25);
	            TreeMap<String, Double> list_asc = new TreeMap<String, Double>(comp);
	            list_asc.putAll(BM25);
	            int rank = 1;
	            for (Iterator itr = list_asc.entrySet().iterator(); itr.hasNext() && rank <= count;) {
	                Map.Entry nx = (Map.Entry) itr.next();
	                Double bm25value = (Double) nx.getValue();
	                System.out.println("in output loop");
	                Result result = new Result();
	                result.setQueryid(queryid);
	                result.setRank(rank);
	                result.setFilename((String)nx.getKey());
	                result.setBm25score(bm25value);
	                
	                resultlist.add(result);
	                
	                
	                System.out.println(queryid + " Q0 " + nx.getKey() + " " + rank + " " + bm25value + " Dasari");
	                rank++;
	            }
	            queryid++;
	            BM25.clear();
	            query_index.clear();
	        }
	        in.close();
	        fileIn.close();
	        bf.close();
	        
	        return resultlist;
	    }

}
