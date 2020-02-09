package crafttweaker.tests.generic_full;

import crafttweaker.tests.generic_full.type.*;
import stanhebben.zenscript.definitions.*;
import stanhebben.zenscript.type.*;

public class GenericSequenceParsedFunctionArgument extends ParsedFunctionArgument {
    
    private final ZenTypeGenericSequence sequence;
    
    public GenericSequenceParsedFunctionArgument(ParsedFunctionArgument base, ZenTypeGenericSequence sequence) {
        super(base.getName(), sequence.getGenericType());
        this.sequence = sequence;
    }
    
    @Override
    public ZenType getType() {
        return sequence.getGenericType();
    }
}
