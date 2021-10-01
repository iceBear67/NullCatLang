package io.ib67.lexer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Token {
    private int line;
    private Type type;
    private String content;

    @Override
    public String toString() {
        return line+": "+type+" "+content;
    }

    public enum Type{
        IDENTIFIER(""),

        CLASS("class"),FUNCTION("fn"),ANNOTATION("annotation"),FOR("for"),WHILE("while"),IF("if"),USING("using")
        ,THIS("this"),TRUE("true"),FALSE("false"),ELSE("else"),VAR("var"),NULL("null"),PRINTLN("println"), // KEYWORDS
        VAL("val"),

        LEFT_BRACE("("),RIGHT_BRACE(")"),
        LEFT_BRACKET("{"),RIGHT_BRACKET("}"),
        LEFT_MID_BRACE("["),RIGHT_MID_BRACE("]"),

        COMMA(","),DOT("."),MINUS("-"),PLUS("+"),STAR("*"),SLASH("/"), // operators
        BREAK_LINE("\n"),ASSIGNMENT("="),EQUALS("=="),SEMICOLON(";"),AT("@"),COLON(":"),

        LITERAL_STRING(""),LITERAL_NUMBER(""); // literals
        @Getter
        private String def;
        Type(String def){
            this.def=def;
        }
    }
}
