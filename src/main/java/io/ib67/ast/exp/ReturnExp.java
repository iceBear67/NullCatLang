package io.ib67.ast.exp;

import io.ib67.ast.ASTNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReturnExp extends ASTNode {
    private ConstExp data;
}
