/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z808;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 *  
 * @author Gabriel
 */
public class Montador {
    public MV mv = new MV();
    public String tabelaSimbolos[][]=new String[500][3];
    public int elementosTabela = 0;
    public int enderecoTabela = 1;
    public Macro[] macro = new Macro[25];
    public int nMacros = 0;
    
    public void adcMacro(String nome, String [] s){
        Macro m = new Macro(nome,s,nMacros);
        macro[nMacros]=m;
        nMacros++;
    }
    
    public int enderecoMacro(String m){
        int id=-1;
        System.out.println("Procurando pela Macro: "+m+"\nNa lista:");
        for(int i=0;i<nMacros;i++){
            System.out.println(macro[i].nome);
            if(macro[i].nome.equals(m))
                id=i;
        }
        System.out.println("id: "+id);

        return id;
    }
    
    public boolean eMacro(String m){
        System.out.println("Já foi definida?");
        for(int i=0;i<nMacros;i++){
            System.out.println(macro[i].nome);
            if(macro[i].nome.equals(m)){
                System.out.println("Sim!");        
                return true;
            }
                
        }
       
        System.out.println("Não!");

        return false;
    }
    
    public void colocarNaTabelaDeSimbolos(String simbolo,String modo){
        tabelaSimbolos[elementosTabela][0] = simbolo;
        tabelaSimbolos[elementosTabela][1] = modo;
        tabelaSimbolos[elementosTabela][2] = ""+enderecoTabela;
        enderecoTabela +=3;
        elementosTabela +=1;
        
    }
    
    public int ultimoEndereco(){
        return enderecoTabela-3;
    }
    
    public boolean procurarNaTabela(String nome){
        System.out.println("Procurando na TS por: "+nome+"\nNa lista:");
        for(int i =0; i<elementosTabela;i++){
            System.out.println(tabelaSimbolos[i][0]);
            if(tabelaSimbolos[i][0].equals(nome))
                return true;
        }
        return false;
        
    }
    
    public int enderecoNaTabela(String nome){
        int id=-1;
        for(int i=0;i<elementosTabela;i++){
            if(tabelaSimbolos[i][0].equals(nome))
               id = i;
        }
        System.out.println("id: " +id);
        return id;
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
    
    Montador(){}
    
    Montador(MV m){
        mv = m;
    }
    
    public void montarCodigo(String[] codigo)throws IOException {
        System.out.println("Codigo Original: ");
        printaString(codigo);
        
        codigo = criaMacros(codigo);        
        
        codigo = arrumaString(codigo);
                
        printaMacros();
        
        codigo = expandeMacros(codigo);
        System.out.println("Pós Expansão de macros: ");
        printaString(codigo);
         
        System.out.println("Criar Tabela: ");
        criarTabela(codigo);
        System.out.println("Tabela criada");
        
        File arq = new File("Saida.txt");//Arrumar
        if(!arq.exists()){
            arq.createNewFile();
        }
        
        
        OutputStream out = new FileOutputStream("saida.txt");
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
        BufferedWriter gravarArq = new BufferedWriter(outputStreamWriter); 
        
        gravarArq.flush();
       
        String end1="",end2="", modo1="",modo2="";
        
        int inst=0,dado=0;
        
        int id=0,id2=0;
        
        
        
        System.out.println("CODIGO \nDEFINICOES:");
        //gravarArq.append("Codigo segment:\n");
        for(int i=0; i<elementosTabela;i++){
            System.out.println(tabelaSimbolos[i][2]+" "+tabelaSimbolos[i][0]+ " "+ tabelaSimbolos[i][1]);
            gravarArq.append(i+ " " +tabelaSimbolos[i][0]+" "+tabelaSimbolos[i][2]);
            gravarArq.newLine();
        }
        gravarArq.newLine();gravarArq.newLine();
        //System.out.println("codigo ends\n");
        
               
        
        System.out.println("Saida Montador:");
        
        printaString(codigo);
        
        for(int i=0;i<codigo.length&&!codigo[i].equals("\0");i++){
            
            switch(codigo[i]){
                case "add":        
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }i++;
                    
                    
                    if(eNumero(codigo[i])){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end2 = codigo[i];
                        modo2="";
                    }
                    
                    gravarArq.append((i-2) +" "+"03 "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-2) +" "+"03 "+end1+modo1+ " "+end2+modo2);
                    
                    
                break;
                case "sub":
                    
                    
                    i++;
                    if(eNumero(codigo[i])){
                        colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é número
                        end1 = ""+ultimoEndereco();
                        modo1="a";
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }i++;
                    
                    
                    if(eNumero(codigo[i])){
                        colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é número
                        end2 = ""+ultimoEndereco();
                        modo2="a";
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end2 = codigo[i];
                        modo2="";
                    }
                    
                    gravarArq.append((i-2) +" "+"2B "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-2) +" "+"2B "+end1+modo1+ " "+end2+modo2);
                    
                break;        
                case "mov":
                    
                    i++;
                    if(eNumero(codigo[i])){
                        colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é número
                        end1 = ""+ultimoEndereco();
                        modo1="a";
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }i++;
                    
                    
                    if(eNumero(codigo[i])){
                        colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é número
                        end2 = ""+ultimoEndereco();
                        modo2="a";
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end2 = codigo[i];
                        modo2="";
                    }
                    
                    gravarArq.append((i-2) +" "+"8B "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-2) +" "+"8B "+end2+modo2);
                    
                break;
                case "mul":
                    
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }
                    end2 = "";
                    modo2 = "";
                    
                    gravarArq.append((i-1) +" "+"F7 "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-1) +" "+"F7 "+end1+modo1+ " "+end2+modo2);
                        
           
                break;
                case "div":
                    
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }
                    end2 = "";
                    modo2 = "";
                    
                    gravarArq.append((i-1) +" "+"F7 "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-1) +" "+"F7 "+end1+modo1+ " "+end2+modo2);
                break;
                case "cmp":
                    
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }i++;
                    
                    
                    if(eNumero(codigo[i])){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end2 = codigo[i];
                        modo2="";
                    }
                    
                    gravarArq.append((i-2) +" "+"3D "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-2) +" "+"3D "+end1+modo1+ " "+end2+modo2);
                    
                break;
                case "and":
                    
                    
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }i++;
                    
                    
                    if(eNumero(codigo[i])){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end2 = codigo[i];
                        modo2="";
                    }
                    
                    gravarArq.append((i-2) +" "+"23 "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-2) +" "+"23 "+end1+modo1+ " "+end2+modo2);
                    
                break;
                case "or":
                    
                    
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }i++;
                    
                    
                    if(eNumero(codigo[i])){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end2 = codigo[i];
                        modo2="";
                    }
                    
                    gravarArq.append((i-2) +" "+"0B "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-2) +" "+"0B "+end1+modo1+ " "+end2+modo2);
                    
                break;
                case "xor":
                    
                    
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }i++;
                    
                    
                    if(eNumero(codigo[i])){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end2 = codigo[i];
                        modo2="";
                    }
                    
                    gravarArq.append((i-2) +" "+"33 "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-2) +" "+"33 "+end1+modo1+ " "+end2+modo2);
                    
                break;
                case "not":
                    
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }
                    end2 = "";
                    modo2 = "";
                    
                    gravarArq.append((i-1) +" "+"F7 "+end1+modo1+ " "+end2+modo2);
                    System.out.println((i-1) +" "+"F7 "+end1+modo1+ " "+end2+modo2);
                break;
                case "jmp":
                    
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }
                    end2 = "";
                    modo2 = "";
                    
                    gravarArq.append((i-1) +" "+"EB "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-1) +" "+"EB "+end1+modo1+ " "+end2+modo2);
                break;
                case "je":
                    
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }i++;
                    
                    
                    if(eNumero(codigo[i])){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end2 = codigo[i];
                        modo2="";
                    }
                    
                    gravarArq.append((i-2) +" "+"74 "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-2) +" "+"74 "+end1+modo1+ " "+end2+modo2);
                break;
                case "jnz":
                    
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }
                    end2 = "";
                    modo2 = "";
                    
                    gravarArq.append((i-1) +" "+"75 "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-1) +" "+"75 "+end1+modo1+ " "+end2+modo2);
                break;
                case "jz":
                    
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }
                    end2 = "";
                    modo2 = "";
                    
                    gravarArq.append((i-1) +" "+"74 "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-1) +" "+"74 "+end1+modo1+ " "+end2+modo2);
                break;
                case "jp":
                    
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }
                    end2 = "";
                    modo2 = "";
                    
                    gravarArq.append((i-1) +" "+"7A "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-1) +" "+"7A "+end1+modo1+ " "+end2+modo2);
                break;
                case "call":
                    
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }i++;
                    
                    
                    if(eNumero(codigo[i])){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end2 = codigo[i];
                        modo2="";
                    }
                    
                    gravarArq.append((i-2) +" "+"E8 "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-2) +" "+"E8 "+end1+modo1+ " "+end2+modo2);
                    
                break;
                case "ret":
                    
                    i++;
                    if(eNumero(codigo[i])){
                        System.out.println("é numero");
                        if(procurarNaTabela(codigo[i])){
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                            //colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            //end1 = ""+ultimoEndereco();
                            //System.out.println("End1: "+end1);
                            //modo1="r";
                        }else{
                            System.out.println("Valor não está na tabela, porém deveria");
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end1 = ""+ultimoEndereco();
                            modo1="r";
                        }else{
                            end1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo1 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end1 = codigo[i];
                        modo1="";
                    }i++;
                    
                    
                    if(eNumero(codigo[i])){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                        
                    }else if(!codigo[i].equals("ax")&&!codigo[i].equals("dx")){
                        if(!procurarNaTabela(codigo[i])){
                            colocarNaTabelaDeSimbolos(codigo[i],"a");//Se é var
                            end2 = ""+ultimoEndereco();
                            modo2="r";
                        }else{
                            end2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][2];
                            modo2 = tabelaSimbolos[enderecoNaTabela(codigo[i])][1];
                        }
                    }else{
                        end2 = codigo[i];
                        modo2="";
                    }
                    
                    gravarArq.append((i-2) +" "+"C3 "+end1+modo1+ " "+end2+modo2);
                    gravarArq.newLine();
                    System.out.println((i-2) +" "+"C3 "+end1+modo1+ " "+end2+modo2);
                    
                break;
                
                default:
                break;
                    
            }
        }
        
        
        //gravarArq.append("Codigo ENDS");
        gravarArq.close();
        out.close();
        
    }
    
    void criarTabela(String [] cod){
        
        String end1="",end2="",modo1="",modo2="";
        
        for(int i=0;i<cod.length&&!cod[i].equals("\0");i++){
            System.out.println("=======//========");
            
            if(cod[i].equals("")||cod[i]==null){
                System.out.println("nulo");

                break;
            }else
            switch(cod[i]){
                case "add":            
                    i++;
                    System.out.println("ADD");
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    i++;
                    
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end2 = cod[i];
                        modo2="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end2=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo2=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é var
                                end2 = ""+ultimoEndereco();
                                modo2="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end2 = ""+ultimoEndereco();
                                    modo2="r";
                                }
                            }
                        }
                    }
                    
                    System.out.println(cod[(i-1)]+" " +end1+modo1+"\n"+cod[i]+ " "+end2+modo2);
                    
                    
                break;
                case "sub":
                    i++;
                    System.out.println("SUB");
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    i++;
                    
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end2 = cod[i];
                        modo2="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end2=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo2=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é var
                                end2 = ""+ultimoEndereco();
                                modo2="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end2 = ""+ultimoEndereco();
                                    modo2="r";
                                }
                            }
                        }
                    }
                    
                    
                    System.out.println(cod[(i-1)]+"->" +end1+modo1+"    "+cod[i]+ "->"+end2+modo2);
                    
                break;
                case "mul":
                    i++;
                    System.out.println("MUL");
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    
                    System.out.println(cod[(i)]+"->" +end1+modo1);
                    
                break;
                case "div":
                    System.out.println("DIV");
                    i++;
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    System.out.println(cod[(i)]+"->" +end1+modo1);
                break;
                case "cmp":
                    i++;
                    System.out.println("CMP");
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    i++;
                    
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end2 = cod[i];
                        modo2="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end2=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo2=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é var
                                end2 = ""+ultimoEndereco();
                                modo2="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end2 = ""+ultimoEndereco();
                                    modo2="r";
                                }
                            }
                        }
                    }
                    
                    
                    System.out.println(cod[(i-1)]+"->" +end1+modo1+"    "+cod[i]+ "->"+end2+modo2);
                    
                break;
                case "and":
                    i++;
                    System.out.println("AND");
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    i++;
                    
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end2 = cod[i];
                        modo2="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end2=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo2=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é var
                                end2 = ""+ultimoEndereco();
                                modo2="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end2 = ""+ultimoEndereco();
                                    modo2="r";
                                }
                            }
                        }
                    }
                    
                    
                    System.out.println(cod[(i-1)]+"->" +end1+modo1+"    "+cod[i]+ "->"+end2+modo2);
                    
                break;
                case "or":
                    i++;
                    System.out.println(cod[i-1].toUpperCase());
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    i++;
                    
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end2 = cod[i];
                        modo2="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end2=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo2=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é var
                                end2 = ""+ultimoEndereco();
                                modo2="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end2 = ""+ultimoEndereco();
                                    modo2="r";
                                }
                            }
                        }
                    }
                    
                    
                    System.out.println(cod[(i-1)]+"->" +end1+modo1+"    "+cod[i]+ "->"+end2+modo2);
                    
                break;
                case "xor":
                    i++;
                    System.out.println(cod[i-1].toUpperCase());
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    i++;
                    
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end2 = cod[i];
                        modo2="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end2=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo2=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é var
                                end2 = ""+ultimoEndereco();
                                modo2="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end2 = ""+ultimoEndereco();
                                    modo2="r";
                                }
                            }
                        }
                    }
                    
                    
                    System.out.println(cod[(i-1)]+"->" +end1+modo1+"    "+cod[i]+ "->"+end2+modo2);
                    
                break;
                case "not":
                    i++;
                    System.out.println(cod[i-1].toUpperCase());
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    
                    System.out.println(cod[(i)]+"->" +end1+modo1);
                    
                break;
                case "jmp":
                    i++;
                    System.out.println(cod[i-1].toUpperCase());
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    i++;
                    System.out.println(cod[(i)]+"->" +end1+modo1);
                    
                break;
                case "je":
                    i++;
                    System.out.println(cod[i-1].toUpperCase());
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    i++;
                    System.out.println(cod[(i)]+"->" +end1+modo1);
                break;
                case "jnz":
                    i++;
                    System.out.println(cod[i-1].toUpperCase());
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    i++;
                    System.out.println(cod[(i)]+"->" +end1+modo1);
                break;
                case "jz":
                    i++;
                    System.out.println(cod[i-1].toUpperCase());
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    i++;
                    System.out.println(cod[(i)]+"->" +end1+modo1);
                break;
                case "jp":
                    i++;
                    System.out.println(cod[i-1].toUpperCase());
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    i++;
                    System.out.println(cod[(i)]+"->" +end1+modo1);
                break;
                case "call":
                    i++;
                    System.out.println(cod[i-1].toUpperCase());
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    i++;
                    
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end2 = cod[i];
                        modo2="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end2=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo2=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é var
                                end2 = ""+ultimoEndereco();
                                modo2="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end2 = ""+ultimoEndereco();
                                    modo2="r";
                                }
                            }
                        }
                    }
                    
                    
                    System.out.println(cod[(i-1)]+"->" +end1+modo1+"    "+cod[i]+ "->"+end2+modo2);
                    
                break;
                case "ret":
                    i++;
                    System.out.println(cod[i-1].toUpperCase());
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end1 = cod[i];
                        modo1="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end1=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo1=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é num
                                end1 = ""+ultimoEndereco();
                                modo1="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end1 = ""+ultimoEndereco();
                                    modo1="r";
                                }
                            }
                        }
                    }
                    i++;
                    
                    
                    if(cod[i].equals("ax")||cod[i].equals("dx")){
                        System.out.println("É Registrador");
                        end2 = cod[i];
                        modo2="";
                          
                    }else{
                        System.out.println("Não é Registrador");
                        if(procurarNaTabela(cod[i])){
                            System.out.println("Está na tabela!");
                            end2=tabelaSimbolos[enderecoNaTabela(cod[i])][2];
                            modo2=tabelaSimbolos[enderecoNaTabela(cod[i])][1];
                        }else{
                            System.out.println("Não está na tabela! Sendo Adicionado!");
                            if(eNumero(cod[i])){
                                System.out.println("É número");
                                colocarNaTabelaDeSimbolos(cod[i],"a");//Se é var
                                end2 = ""+ultimoEndereco();
                                modo2="a";
                            }else{
                                if(!cod[i].equals("ax")||cod[i].equals("dx")){
                                    System.out.println("É var");
                                    colocarNaTabelaDeSimbolos(cod[i],"r");//Se é var
                                    end2 = ""+ultimoEndereco();
                                    modo2="r";
                                }
                            }
                        }
                    }
                    System.out.println(cod[(i-1)]+"->" +end1+modo1+"    "+cod[i]+ "->"+end2+modo2);

                    
                break;
                default://Por enquanto
                break;
                    
            }
        }
    }
    
    public String[] criaMacros(String[] cod){
        System.out.println("Criando macro!");
        
        int i=0;
        
        for(;i<cod.length&&!cod[i].equals("\0");i++){
            switch(cod[i]){
                case "macro":
                    cod[i]="";
                    i++;
                    System.out.println("Macro!");
                    if(!eMacro(cod[i])){
                        String nome = cod[i];
                        System.out.println("Nome: " + nome);
                        cod[i]="";
                        i++;
                        String [] macro = new String[70];
                        boolean finalMacro = false;
                        int n=0;
                        for(;finalMacro==false;n++,i++){
                            if(!cod[i].equals("endm")){
                                macro[n] = cod[i];
                                cod[i]="";
                                System.out.println("M: "+macro[n]);
                            }else{
                                cod[i]="";
                                macro[n] = "\0";
                                finalMacro=true;
                            }
                        }
                        
                        adcMacro(nome, macro);
                    }else{
                        System.out.println("Já existe macro com este nome!");
                    }
                break;
                default:
                    System.out.println("Instrução!");
                break;
            }
        }
        cod[i] = "\0";
        return cod;
    }
    
    public String[] expandeMacros(String[] cod){
        
        System.out.println("Expandindo macro!");

        String []result = new String [cod.length*3];
        
        System.out.println("Auxiliar criado!");
        
        int idMacro =0, i=0, k=0;
        
        for(;!cod[i].equals("\0");i++,k++){
            idMacro =-1;
            System.out.println("INSTRUÇÃO");
            for(int j=0;j<nMacros;j++){//se o cod é uma macro definida
                if(cod[i].equals(macro[j].nome)){
                    idMacro=j;
                }
            }
            
            if(idMacro==-1){
                if(!cod[i].equals(""))
                    result[k] = cod[i];
                else
                    k--;
            }else{
                for(int t=0;t<(macro[idMacro].cod.length-1);t++,k++){
                    if(!macro[idMacro].cod[t].equals("")&&macro[idMacro].cod[t]!=null)
                        result[k]=macro[idMacro].cod[t];
                   
                    System.out.println(result[k]);
                }
                k--;
            }
                    
        }
        result[k]="\0";
        
        
        return result;
    }
    
    public void printaString(String[] cod){
        for(int i=0;i<cod.length;i++){
            System.out.println(cod[i]);
        }
    }
    
    public void printaMacros(){
        System.out.println("Lista de macros:");
        for(int i=0;i<nMacros;i++){
            System.out.println("Macro " + i +"\nNome: " + macro[i].nome);
            printaString(macro[i].cod);
        }
        System.out.println("Fim da lista de macros:");
    }
    
    public String[] arrumaString(String[] cod){
        System.out.println("Arrumando String");
        
        System.out.println("String Recebida:");
        printaString(cod);
        
        int i=0;
        boolean fim = false;
        for(;fim==false&&i<cod.length&&!cod[i].equals("\0");i++){
            
            if(cod[i].equals("")){
                int t=i+1;
                
               while(t<cod.length&&cod[t].equals("")) { 
                   ++t;
               }
               if(t>=cod.length)
                   fim = true;
               else{
                    System.out.println(cod[t]);
                    cod[i]=cod[t];
                    cod[t] = "";
               }
            }
        }
        printaString(cod);
        cod[i] = "\0";
        
        return cod;
    }
}
