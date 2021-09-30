package io.ib67.lexer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Lexer {
    private static final Set<String> KWs = new HashSet<>();
    private static final Set<Character> SYMBOLS = new HashSet<>();
    private static final Set<Character> OPERATORS = new HashSet<>();
    private static final Set<Character> SYMBOL_OR_OPERATORS = new HashSet<>();

    static {
        try {
            for (String keywords : new String(Lexer.class.getClassLoader().getResourceAsStream("lexerConfig").readAllBytes()).split("\n")) {
                if (keywords.startsWith("@keyword ")) {
                    KWs.add(keywords.replaceFirst("@keyword ", ""));
                } else {
                    char dealed = keywords.replaceFirst("@operator ", "").toCharArray()[0];
                    SYMBOL_OR_OPERATORS.add(dealed);
                    if (keywords.startsWith("@operator ")) {
                        OPERATORS.add(dealed);
                    } else {
                        SYMBOLS.add(dealed);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final String rawContent;

    public Lexer(String content) {
        rawContent = content.replaceAll("//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/","$1 "); // remove comments.
    }

    public Set<LexedNode> startLexing() {
        char[] charStream = rawContent.toCharArray();
        Set<LexedNode> nodes = new LinkedHashSet<>();
        StringBuilder buffer = new StringBuilder();
        boolean inIdOrLiteral = false;
        boolean stringMode = false;
        for (int i = 0; i < charStream.length; i++) {
            char now = charStream[i];
            switch (now) {
                case '"':
                    if (i != 0 && charStream[i - 1] != '\\') {
                        // string starts or end
                        inIdOrLiteral = !inIdOrLiteral;
                        stringMode = true;
                        if (!inIdOrLiteral) {
                            stringMode = false;
                            // a new string!
                            nodes.add(new LexedNode(buffer.toString(), LexedNode.NodeType.LITERAL_STRING));
                            buffer = new StringBuilder();
                            continue;
                        }
                    }
                    continue;
                case '\\':
                    if (i != 0 && charStream[i - 1] != '\\') {
                        continue;
                    }
                    break;
                case '\n':
                    nodes.add(new LexedNode("\n", LexedNode.NodeType.LINE_SEPERATOR));
                case ' ':
                    if (stringMode) {
                        break;
                    }
                    inIdOrLiteral = !inIdOrLiteral;
                    if (inIdOrLiteral) { // start collecting
                        continue;
                    }
                    // end!
                    String str = buffer.toString();
                    identifierParse(str, nodes);
                    buffer = new StringBuilder();
                    // compose
                    continue;
            }
            /* Other Symbols */
            if (SYMBOL_OR_OPERATORS.contains(now) && !stringMode) {
                if (inIdOrLiteral) { // keyword
                    // now == a symbol,we should end this.
                    identifierParse(buffer.toString(), nodes);
                    buffer = new StringBuilder();
                    inIdOrLiteral = false;
                }
                if (SYMBOLS.contains(now)) {
                    nodes.add(new LexedNode(now, LexedNode.NodeType.SYMBOL));
                    continue;
                } else if (OPERATORS.contains(now)) {
                    nodes.add(new LexedNode(now, LexedNode.NodeType.OPERATOR));
                    continue;
                } else {
                    throw new LexerException("Unknown char: " + now);
                }
            } else {
                inIdOrLiteral = true; // not symbol & not identifier
            }
            if (i == charStream.length - 1) {
                if (inIdOrLiteral) {
                    if (stringMode) {
                        // cant parse!
                        throw new LexerException("Cant parse string until end of file: " + buffer.toString());
                    } else {
                        String str = buffer.toString();
                        identifierParse(str, nodes);
                    }
                }
            }
            /* Collect String or Identifier */
            if (inIdOrLiteral) {
                buffer.append(now);
                continue;
            }
        }
        return nodes;
    }
    private static final boolean isInteger(String str){
        try{
            new BigInteger(str);
            return true;
        }catch(Throwable t){
            return false;
        }
    }
    private static final void identifierParse(String identifier, Set<LexedNode> nodes) {
        if (identifier.length() == 0) {
            return;
        }
        if (KWs.contains(identifier)) {
            nodes.add(new LexedNode(identifier, LexedNode.NodeType.KEYWORD));
            return;
        }else if(isInteger(identifier)){
            nodes.add(new LexedNode(identifier, LexedNode.NodeType.LITERAL_NUMBER));
            return;
        }
        nodes.add(new LexedNode(identifier, LexedNode.NodeType.IDENTIFIER));
    }

}
