package crafttweaker.tests.generic_full.expression;

import crafttweaker.tests.generic_full.*;
import crafttweaker.tests.generic_full.type.*;
import stanhebben.zenscript.compiler.*;
import stanhebben.zenscript.definitions.*;
import stanhebben.zenscript.expression.*;
import stanhebben.zenscript.expression.partial.*;
import stanhebben.zenscript.type.*;
import stanhebben.zenscript.util.*;

import java.util.*;

public class ExpressionSequenceForEach extends Expression {
    
    private final IPartialExpression callee;
    private Expression fun;
    
    public ExpressionSequenceForEach(ZenPosition position, IPartialExpression callee, Expression fun) {
        super(position);
        this.callee = callee;
        this.fun = fun;
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
        environment.getOutput().invokeVirtual(MySequence.class, "forEach", void.class, Functions.Consumer.class);
    }
    
    private void updateFun(ZenTypeGenericSequence sequence) {
        if(fun instanceof ExpressionJavaLambdaSimpleGeneric) {
            List<ParsedFunctionArgument> arguments = new ArrayList<>(((ExpressionJavaLambdaSimpleGeneric) fun).getArguments());
            ParsedFunctionArgument firstArg = arguments.get(0);
            if(firstArg.getType() == ZenType.ANY) {
                arguments.set(0, new GenericSequenceParsedFunctionArgument(firstArg, sequence));
            }
        
            fun = new ExpressionJavaLambdaSimpleGeneric(fun.getPosition(), ((ExpressionJavaLambdaSimpleGeneric) fun).getInterfaceClass(), arguments, ((ExpressionJavaLambdaSimpleGeneric) fun).getStatements(), fun.getType());
        }
    }
    
    @Override
    public ZenType getType() {
        return ZenType.VOID;
    }
}
