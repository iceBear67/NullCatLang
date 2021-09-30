package io.ib67;

import io.ib67.lexer.Lexer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class NullCatCompiler {

    private NullCatCompiler(){

    }
    public static void main(String... args) throws IOException {
        String str = new String(new FileInputStream(new File("test.sb")).readAllBytes());
        new Lexer(str).startLexing().forEach(System.out::println);
    }
}
