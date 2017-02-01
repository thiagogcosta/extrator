/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extrator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC;
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
    private Model model;
    private Resource modelResource;
    private long tempoInicial = System.currentTimeMillis();
    private ArrayList<String> linksGuardados = new ArrayList();
    private ArrayList<String> linksAcessados = new ArrayList();
    private ArrayList<Dado> mheads = new ArrayList();
    private ArrayList<Dado> mconteudo = new ArrayList();
    private ArrayList<MDado> mRdf = new ArrayList();
    int contador = 0;
    private Pattern pattern;
    private Matcher matcher;
    private static final String SITE_PATTERN = ".*\\.(png|jpg|gif|bmp|pdf|ppt|pptx|jpeg|xml|csv)";
    
    
    public Busca(String u){
        URL = u;
        model = ModelFactory.createDefaultModel();
        String modelUri = URL;
        modelResource = model.createResource(modelUri);
        getPages(URL);
    }
    
    public int getPages(String u){
        Document doc = null;
        Elements links = null;
        Elements header = null;
        
        try {
            if(!linksAcessados.contains(u)){
                doc = Jsoup.connect(u).get();
               
                //**********************************************EXTRAÇÃO METAS*******************************
                try{
                    header = doc.select("meta");
                    
                    for(Element meta : header) {
                        if(meta.attr("property").equals("og:title")||meta.attr("property").equals("og:description")||meta.attr("property").equals("og:url")||meta.attr("property").equals("og:site_name")){
                            Dado d = new Dado("dado",meta.attr("property"),meta.attr("content"),meta.baseUri());
                            mheads.add(d);
                        }
                    }
                }catch(Exception e){
                    System.out.println("Erro ao acessar os metas!");
                }
                
                linksAcessados.add(u);
                
                //********************************************EXTRAÇÃO DE METADADOS*********************************
                try{
                   
                    
                   
                   Elements x = doc.getAllElements();
                   
                   for(Element a: x){
                        if(!a.text().isEmpty()){
                           if(a.tagName().equals("h1")){
                               //System.out.println("texto:"+a.text()+" css:"+a.cssSelector());
                               Dado d = new Dado("metadado", "h1",a.text(),a.baseUri(), a.cssSelector());
                               mconteudo.add(d);
                           }
                           if(a.tagName().equals("h2")){
                               //System.out.println("texto:"+a.text()+" css:"+a.cssSelector());
                               Dado d = new Dado("metadado", "h2",a.text(),a.baseUri(), a.cssSelector());
                               mconteudo.add(d);
                           } 
                           if(a.tagName().equals("h3")){
                               //System.out.println("texto:"+a.text()+" css:"+a.cssSelector());
                               Dado d = new Dado("metadado", "h3",a.text(),a.baseUri(), a.cssSelector());
                               mconteudo.add(d);
                           } 
                           if(a.tagName().equals("h4")){
                               //System.out.println("texto:"+a.text()+" css:"+a.cssSelector());
                               Dado d = new Dado("metadado", "h4",a.text(),a.baseUri(), a.cssSelector());
                               mconteudo.add(d);
                           }
                           if(a.tagName().equals("h5")){
                               //System.out.println("texto:"+a.text()+" css:"+a.cssSelector());
                               Dado d = new Dado("metadado", "h5",a.text(),a.baseUri(), a.cssSelector());
                               mconteudo.add(d);
                           } 
                           if(a.tagName().equals("h6")){
                               //System.out.println("texto:"+a.text()+" css:"+a.cssSelector());
                               Dado d = new Dado("metadado", "h6",a.text(),a.baseUri(), a.cssSelector());
                               mconteudo.add(d);
                           } 
                           if(a.tagName().equals("p")){
                               //System.out.println("texto:"+a.text()+" css:"+a.cssSelector());
                               Dado d = new Dado("dado", "p",a.text(),a.baseUri(), a.cssSelector());
                               mconteudo.add(d);
                           }
                           if(a.tagName().equals("pre")){
                               //System.out.println("texto:"+a.text()+" css:"+a.cssSelector());
                               Dado d = new Dado("dado", "pre",a.text(),a.baseUri(), a.cssSelector());
                               mconteudo.add(d);
                           }
                           if(a.tagName().equals("i")){
                               //System.out.println("texto:"+a.text()+" css:"+a.cssSelector());
                               Dado d = new Dado("dado", "i",a.text(),a.baseUri(), a.cssSelector());
                               mconteudo.add(d);
                           }
                          
                        }
                    }
               
                    
                 //System.out.println("tag: "+a.tagName()+" conteudo: "+a.text());
                
                }catch(Exception e){
                    System.out.println("Erro ao acessar os metadados!");
                }
                
                
                
                //***********************************************EXTRAÇÃO LINKS*******************************
                links = doc.select("a[href]");
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
            System.err.println("Erro em: '" + URL + "': " + e.getMessage());
            contador++;
        }
        
        //************************Só entra aqui quando terminar de extrair tudo************************
        
        if(contador == linksGuardados.size()){
            
            /*for(Dado a: mheads){
               Dado d = new Dado("metadado", a.getTag(),a.getTag(),a.getBUri());
               MDado m = new MDado(a,d);
               mRdf.add(m);
            } 
            
           System.out.println("*****************************************************************************");
            for(Dado x: mconteudo){
                System.out.println("Tipo: "+x.getTipo()+" Tag: "+x.getTag()+" Conteudo: "+x.getConteudo()+" css: "+x.getCss());
            }*/
            System.out.println("*****************************************************************************");
            for(Dado a: mheads){
               Dado d = new Dado("metadado", a.getTag(),a.getTag(),a.getBUri());
               MDado m = new MDado(d,a);
               mRdf.add(m);
            } 
            
            System.out.printf("tamanho mconteudo: "+mconteudo.size());
            
            
            for(int i = 0; i < mconteudo.size(); i++){
                try{
                    if(mconteudo.get(i).getTipo().equals("metadado")&&mconteudo.get(i+1).getTipo().equals("metadado")){
                         Dado d = new Dado("metadado", mconteudo.get(i).getTag(),mconteudo.get(i).getTag(),mconteudo.get(i).getBUri());
                         MDado m = new MDado(d,mconteudo.get(i));
                         
                         //Verificação de unicidade
                         if(getUnicidade(m) == 1){
                            mRdf.add(m);
                         }
                    }else{
                        if(mconteudo.get(i).getTipo().equals("metadado")&&mconteudo.get(i+1).getTipo().equals("dado")){
                             int j = i;
                             j++;
                            while(mconteudo.get(j).getTipo().equals("dado") && j<mconteudo.size()){
                                 //if(j < mconteudo.size()){
                                MDado m = new MDado(mconteudo.get(i),mconteudo.get(j));
                                
                                if(getUnicidade(m) == 1){
                                    mRdf.add(m);
                                }
                                     //System.out.println("J: "+j);
                                 //}
                                 //System.out.println("num: "+i+" Tipo: "+mconteudo.get(i).getTipo()+" Tag: "+mconteudo.get(i).getTag()+" Conteudo: "+mconteudo.get(i).getConteudo()+" css: "+mconteudo.get(i).getCss());
                                 //System.out.println("num: "+j+"***Tipo: "+mconteudo.get(j).getTipo()+" Tag: "+mconteudo.get(j).getTag()+" Conteudo: "+mconteudo.get(j).getConteudo()+" css: "+mconteudo.get(j).getCss());
                                 j++;
                            }

                             i = j-1;
                        }else{
                            Dado d = new Dado("dado", mconteudo.get(i).getTag(),mconteudo.get(i).getTag(),mconteudo.get(i).getBUri());
                            MDado m = new MDado(d,mconteudo.get(i));
                            if(getUnicidade(m) == 1){
                                mRdf.add(m);
                            }
                        }
                    }
                }catch(Exception e){
                    System.out.println("Erro ao indexar MDado!");
                }
            }
            
            
            for(MDado a: mRdf){
                
                System.out.println("uri: "+a.getMetadado().getBUri()+" tipo: "+a.getMetadado().getTipo()+" descricao: "+a.getMetadado().getConteudo()+" tipo: "+a.getDado().getTipo()+" descricao: "+a.getDado().getConteudo());
                try {
                    //org.apache.log4j.BasicConfigurator.configure(new NullAppender());
                    modelResource.addProperty(DC.identifier, model.createResource(a.getMetadado().getBUri())
                            .addProperty(DC.identifier, model.createResource()      
                            .addProperty(DC.title, a.getMetadado().getConteudo())
                            .addProperty(DC.description, a.getDado().getConteudo()))
                                    
                    );
                }catch(Exception e){
                    System.out.println("excessao rdf: "+e);
                }
            }
            
            model.write(System.out);
             
            long tempofinal = (System.currentTimeMillis() - tempoInicial)/1000;
            
            System.out.println("tempo final: "+tempofinal);
            return 1;
        }else{
            /*System.out.println("Tamanho dos linksAcessados: "+linksAcessados.size());
            System.out.println("Tamanho dos linksGuardados: "+linksGuardados.size());*/
                
            return getPages(linksGuardados.get(contador));
            
        }
    }
    
    public int getUnicidade(MDado m){
        for(MDado t: mRdf){
            if((t.getDado().getTipo().equals(m.getDado().getTipo()))&&(t.getDado().getTag().equals(m.getDado().getTag()))&&(t.getDado().getConteudo().equals(m.getDado().getConteudo()))&&(t.getMetadado().getTipo().equals(m.getMetadado().getTipo()))&&(t.getMetadado().getTag().equals(m.getMetadado().getTag()))&&(t.getMetadado().getConteudo().equals(m.getMetadado().getConteudo()))){
                return 0;
            }   
        }
        return 1;
    }
}
