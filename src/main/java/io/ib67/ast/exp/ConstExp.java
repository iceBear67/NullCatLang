package io.ib67.ast.exp;

import io.ib67.ast.ASTNode;
import io.ib67.ast.consts.Const;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConstExp extends ASTNode {
    private Const<?> data;
}
