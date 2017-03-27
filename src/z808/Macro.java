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
public class Macro {
    public String nome;
    public String[] cod;
    public int end;
    
    Macro(String n, String[] c, int e){
        nome = n;
        System.out.println("Criando macro: "+n);
        
        int tamanho = 0;
        for(;!c[tamanho].equals("\0");tamanho++);
        cod = new String[tamanho+1];
        
        System.out.println("Escopo:");
        
        for(int i=0;i<c.length-1 && !c[i].equals("\0");i++){
            cod[i] = c[i];
            System.out.println(cod[i]);
        }
        
        end = e;
        
        System.out.println("Salva no endereco:" + e);
    }
    
    public void Macro(String n, String[] c, int e){
        nome = n;
        System.out.println("Criando macro: "+n);
        
        int tamanho = 0;
        for(;!c[tamanho].equals("\0");tamanho++);
        cod = new String[tamanho+1];
        
        System.out.println("Escopo:");
        
        for(int i=0;i<c.length && !c[i].equals("\0");i++){
            cod[i] = c[i];
            System.out.println(cod[i]);
        }
        
        end = e;
        
        System.out.println("Salva no endereco:" + e);
    }
    
}
