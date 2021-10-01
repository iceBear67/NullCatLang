package io.ib67.parser;

import io.ib67.ast.decl.ClassDef;
import io.ib67.ast.decl.Definition;
import io.ib67.ast.decl.MethodDef;
import io.ib67.ast.decl.VariableDef;

import java.util.HashMap;
import java.util.Map;

public class CompilationRequest {
    /* 符号表 */
    private ClassDef classDefinition;
    private Map<String, MethodDef> methods = new HashMap<>();
    private Map<String, VariableDef> fields = new HashMap<>();
    
}
