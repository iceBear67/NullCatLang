package io.ib67.parser.meta;

import io.ib67.NullCatCompiler;
import io.ib67.ast.decl.ClassDef;
import io.ib67.ast.decl.MethodDef;
import io.ib67.ast.decl.MethodSign;
import io.ib67.ast.decl.VariableDef;
import io.ib67.parser.ParseException;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CatMetadata {
    private ClassDef classDefinition = new ClassDef();
    private Map<String,CatMetadata> cachedUsings = new HashMap<>();
    private List< MethodSign> methods = new ArrayList<>();
    private Map<String, VariableDef> fields = new HashMap<>();
    public CatMetadata resolveType(String typeExpr,boolean global){
        String expr;
        if(typeExpr.contains("<") && typeExpr.contains(">")){
            var genericExpr = typeExpr.substring(typeExpr.indexOf("<")+1,typeExpr.indexOf(">"));
            expr = typeExpr.substring(0,typeExpr.indexOf("<"));
            resolveType(genericExpr,true);
        }else{
            expr = typeExpr;
        }
        if(global){
            var b= cachedUsings.values().stream().filter(e->e.getClassDefinition().getClassName().endsWith("."+expr)).findFirst().orElse(Global.forClass(expr));
            cachedUsings.put(b.getClassDefinition().getClassName(),b);
        }
        return cachedUsings.values().stream().filter(e->e.getClassDefinition().getClassName().endsWith("."+expr)).findFirst().orElseThrow(()->new ParseException("Unknown type: "+typeExpr));
    }
    public static class Global {
        private static final Map<String,CatMetadata> GLOBAL_METADATAS = new HashMap<>();
        public static final CatMetadata forClass(String str){
            /*
             * Scan compiler classPaths
             */
            var meta = NullCatCompiler.solveMeta(str);
            if(meta!=null) {
                GLOBAL_METADATAS.put(str, meta);
            }else{
                meta = NullCatCompiler.solveMeta("java.lang." + str);
            }
            return meta;
        }
    }
}
