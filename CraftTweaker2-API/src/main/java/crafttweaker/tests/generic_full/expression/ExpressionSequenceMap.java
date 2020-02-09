package crafttweaker.tests.generic_full.expression;

import crafttweaker.tests.generic_full.*;
import crafttweaker.tests.generic_full.type.*;
import stanhebben.zenscript.compiler.*;
import stanhebben.zenscript.expression.*;
import stanhebben.zenscript.expression.partial.*;
import stanhebben.zenscript.type.*;
import stanhebben.zenscript.util.*;

public class ExpressionSequenceMap extends Expression {
    
    private final IPartialExpression callee;
    private final ZenTypeGenericSequence genericSequence;
    private Expression fun;
    
    public ExpressionSequenceMap(ZenPosition position, IPartialExpression callee, Expression fun, ZenTypeGenericSequence genericSequence) {
        super(position);
        this.callee = callee;
        this.genericSequence = genericSequence;
        this.fun = fun instanceof ExpressionJavaLambdaSimpleGeneric ? new ExpressionMappingFunction((ExpressionJavaLambdaSimpleGeneric) fun, genericSequence) : fun;
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        Expression eval = callee.eval(environment);
        eval.compile(true, environment);
    
        ZenType type = eval.getType();
        if(type instanceof ZenTypeGenericSequence) {
            updateFun((ZenTypeGenericSequence) type);
        }
        
        fun.compile(true, environment);
        environment.getOutput().invokeVirtual(MySequence.class, "map", MySequence.class, Functions.Mapper.class);
        
        if(!result) {
            environment.getOutput().pop();
        }
    }
    
    private void updateFun(ZenTypeGenericSequence sequence) {
        if(sequence.getGenericType() == ZenType.ANY) {
            return;
        }
        
        if(fun instanceof ExpressionMappingFunction) {
            fun = new ExpressionMappingFunction(((ExpressionMappingFunction) fun), sequence);
        }
    }
    
    @Override
    public ZenType getType() {
        if(fun instanceof ExpressionMappingFunction) {
            return new ZenTypeGenericSequence(() -> ((ExpressionMappingFunction) fun).getGenericReturnType());
        }
        
        return new ZenTypeGenericSequence(ZenType.ANY);
    }
}
