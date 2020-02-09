package crafttweaker.tests.generic_full.expression;

import crafttweaker.tests.generic_full.*;
import crafttweaker.tests.generic_full.type.*;
import stanhebben.zenscript.compiler.*;
import stanhebben.zenscript.definitions.*;
import stanhebben.zenscript.expression.*;
import stanhebben.zenscript.expression.partial.*;
import stanhebben.zenscript.symbols.*;
import stanhebben.zenscript.type.*;
import stanhebben.zenscript.util.*;

import java.util.*;

public class PartialGenericSequenceMember implements IPartialExpression {
    
    private final ZenPosition position;
    private final String name;
    private final IEnvironmentGlobal environment;
    private final ZenTypeGenericSequence genericSequence;
    private final IPartialExpression callee;
    
    public PartialGenericSequenceMember(ZenPosition position, String name, IEnvironmentGlobal environment, ZenTypeGenericSequence genericSequence, IPartialExpression value) {
        this.position = position;
        this.name = name;
        this.environment = environment;
        this.genericSequence = genericSequence;
        callee = value;
    }
    
    @Override
    public Expression eval(IEnvironmentGlobal environment) {
        environment.error(position, "Cannot use Member as expression, you need to call it!");
        return new ExpressionInvalid(position);
    }
    
    @Override
    public Expression assign(ZenPosition position, IEnvironmentGlobal environment, Expression other) {
        environment.error(position, "Cannot assign to a Member, you need to call it!");
        return new ExpressionInvalid(position);
    }
    
    @Override
    public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
        environment.error(position, "Cannot get Member of a member, you need to call it!");
        return new ExpressionInvalid(position);
    }
    
    @Override
    public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression... values) {
        Expression fun = values[0];
        if(fun instanceof ExpressionJavaLambdaSimpleGeneric) {
            List<ParsedFunctionArgument> arguments = new ArrayList<>(((ExpressionJavaLambdaSimpleGeneric) fun).getArguments());
            ParsedFunctionArgument firstArg = arguments.get(0);
            if(firstArg.getType() == ZenType.ANY) {
                arguments.set(0, new GenericSequenceParsedFunctionArgument(firstArg, this.genericSequence));
            }
            
            fun = new ExpressionJavaLambdaSimpleGeneric(fun.getPosition(), ((ExpressionJavaLambdaSimpleGeneric) fun).getInterfaceClass(), arguments, ((ExpressionJavaLambdaSimpleGeneric) fun).getStatements(), fun.getType());
        }
        
        switch(name) {
            case "map":
                return new ExpressionSequenceMap(position, callee, fun, genericSequence);
            case "filter":
                return new ExpressionSequenceFilter(position, callee, fun, genericSequence);
            case "forEach":
                return new ExpressionSequenceForEach(position, callee, fun);
        }
        
        environment.error(position, "Invalid name: " + name);
        return new ExpressionInvalid(position);
    }
    
    @Override
    public ZenType[] predictCallTypes(int numArguments) {
        if(numArguments == 0) {
            return new ZenType[0];
        }
        
        final ZenType[] zenTypes = new ZenType[numArguments];
        
        final ZenType zenType;
        switch(name) {
            case "filter":
                zenType = environment.getType(Functions.Predicate.class);
                break;
            case "forEach":
                zenType = environment.getType(Functions.Consumer.class);
                break;
            case "map":
                zenType = new ZenTypeGenericMapperFunction(genericSequence);
                break;
            default:
                zenType = null;
        }
        zenTypes[0] = zenType;
        return zenTypes;
    }
    
    @Override
    public IZenSymbol toSymbol() {
        return null;
    }
    
    @Override
    public ZenType getType() {
        return null;
    }
    
    @Override
    public ZenType toType(IEnvironmentGlobal environment) {
        return null;
    }
    
}
