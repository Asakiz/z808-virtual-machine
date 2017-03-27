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
public class Instrucao {
    String[] cod = new String[23] ;
    String[] codigo = new String [23];
            
    Instrucao(){
        cod[0] = "mov";
        codigo[0] = "8B";
        
        cod[1] = "push";
        codigo[1] = "50";
        
        
        cod[2] = "pop";
        codigo[2] = "58";
        
        
        //Aritméticas
        
        cod[5] = "add";
        codigo[5] = "03";
        
        
        cod[6] = "sub";
        codigo[6] = "2B";
        
        
        cod[7] = "mul"; //ACC + OP 
        codigo[7] = "F7";
        
        
        cod[8] = "div";
        codigo[8] = "F7";
        
        
        cod[9] = "cmp";
        codigo[9] = "3D" ;
        
        //Logicas
        cod[11] = "and";
        codigo[11] = "23";
        
        
        cod[12] = "or";
        codigo[12] = "0B";
        
        
        cod[13] = "xor";
        codigo[13] = "33";
        
        
        cod[14] = "not";
        codigo[14] = "F7";
        
        
        cod[17] = "jmp";
        codigo[17] = "EB";
        
        
        cod[18] = "je";
        codigo[18] = "74";
        
        
        cod[19] = "jz";
        codigo[19] = "74";
        
        
        cod[20] = "jnz";
        codigo[20] = "75";
        
    }
    
    public int isInstrucao(String s){
        int id = -1;
        for(int i=0;i<cod.length;i++){
            if(cod[i].equals(s)){
                id = i;
                i = 25;   
            }
        }
        return id;
    }
    
    
}
