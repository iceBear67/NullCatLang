package io.ib67;

import io.ib67.lexer.Lexer;
import io.ib67.meta.ClassMetaPathImpl;
import io.ib67.meta.MetaPath;
import io.ib67.parser.MetadataGenerator;
import io.ib67.parser.Parser;
import io.ib67.parser.meta.CatMetadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class NullCatCompiler {
    private static final List<MetaPath> metaPaths = new ArrayList<>();
    static {
        metaPaths.add(new ClassMetaPathImpl(NullCatCompiler.class.getClassLoader()));
    }
    private NullCatCompiler(){

    }
    public static CatMetadata solveMeta(String clazzName){
        return metaPaths.stream().map(e->e.findClass(clazzName)).filter(Objects::nonNull).findFirst().orElse(null);
    }
    public static void main(String... args) throws IOException {
        String str = new String(new FileInputStream(new File("test.sb")).readAllBytes());
        var a= new Lexer(str,"test.sb").tokenize();
        var cm = new MetadataGenerator(a.key,a.value).gen();
        System.out.println(1);
    }
}
