package edu.neu.textretrieval;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.neu.mainclasses.ProcessQuery;
import edu.neu.mainclasses.Result;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping("/myquery")
	public String getQueryResults(Model model,@RequestParam("queryname")String queryname) throws ClassNotFoundException, IOException
	{
		
		
		File file = new File("C:\\Users\\Anudeep\\Documents\\indexfile\\query.txt");
		FileWriter fwriter = new FileWriter(file);
		BufferedWriter buffwriter = new BufferedWriter(fwriter);
		buffwriter.write(queryname);
		buffwriter.close();
		
		ProcessQuery pq = new ProcessQuery();
		LinkedList<Result> resultlist=pq.processQuery();
		model.addAttribute("resultlist", resultlist);
		
		return "displayresults";
	}
	
	@RequestMapping("/displaypage")
	public String getFile(Model model,@RequestParam("page")String page) throws IOException
	{
		File file = new File("C:\\Users\\Anudeep\\Documents\\mytextfiles\\"+page+".txt");
		FileReader filereader = new FileReader(file);
		BufferedReader bufferreader = new BufferedReader(filereader);
		
		String line = bufferreader.readLine();
		
		String text = "";
		
		while(line!=null)
		{
			text=text+""+line;
			line = bufferreader.readLine();
		}
		bufferreader.close();
		
		System.out.println(text);
		model.addAttribute("textinfo", text);
		
		return "displaypage";
		
	}
	
}
