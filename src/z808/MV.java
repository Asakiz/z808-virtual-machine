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


public class MV {
    
    ULA ula = new ULA();
    public int ax,dx;
    public String [] nomeVar = new String[50];
    public int [] valorVar = new int[50];
    public int nVars=0;
    
    
    
    public void adcVar(String nome, int valor){
        nomeVar[nVars]  = nome;
        valorVar[nVars] = valor;
        nVars++;
    }
    
    public int eVar(String cod){
        for(int j=0;j<nVars;j++)
        {
            if(cod.equals(nomeVar[j]))
                return j;
        }
        return -1;
    }
    
    public boolean eNumero(String s){
        char c[] = s.toCharArray();
        for(int i=0;i<c.length;i++){
            if(!Character.isDigit(c[i])){
                return false;
            }
        }
        return true;
    }
    
    MV(){
        ax =0;
        dx =0;
        ula.SP = 65535;
    }
    
    
    public void executa(String[] codigo) {
        
        for(int i=0;i<codigo.length;i++) {
            
            switch(codigo[i]) {
                
                case "add":
                    i++;
                    if(codigo[i].equals("ax")){
                        ax +=ax;
                    }else 
                    if(codigo[i].equals("dx")){
                        ax +=dx;
                    }else 
                    if(!eNumero(codigo[i])){
                        int index = eVar(codigo[i]);
                        if(index !=-1 ){
                            ax+=valorVar[index];
                        }else{
                            adcVar(codigo[i], 0);
                        }
                    }else{
                        ax += Integer.parseInt(codigo[i]);
                    }
                    i++;
                    
                    if(codigo[i].equals("ax")){
                        ax +=ax;
                    }else 
                    if(codigo[i].equals("dx")){
                        ax +=dx;
                    }else 
                    if(!eNumero(codigo[i])){
                        int index = eVar(codigo[i]);
                        if(index !=-1 ){
                            ax+=valorVar[index];
                        }else{
                            adcVar(codigo[i], 0);
                        }
                    }else {
                        ax += Integer.parseInt(codigo[i]);
                    }
                    break;
                
                case "sub":
                    i++;
                    if(codigo[i].equals("ax")){
                        ax -=ax;
                    }else 
                    if(codigo[i].equals("dx")){
                        ax -=dx;
                    }else 
                    if(!eNumero(codigo[i])){
                        int index = eVar(codigo[i]);
                        if(index !=-1 ){
                            ax-=valorVar[index];
                        }else{
                            adcVar(codigo[i], 0);
                        }
                    }else{
                        ax -= Integer.parseInt(codigo[i]);
                    }
                    i++;
                    
                    if(codigo[i].equals("ax")){
                        ax -=ax;
                    }else 
                    if(codigo[i].equals("dx")){
                        ax -=dx;
                    }else 
                    if(!eNumero(codigo[i])){
                        int index = eVar(codigo[i]);
                        if(index !=-1 ){
                            ax-=valorVar[index];
                        }else{
                            adcVar(codigo[i], 0);
                        }
                    }else{
                        ax -= Integer.parseInt(codigo[i]);
                    }
                    break;
                
                case "mul":
                    i++;
                    if(codigo[i].equals("ax")){
                        ax *=ax;
                    }else 
                    if(codigo[i].equals("dx")){
                        ax *=dx;
                    }else 
                    if(!eNumero(codigo[i])){
                        int index = eVar(codigo[i]);
                        if(index !=-1 ){
                            ax*=valorVar[index];
                        }else{
                            adcVar(codigo[i], 0);
                        }
                    }else{
                        ax *= Integer.parseInt(codigo[i]);
                    }
                    break;
                          
                case "div":
                    i++;
                    if(codigo[i].equals("ax")){
                        ax /=ax;
                    }else 
                    if(codigo[i].equals("dx")){
                        ax /=dx;
                    }else 
                    if(!eNumero(codigo[i])){
                        int index = eVar(codigo[i]);
                        if(index !=-1 ){
                            ax/=valorVar[index];
                        }else{
                            adcVar(codigo[i], 0);
                        }
                    }else{
                        ax /= Integer.parseInt(codigo[i]);
                    }
                    break;
                           
                case "mov":
                    
                    i++;
                    if(codigo[i].equals("ax")){
                        if(codigo[i+1].equals("ax")){
                            ax =ax;
                        }else 
                        if(codigo[i+1].equals("dx")){
                            ax =dx;
                        }else 
                        if(!eNumero(codigo[i+1])){
                            int index = eVar(codigo[i+1]);
                            if(index !=-1 ){
                                ax = valorVar[index];
                            }else{
                                adcVar(codigo[i], 0);
                                ax = 0;
                            }
                        }
                    }else 
                    if(codigo[i+1].equals("dx")){
                        if(codigo[i+1].equals("ax")){
                            dx =ax;
                        }else 
                        if(codigo[i+1].equals("dx")){
                            dx =dx;
                        }else 
                        if(!eNumero(codigo[i+1])){
                            int index = eVar(codigo[i+1]);
                            if(index !=-1 ){
                                dx = valorVar[index];
                            }else{
                                adcVar(codigo[i], 0);
                                dx = 0;
                            }
                        }
                    }else 
                    if(!eNumero(codigo[i])){
                        int index1 = eVar(codigo[i]);
                        if(index1 !=-1 ){
                            if(codigo[i+1].equals("ax")){
                                valorVar[index1] =ax;
                            }else 
                            if(codigo[i+1].equals("dx")){
                                valorVar[index1] =dx;
                            }else 
                            if(!eNumero(codigo[i+1])){
                                int index = eVar(codigo[i+1]);
                                if(index !=-1 ){
                                    valorVar[index1] = valorVar[index];
                                }else{
                                    adcVar(codigo[i+1], 0);
                                    ax = 0;
                                }
                            }
                        }else{
                            adcVar(codigo[i], 0);
                        }
                    }else{
                        ax *= Integer.parseInt(codigo[i]);
                    }
                    break;
                    
                case "jmp":
                    i = Integer.parseInt(codigo[i]);
                    break;
                   
                case "je":
                    if(ax == Integer.parseInt(codigo[i])){
                       i++;
                       i = Integer.parseInt(codigo[i]);
                    }
                break;
                
                case "jnz":
                    if(ax<=0){
                        i = Integer.parseInt(codigo[i]);
                    }
                break;
                
                case "jz":
                    if(ax==0){
                        i = Integer.parseInt(codigo[i]);
                    }
                break;
                
                case "jp":
                    if(ax>=0){
                        i = Integer.parseInt(codigo[i]);
                    }
                break;
            
                case "and":
                   
                    i++;
                
                    if(codigo[i].equals("dx")) {

                        ax = ax & dx;

                        break;
                    }

                    if(codigo[i+1].equals("ax")) {
                        
                        ax = ax & ax;

                        break;
                    }

                   if(eNumero(codigo[i])) {

                        ax = ax & Integer.parseInt(codigo[i]);

                        break;
                    }

                    break;

                case "not":

                    i++;

                    if(codigo[i].equals("ax"))ax = ~ax;
                    else
                    if(codigo[i].equals("dx"))ax = ~dx;
                    else
                    if(eNumero(codigo[i]))ax = ~Integer.parseInt(codigo[i]);

                case "or":

                    i++;

                    if(codigo[i].equals("dx")) {

                         ax = ax ^ dx;

                         break;
                    }

                    if(codigo[i].equals("ax")) {

                        ax = ax ^ ax;

                        break;
                    }

                    if(eNumero(codigo[i])) {

                        ax = ax ^ Integer.parseInt(codigo[i]);

                        break;
                    } 

                case "xor":

                i++;

                if(codigo[i].equals("dx")) {

                    ax = ((~ax & dx) ^ (ax & ~dx));

                    break;
                }

                if(codigo[i].equals("ax")) {

                    ax = ((~ax & ax) ^ (ax & ~ax));

                    break;

                }

                if(eNumero(codigo[i])) {

                    ax = ((~ax & Integer.parseInt(codigo[i])) ^ (ax & ~(Integer.parseInt(codigo[i]))));

                    break;

                }

            }
        
        }
        
    }
    
 
}
