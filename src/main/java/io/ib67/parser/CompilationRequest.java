package io.ib67.parser;

import io.ib67.ast.ASTNode;
import io.ib67.ast.decl.*;
import io.ib67.parser.meta.CatMetadata;
import io.ib67.util.Pair;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CompilationRequest {

    private final String fileName;
    /* 符号表 */
    private final CatMetadata metadata;

    /* ASTs */
    private Map<MethodSign, Pair<Map<String,VariableDef>,List<ASTNode>>> methodCodes;
}
