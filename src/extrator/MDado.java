/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extrator;

import java.util.ArrayList;

public class MDado {
    
    //Atributo
    private Dado metadado;
    private Dado dado;  
   
    //construtor
    public MDado(Dado m,Dado d){
        setMetadado(m);
        setDado(d);
    }
    
    //Método
    public Dado getMetadado(){
        return metadado;
    }
    
    public Dado getDado(){
        return dado;
    }
    
    public void setMetadado(Dado m){
        this.metadado = m;
    }
    
    public void setDado(Dado d){
        this.dado = d;
    }
}
