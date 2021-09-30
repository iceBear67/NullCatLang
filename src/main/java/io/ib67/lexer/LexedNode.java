package io.ib67.lexer;

public class LexedNode {
    private NodeType type;
    private String content;

    public NodeType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public LexedNode(char content, NodeType type){
        this(content+"",type);
    }
    public LexedNode(String content,NodeType type){
        this.content=content;
        this.type=type;
    }
    public enum NodeType {
        IDENTIFIER,SYMBOL,KEYWORD,OPERATOR,
        LINE_SEPERATOR,
        LITERAL_STRING,LITERAL_NUMBER
    }

    @Override
    public String toString() {
        return type.name()+" "+content;
    }
}
