package crafttweaker.tests.generic_full.expression;

import crafttweaker.tests.generic_full.type.*;
import stanhebben.zenscript.compiler.*;
import stanhebben.zenscript.expression.*;
import stanhebben.zenscript.expression.partial.*;
import stanhebben.zenscript.symbols.*;
import stanhebben.zenscript.type.*;
import stanhebben.zenscript.util.*;

import java.util.*;

public class PartialGenericSequence extends Expression {
    
    private final String collect;
    private final IEnvironmentGlobal backupEnvironment;
    
    public PartialGenericSequence(ZenPosition position, String collect, IEnvironmentGlobal environment) {
        super(position);
        this.collect = collect;
        backupEnvironment = environment;
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
    }
    
    @Override
    public Expression eval(IEnvironmentGlobal environment) {
        return this;
    }
    
    @Override
    public Expression assign(ZenPosition position, IEnvironmentGlobal environment, Expression other) {
        environment.error(position, "not a valid lvalue");
        return new ExpressionInvalid(position, getType());
    }
    
    @Override
    public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
        environment.error(position, "Sequence Brackets have no members");
        return new ExpressionInvalid(position);
    }
    
    @Override
    public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression... values) {
        final Expression[] expressionList = new Expression[values.length];
        final ZenType genericType = getGenericType(environment);
        for(int i = 0; i < values.length; i++) {
            expressionList[i] = values[i].cast(position, environment, genericType);
        }
        return new ExpressionSequenceNew(position, genericType, expressionList);
    }
    
    private ZenType getGenericType(IEnvironmentGlobal environment) {
        final String[] split = collect.split("[.]");
        IPartialExpression value = environment.getValue(split[0], getPosition());
        for(int i = 1; i < split.length; i++) {
            value = value.getMember(getPosition(), environment, split[i]);
        }
    
        return value.toType(environment);
    }
    
    @Override
    public ZenType[] predictCallTypes(int numArguments) {
        final ZenType genericType = getGenericType(backupEnvironment);
        final ZenType[] zenTypes = new ZenType[numArguments];
        Arrays.fill(zenTypes, genericType);
        return zenTypes;
    }
    
    @Override
    public IZenSymbol toSymbol() {
        return null;
    }
    
    @Override
    public ZenType getType() {
        return toType(backupEnvironment);
    }
    
    @Override
    public ZenType toType(IEnvironmentGlobal environment) {
        return new ZenTypeGenericSequence(getGenericType(environment));
    }
}
