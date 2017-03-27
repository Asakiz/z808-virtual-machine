/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z808;

import java.util.ArrayList;

/**
 *
 * @author Gabriel
 */
public class Expansor {
    public String nome;
    public ArrayList<String> parametros;
    public String codigo;
    
    
    Expansor () 
    {
        parametros = new ArrayList<String>();
        codigo = "";
    }
    
    public void criarMacro(String n, ArrayList<String> al, String c){
        nome = n;
        parametros = al;
        codigo = c;
    }
    
}
