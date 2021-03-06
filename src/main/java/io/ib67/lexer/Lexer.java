package io.ib67.lexer;

import io.ib67.util.Pair;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class Lexer {
    private final String fileName;
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

    public Lexer(String content,String fileName) {
        rawContent = content.replaceAll("//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/", "$1 "); // remove comments.
        this.fileName=fileName;
    }

    public List<LexedNode> fuzzyTokenize() {
        char[] charStream = rawContent.toCharArray();
        List<LexedNode> nodes = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        boolean inIdOrLiteral = false;
        boolean stringMode = false;
        int line=1;
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
                    // fix: flush buffer.
                  //  buffer.append(now);
                    if(inIdOrLiteral){
                        if(stringMode){
                            /*nodes.add(new LexedNode(buffer.toString(), LexedNode.NodeType.LITERAL_STRING));
                            buffer = new StringBuilder();*/
                            throw new LexerException(fileName+": Unexpected string end at line: "+line);
                        }else{
                            identifierParse(buffer.toString(),nodes);
                            buffer = new StringBuilder();
                        }
                    }
                    nodes.add(new LexedNode("\n", LexedNode.NodeType.LINE_SEPERATOR));
                    line++;
                    continue;
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
                    throw new LexerException(fileName+": Unknown char: " + now+" line: "+line);
                }
            } else {
                inIdOrLiteral = true; // not symbol & not identifier
            }

            if (i == charStream.length - 1) { //the end of file
                if (inIdOrLiteral) {
                    if (stringMode) {
                        // cant parse!
                        throw new LexerException(fileName+": Cant parse string until end of file: " + buffer.toString());
                    } else {
                        buffer.append(now);
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

    public Pair<String,List<Token>> tokenize() {
        var lexedNodes = fuzzyTokenize();
        var tokens = new ArrayList<Token>();
        var line = 1;
        for (int i = 0; i < lexedNodes.size(); i++) {
            LexedNode lexedNode = lexedNodes.get(i);
            if(lexedNode.getContent().equals(":")){
                System.out.println(2);
            }
            switch (lexedNode.getType()) {
                case LINE_SEPERATOR:
                    tokens.add(new Token(line, Token.Type.BREAK_LINE,""));
                    line++;
                    break;
                case SYMBOL:
                case KEYWORD:
                    var type = Arrays.stream(Token.Type.values()).filter(e -> e.getDef().equals(lexedNode.getContent())).findFirst().orElseThrow(()->{
                       return new NullPointerException(lexedNode.toString());
                    });
                    tokens.add(new Token(line, type, type.getDef()));
                    break;
                case LITERAL_STRING:
                    tokens.add(new Token(line, Token.Type.LITERAL_STRING, lexedNode.getContent()));
                    break;
                case LITERAL_NUMBER:
                    tokens.add(new Token(line, Token.Type.LITERAL_NUMBER, lexedNode.getContent()));
                    break;
                case OPERATOR:
                    // =
                    boolean isEnd = (i == lexedNodes.size() - 1);
                    switch (lexedNode.getContent()) {
                        case "=":
                            if (isEnd) {
                                throw new LexerException(fileName+": Invalid syntax line "+line);
                            }
                            if (lexedNodes.get(i + 1).getType() == LexedNode.NodeType.OPERATOR && lexedNodes.get(i + 1).getContent().equals("=")) { // ==
                                tokens.add(new Token(line, Token.Type.EQUALS, "=="));
                                i = i + 1; // skip next
                                break;
                            } else {
                                tokens.add(new Token(line, Token.Type.ASSIGNMENT, "="));
                            }
                        case ".":
                            tokens.add(new Token(line, Token.Type.DOT, "."));
                            break;
                        case ",":
                            tokens.add(new Token(line, Token.Type.COMMA, ","));
                            break;
                        case "-":
                            tokens.add(new Token(line, Token.Type.MINUS, "-"));
                            break;
                        case "+":
                            tokens.add(new Token(line, Token.Type.PLUS, "+"));
                            break;
                        case "*":
                            tokens.add(new Token(line, Token.Type.STAR,"*"));
                            break;
                        case "/":
                            tokens.add(new Token(line, Token.Type.SLASH,"/"));
                            break;
                        case ";":
                            tokens.add(new Token(line, Token.Type.SEMICOLON,";"));
                            break;
                        case ":":
                            tokens.add(new Token(line,Token.Type.COLON,":"));
                            break;
                    }
                    break;
                case IDENTIFIER:
                    tokens.add(new Token(line, Token.Type.IDENTIFIER, lexedNode.getContent()));
                    break;

            }
        }
        return Pair.of(fileName,tokens);
    }

    private static final boolean isInteger(String str) {
        try {
            new BigInteger(str);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private static final void identifierParse(String identifier, List<LexedNode> nodes) {
        if (identifier.length() == 0) {
            return;
        }
        if (KWs.contains(identifier)) {
            nodes.add(new LexedNode(identifier, LexedNode.NodeType.KEYWORD));
            return;
        } else if (isInteger(identifier)) {
            nodes.add(new LexedNode(identifier, LexedNode.NodeType.LITERAL_NUMBER));
            return;
        }
        nodes.add(new LexedNode(identifier, LexedNode.NodeType.IDENTIFIER));
    }

}
