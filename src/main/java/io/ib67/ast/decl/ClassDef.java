package io.ib67.ast.decl;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class ClassDef implements Definition{
    private String className;
    private List<String> interfaces;
    private String superclass;
}
