package io.ib67.ast.consts;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class IntConst extends Const<Integer>{
    private int value;
    @Override
    public Integer value() {
        return value;
    }
}
