package io.ib67.ast.decl;

import io.ib67.ast.ASTNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FieldDecl implements Definition{
    private VariableDef fieldDef;
    private ASTNode value;
}
