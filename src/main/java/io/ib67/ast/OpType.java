package io.ib67.ast;

import lombok.Getter;

public enum OpType {
    COMMA(","),DOT("."),MINUS("-"),PLUS("+"),STAR("*"),SLASH("/"), // operators
    BREAK_LINE("\n"),ASSIGNMENT("="),EQUALS("=="),SEMICOLON(";");
    @Getter
    private String def;
    OpType(String def){
        this.def=def;
    }

}
