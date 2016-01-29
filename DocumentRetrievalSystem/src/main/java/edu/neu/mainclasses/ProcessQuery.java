package edu.neu.mainclasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class ProcessQuery {
	private static TreeMap<String,TreeMap<String,Integer>> invindex = new TreeMap<String,TreeMap<String,Integer>>();
    private static TreeMap<String,Integer> countTokens = new TreeMap<String,Integer>();
    
    
	public LinkedList<Result> processQuery() throws IOException, ClassNotFoundException
	{

    FileReader freader = new FileReader("C:\\Users\\Anudeep\\Documents\\myfolder\\stopwords.txt");
    BufferedReader breader = new BufferedReader(freader);
    String l = breader.readLine();
    
    LinkedList<String> stopwordslist = new LinkedList<String>();
    
    while(l!=null)
    {
        stopwordslist.add(l);
        l = breader.readLine();
    }
    System.out.println("Stopwords list "+stopwordslist);
    
    File myfolder = new File("C:\\Users\\Anudeep\\Documents\\mytextfiles");
    File[]  fileslist = myfolder.listFiles();
    
    //Iterate over the all given files in the folder
    for(File file:fileslist)
    { 
        int wordcount = 0;
        System.out.println("File name "+file.getName());
        
        FileReader filereader = new FileReader(file);
        BufferedReader bufferreader = new BufferedReader(filereader);
        
        String line = bufferreader.readLine();
        String str = "";
        //Read each line from the file and assign it to a single string
       while(line!=null)
       {
           str = str+" "+line;
           System.out.println("line "+str);
           line = bufferreader.readLine();
                   
           
       }
       System.out.println("total line "+str);
       
       PorterStemmer stemmer = new PorterStemmer();
       //Tokenize all the words  in each file (ommiting all whitespace and alphanumeric characters
       
       StringTokenizer stz = new StringTokenizer(str," ,.~!@#$%^&*()_+{}:[]/';");
       String[] tokens = new String[stz.countTokens()];
       
      
       while(stz.hasMoreTokens())
       {
           String token = stz.nextToken();
           System.out.println("token "+token);
           //Eliminating all the stopwords by comparing with the corpus
           if(!stopwordslist.contains(token))
           {
               
              //Perform Stemming
               String stemmedword = stemmer.stem(token);
               System.out.println("stemmed word "+stemmedword);
              
               wordcount = wordcount+1;
               //Counting total words in each file
               countTokens.put(file.getName().substring(0, file.getName().length()-4),wordcount);
               if(!invindex.containsKey(stemmedword))
               {
                   System.out.println("New Word "+stemmedword);
                   TreeMap<String,Integer>  nestedmap = new TreeMap<String,Integer>();
                   nestedmap.put(file.getName().substring(0, file.getName().length()-4),1);
                   invindex.put(stemmedword,nestedmap);
               }
               else
               {
                   System.out.println("repeated word "+stemmedword);
                   TreeMap<String,Integer> hm = invindex.get(stemmedword);
                   
                 
                  if(hm.containsKey(file.getName().substring(0, file.getName().length()-4)))
                  {
                   int occurence = hm.get(file.getName().substring(0, file.getName().length()-4));
                   hm.put(file.getName().substring(0, file.getName().length()-4), (occurence+1));
                   invindex.put(stemmedword,hm);
                  }
                  else
                  {
                    System.out.println("new in the file "+stemmedword);
                   hm.put(file.getName().substring(0, file.getName().length()-4),1);
                   invindex.put(stemmedword,hm);
                  }
               }
                   
               
           }
       }
       
       
       
        
        
        
        
    }
    
    for(Map.Entry<String,TreeMap<String,Integer>> entries:invindex.entrySet())
    {
       
       TreeMap<String,Integer> hmp = entries.getValue();
       for(Map.Entry<String,Integer> e:hmp.entrySet())
       {
           System.out.println("[Term:]  "+entries.getKey()+" [File name:]  "+e.getKey()+" [Occurence]  "+e.getValue());
          
       }
    }
    
    for(Map.Entry<String,Integer> mapentries:countTokens.entrySet())
    {
      System.out.println("File name is "+mapentries.getKey()+" Wordcount "+mapentries.getValue());
    }
    
    FileOutputStream outfile = new FileOutputStream("C:\\Users\\Anudeep\\Documents\\indexfile\\index.out");
    ObjectOutputStream outputfile = new ObjectOutputStream(outfile);
    
    outputfile.writeObject(invindex);
    outputfile.writeObject(countTokens);
    outputfile.close();
    outfile.close();
     
  PageRanking bm25 = new PageRanking();
  LinkedList<Result> resultlist=bm25.computeRank("index.out", "query.txt", "10");
return resultlist;
   
    
    }
}


