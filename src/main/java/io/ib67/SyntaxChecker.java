package io.ib67;

import io.ib67.lexer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class SyntaxChecker {
    private static final Pattern IDENTIFIER = Pattern.compile("[a-zA-Z_]");
    public static List<String> check(List<Token> tokens){
        var errors = new ArrayList<String>();
        for (int i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i);
            switch(token.getType()){
                case IDENTIFIER:
                    if(!IDENTIFIER.matcher(token.getContent()).find()){
                        errors.add("Invalid Identifier Name: "+token.getContent());
                    }
                    break;
            }
        }
        return errors;
    }
}
