package io.ib67.ast.exp;

import io.ib67.ast.ASTNode;
import io.ib67.ast.OpType;
import io.ib67.ast.decl.VariableDef;

public class FieldDeclExp extends BinOpExp{

    public FieldDeclExp(VariableDef left, ASTNode right, OpType opType) {
        super(left, right, opType);
    }

    @Override
    public VariableDef getLeft() {
        return (VariableDef) super.getLeft();
    }
}
