package crafttweaker.tests.generic_full.expression;

import crafttweaker.tests.generic_full.*;
import crafttweaker.tests.generic_full.type.*;
import stanhebben.zenscript.compiler.*;
import stanhebben.zenscript.expression.*;
import stanhebben.zenscript.type.*;
import stanhebben.zenscript.util.*;

public class ExpressionSequenceNew extends Expression {
    
    private final ExpressionArray expressionArray;
    private final ZenType genericType;
    
    public ExpressionSequenceNew(ZenPosition position, ZenType genericType, Expression... content) {
        super(position);
        this.expressionArray = new ExpressionArray(position, new ZenTypeArrayBasic(genericType), content);
        this.genericType = genericType;
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        if(!result) {
            return;
        }
    
        final MethodOutput output = environment.getOutput();
        output.newObject(MySequence.class);
        output.dup();
        expressionArray.compile(true, environment);
        output.invokeSpecial(ZenTypeUtil.internal(MySequence.class), "<init>", "([Ljava/lang/Object;)V");
    }
    
    @Override
    public ZenType getType() {
        return new ZenTypeGenericSequence(genericType);
    }
}
