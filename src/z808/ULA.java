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
public class ULA {
    public int AX,DX,PC,SP,CMP;
    
    
    //Aritmética:
    public void add(int opd1, int opd2){
        AX = opd1 + opd2;
    }
    public void sub(int opd1, int opd2){
        AX = opd1-opd2;
    }
    public void mul(int opd1, int opd2){
        AX = opd1*opd2;
    }
    public void mul(int opd1){
        AX *= opd1;
    }
    public void div(int opd1, int opd2){
        AX = opd1/opd2;
    }
    public void div(int opd1){
        AX /= opd1;
    }
    public void cmp(int opd1, int opd2){
        if(opd1 == opd2) CMP = 1;
        else CMP = 0;
    }
    
    
    //Lógicas:
    public void or(int opd1, int opd2){
        AX = opd1 ^ opd2;
    }
    public void not(int opd1){
        AX = ~opd1;
    }
    public void and(int opd1, int opd2){
        AX = opd1 & opd2;
    }
    public void xor(int opd1, int opd2){
        AX = (opd1 & ~opd2) ^ (~opd1 & opd2);
    }
    
    //Desvios
    public void jmp(int end){
        PC += end;
    }
    public void je(int opd1, int end){
        if(opd1 == AX) PC += end;
    }
    public void jz(int end){
        if(0 == AX) PC += end;
    }
    public void jnz(int end){
        if(0 != AX) PC += end;
    }
    
    //move
    
    public void mov(int opd1){
        AX = opd1;
    }
    public int mov_reg(int reg){
        reg = AX;
        return reg;
    }
    
    
    
    
    
}
