/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extrator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author thiago
 */
public class Busca {
    String URL;
    ArrayList<String> linksGuardados = new ArrayList();
    ArrayList<String> linksAcessados = new ArrayList();
    int contador = 0;
    private Pattern pattern;
    private Matcher matcher;
    private static final String SITE_PATTERN = ".*\\.(png|jpg|gif|bmp|pdf|ppt|pptx|jpeg|xml|csv)";
    
    
    public Busca(String u){
        URL = u;
        getPages(URL);
    }
    
    public int getPages(String u){
        Document doc = null;
        Elements links = null;
        try {
            if(!linksAcessados.contains(u)){
                doc = Jsoup.connect(u).get();
                links = doc.select("a[href]");

                linksAcessados.add(u);
                 
                for (Element a: links) {
                    
                    //verifico se não tem os formatos da excessão
                    pattern = Pattern.compile(SITE_PATTERN);
                    matcher = pattern.matcher(a.attr("abs:href"));
                     
                    if(!linksGuardados.contains(a.attr("abs:href"))){
                        if(a.attr("abs:href").contains(URL) && !matcher.find()){
                            linksGuardados.add(a.attr("abs:href"));
                            System.out.println("links: "+a.attr("abs:href"));
                        }
                    }
                } 
            }
        contador++;
        } catch (IOException e) {
            System.err.println("For '" + URL + "': " + e.getMessage());
            contador++;
        }
        if(contador == linksGuardados.size()){
                return 1;
        }else{
            System.out.println("Tamanho dos linksAcessados: "+linksAcessados.size());
            System.out.println("Tamanho dos linksGuardados: "+linksGuardados.size());
            return getPages(linksGuardados.get(contador));
        }
    }
}
