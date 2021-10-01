package io.ib67.parser;

import io.ib67.ast.decl.MethodSign;
import io.ib67.lexer.Token;
import io.ib67.parser.meta.CatMetadata;
import io.ib67.util.Pair;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MetadataGenerator {
    private final String fileName;
    private final List<Token> tokens;
    private CatMetadata cm = new CatMetadata();
    private int i=0;
    public CatMetadata gen(){
        for (i = 0; i < tokens.size(); i++) {
            Token now = tokens.get(i);
            boolean end = (i==tokens.size()-1);
            Token next = end?null:tokens.get(i+1);
            switch(now.getType()){
                case USING:
                    if(!end){
                        i=i+1;
                        var clazz = readAsStringUntilLB();
                        cm.getCachedUsings().put(clazz, Optional.ofNullable(CatMetadata.Global.forClass(clazz)).orElseThrow(()->new ParseException("Can't find clazz "+clazz)));
                    }else{
                        throwEOF();
                    }
                    continue;
                case FUNCTION:
                    if (end) {
                        throwEOF();
                    }
                    // fn main(){}
                    if(next.getType() != Token.Type.IDENTIFIER){
                        throw new ParseException(fileName+": Unexcepted "+next.getType()+" at line "+now.getLine());
                    }
                    String methodName = next.getContent();
                    i=i+2;
                    MethodSign sign = readMethodSign(methodName);
                    if(cm.getMethods().stream().anyMatch(e->e.hashCode()==sign.hashCode())){
                        throw new ParseException(fileName+": Duplicated method: "+sign+" at line "+now.getLine());
                    }
                    cm.getMethods().add(sign);
                    continue;
            }
        }
        return cm;
    }
    private final MethodSign readMethodSign(String name){
        StringBuilder buffer = new StringBuilder();
        MethodSign sign = new MethodSign(name,new ArrayList<>());
        int b =0;
        for(int a = i;tokens.get(a).getType()!= Token.Type.RIGHT_BRACE;a++){
            b=a;
            Token now = tokens.get(a);
            boolean end = (i==tokens.size()-1);
            Token last = end?null:tokens.get(a-1);
            switch (now.getType()){
                case IDENTIFIER:
                    if(end){
                        throwEOF();
                    }
                    if(last.getType() == Token.Type.COLON){
                        // type here.
                        CatMetadata catm = cm.resolveType(now.getContent());
                        sign.types.add(catm.getClassDefinition().getClassName());
                    }
            }
        }
        i = b;
        return sign;
    }
    private final void throwEOF(){
        throw new ParseException(fileName+": Unexcepted end of file");
    }
    private final String readAsStringUntilLB(){
        StringBuilder sb = new StringBuilder();
        int b=0;
        for(int a = i; tokens.get(a).getType()!= Token.Type.BREAK_LINE;a++){
            sb.append(tokens.get(a).getContent());
            b=a;
        }
        i = b;
        return sb.toString();
    }
}
