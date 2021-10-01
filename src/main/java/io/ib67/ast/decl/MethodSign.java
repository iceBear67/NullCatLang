package io.ib67.ast.decl;

import io.ib67.util.Pair;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class MethodSign {
    public String name;
    public ArrayList<String> types;

    @Override
    public String toString() {
        return name+ Arrays.toString(types.toArray(new String[0]));
    }

    @Override
    public int hashCode() {
        int i =1;
        i = i * 31 + types.hashCode();
        return i;
    }
}
