package tinycc.implementation;

import java.util.ArrayList;

import tinycc.implementation.statement.Statement;

public class Buffer {
    private ArrayList<Statement> buffer;

    public Buffer(){
        this.buffer = new ArrayList<>(); 
    }

    public ArrayList<Statement> getBuffer(){
        return this.buffer;
    }
}
