package io.ib67.parser;

import io.ib67.NullCatCompiler;
import io.ib67.SyntaxChecker;
import io.ib67.lexer.Token;
import io.ib67.parser.meta.CatMetadata;
import io.ib67.util.Pair;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class Parser {
    private final Pair<String,List<Token>> tokens;
    public CompilationRequest compileAST(){
        // check basic syntax.
        var _erros = SyntaxChecker.check(tokens.value);
        if(_erros.size()!=0){
            _erros.stream().map(e->tokens.key+": "+e).forEach(System.err::println);
            throw new ParseException("Invalid syntax!");
        }
        return null;
    }


}
