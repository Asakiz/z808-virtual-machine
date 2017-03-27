/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z808;

/**
 *
 * @author Gabriel
 */
public class Simbolos {
    public String simbolo = new String();
    public String definicao = new String();
    public int endereco;
    
    Simbolos(String s,String d, int e){
        simbolo = s;
        definicao =d;
        endereco = e;
    }
    
    public void setElement(String s,String d, int e){
        simbolo = s;
        definicao =d;
        endereco = e;
    }

    
}
