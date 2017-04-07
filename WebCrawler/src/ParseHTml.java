

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/*
 * 1) Save web-page content in file.
 * 2) Seprate all link from save page.
3) Crawle all link one by one, and save all crawle link pages.
4) All link should only crawle at one time.
 * */

public class ParseHTml {

	private static List<String> linksList = new ArrayList<>();
	private static List<String> finalLinksList = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String urlPath = "https://www.tutorialspoint.com/mahout/mahout_machine_learning.htm";
//
//		ParseHTml parse = new ParseHTml();
//		File file = parse.crawlePage(urlPath);
//		List links = parse.seprateLinks(file, urlPath);
//		links.forEach(link -> parse.extractHttpBasedLinks(link.toString()));
		ParseHTml parse = new ParseHTml();
		parse.initCrawler(urlPath);
		
	}

	
	public void initCrawler(String path) throws IOException{
		String urlPath = path;
		 List<String> parssedList = new ArrayList<>();
		ParseHTml parse = new ParseHTml();
		File file = parse.crawlePage(urlPath);
		List<Object> list =  parse.seprateLinks(file, urlPath).stream().filter(link -> Objects.nonNull(link)&&!parse.processLink(link.toString(),parssedList).equals("")).collect(Collectors.toList());
		
		parssedList.forEach(l -> {
			
			    if(!finalLinksList.contains(l)){
				try {
					System.out.println(l);
					finalLinksList.add(l);
					initCrawler(l);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    }
			
		});
		
		
	}
	
	
    private String processLink(String link,List<String> parssedList){
    	ParseHTml parse = new ParseHTml();
    	if(!linksList.contains(link)){
			
			String url = parse.extractHttpBasedLinks(link,parssedList);
			linksList.add(url);
			return url;
    	}
    	
    	return "";
    } 
	
	
	public File crawlePage(String path) throws IOException {
		URL url = new URL(path);
		InputStream io = url.openStream();
		// Lis list = (List) Arrays.asList(path.split("/"));
		List list = Arrays.asList(path.split("/"));
		File file = new File((String) list.get(list.size() - 1));
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(io));
		FileOutputStream fileOutputStream = new FileOutputStream(file, true);
		PrintWriter pr = new PrintWriter(fileOutputStream, true);
		String str = "";
		while ((str = bufferedReader.readLine()) != null) {
			pr.println(str);
      	}
		return file;

	}

	public List<Object> seprateLinks(final File file, String urlPath) throws IOException {

		System.out.println(file.getAbsolutePath());
		
		Document doc = Jsoup.parse(file, "UTF-8", urlPath);

		Elements links = doc.select("a[href]"); 
	
		List<Object> extractList = new ArrayList<>();
		
		extractList.addAll(links);
		
		return extractList;

	}

	public String extractHttpBasedLinks(String link,List<String> parssedList) {

		Pattern pattern = Pattern.compile("href=\"(.*?)\"");
		Matcher matcher = pattern.matcher(link);
		if (matcher.find()) {
			String s = matcher.group(1).toString();
			if (s.startsWith("http") && s.contains("tutorialspoint.com") )
				parssedList.add(s);
				return s;
		}

		return "";
	}

}
