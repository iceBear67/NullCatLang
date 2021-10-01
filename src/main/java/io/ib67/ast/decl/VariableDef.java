package io.ib67.ast.decl;

import io.ib67.ast.ASTNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VariableDef extends ASTNode implements Definition {
    /**
     * Null or type. (ast scan 2)
     */
    private String type;
    private String name;
}
