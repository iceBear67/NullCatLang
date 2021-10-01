package io.ib67.ast.consts;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StringConst extends Const<String>{
    private String value;
    @Override
    public String value() {
        return value;
    }
}
