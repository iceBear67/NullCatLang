package io.ib67;

import io.ib67.lexer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class SyntaxChecker {
    private static final Pattern IDENTIFIER = Pattern.compile("[a-zA-Z0-9]+");
    public static List<String> check(List<Token> tokens){
        var errors = new ArrayList<String>();
        for (int i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i);
            var lastToken = (i == tokens.size()-1);
            switch(token.getType()){ // Basic syntax check.
                case IDENTIFIER:
                    if(!IDENTIFIER.matcher(token.getContent()).matches()){
                        errors.add("Invalid Identifier Name: "+token.getContent()+" line: "+token.getLine());
                    }
                    break;
                case IF:
                case DOT:
                case FOR:
                case ELSE:
                case VAR:
                case PLUS:
                case STAR:
                case COMMA:
                case WHILE:
                case EQUALS:
                case FUNCTION:
                case SEMICOLON:
                case ANNOTATION:
                case ASSIGNMENT:
                case LEFT_BRACE:
                case LEFT_BRACKET:
                case LEFT_MID_BRACE:
                case CLASS: // class declaration.
                    if(lastToken){
                        errors.add("Incomplete "+token.getType()+" Declaration! line: "+token.getLine());
                    }
            }
        }
        return errors;
    }
}