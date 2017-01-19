/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extrator;

import java.util.HashSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Busca {
    
    //Atributos
    private HashSet<String> links;
    
    //Construtor
    public Busca(){ 
        links = new HashSet<String>();
    }
    
    public void getPageLinks(String URL) {
       
        if (!links.contains(URL)) {
            try {
                if (links.add(URL)) {
                    System.out.println(URL);
                }
                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                //3. Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[href]");

                //5. For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"));
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        //1. Pick a URL from the frontier
        new BasicWebCrawler().getPageLinks("http://www.mkyong.com/");
    }
    
    
  
}
