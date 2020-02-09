package crafttweaker.tests.generic_full;

import crafttweaker.annotations.*;
import crafttweaker.tests.generic_full.expression.*;
import crafttweaker.zenscript.*;
import stanhebben.zenscript.compiler.*;
import stanhebben.zenscript.parser.*;
import stanhebben.zenscript.symbols.*;

import java.util.*;
import java.util.stream.*;

@BracketHandler
@ZenRegister
public class BracketSequence implements IBracketHandler {
    
    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        if(tokens.size() < 3 || !tokens.get(0).getValue().equals("sequence")) {
            return null;
        }
    
        final String collect = tokens.subList(2, tokens.size())
                .stream()
                .map(Token::getValue)
                .collect(Collectors.joining());
    
        return position -> new PartialGenericSequence(position, collect, environment);
    }
    
    @Override
    public String getRegexMatchingString() {
        return "sequence:[\\w\\.]*";
    }
    
    @Override
    public Class<?> getReturnedClass() {
        return MySequence.class;
    }
}
