package io.ib67.ast.exp;

import io.ib67.ast.ASTNode;
import io.ib67.ast.OpType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class BinOpExp extends ASTNode {
    private ASTNode left;
    private ASTNode right;
    private OpType opType;
}
