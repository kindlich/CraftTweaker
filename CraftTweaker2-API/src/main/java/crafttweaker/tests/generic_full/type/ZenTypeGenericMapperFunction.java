package crafttweaker.tests.generic_full.type;

import crafttweaker.tests.generic_full.*;
import org.objectweb.asm.*;
import stanhebben.zenscript.annotations.*;
import stanhebben.zenscript.compiler.*;
import stanhebben.zenscript.expression.*;
import stanhebben.zenscript.expression.partial.*;
import stanhebben.zenscript.type.*;
import stanhebben.zenscript.type.casting.*;
import stanhebben.zenscript.util.*;

public class ZenTypeGenericMapperFunction extends ZenTypeNative {
    
    private static final Class<?> cls = Functions.Mapper.class;
    
    private ZenTypeGenericSequence baseSequence;
    private ZenTypeGenericSequence resultSequence;
    
    public ZenTypeGenericMapperFunction(ZenTypeGenericSequence baseSequence) {
        super(cls);
        this.baseSequence = baseSequence;
        this.resultSequence = new ZenTypeGenericSequence(ZenType.ANY);
    }
    
    public ZenTypeGenericSequence getBaseSequence() {
        return baseSequence;
    }
    
    public void setBaseSequence(ZenTypeGenericSequence baseSequence) {
        this.baseSequence = baseSequence;
    }
    
    public ZenTypeGenericSequence getResultSequence() {
        return resultSequence;
    }
    
    public void setResultSequence(ZenTypeGenericSequence resultSequence) {
        this.resultSequence = resultSequence;
    }
    
    @Override
    public Expression unary(ZenPosition position, IEnvironmentGlobal environment, Expression value, OperatorType operator) {
        environment.error(position, "Operator 'unary' not supported!");
        return new ExpressionInvalid(position);
    }
    
    @Override
    public Expression binary(ZenPosition position, IEnvironmentGlobal environment, Expression left, Expression right, OperatorType operator) {
        environment.error(position, "Operator 'binary' not supported!");
        return new ExpressionInvalid(position);
    }
    
    @Override
    public Expression trinary(ZenPosition position, IEnvironmentGlobal environment, Expression first, Expression second, Expression third, OperatorType operator) {
        environment.error(position, "Operator 'trinary' not supported!");
        return new ExpressionInvalid(position);
    }
    
    @Override
    public Expression compare(ZenPosition position, IEnvironmentGlobal environment, Expression left, Expression right, CompareType type) {
        environment.error(position, "Operator 'compare' not supported!");
        return new ExpressionInvalid(position);
    }
    
    @Override
    public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, IPartialExpression value, String name) {
        environment.error(position, "getMember not supporter");
        return new ExpressionInvalid(position);
    }
    
    @Override
    public IPartialExpression getStaticMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
        environment.error(position, "getStaticMember not supporter");
        return new ExpressionInvalid(position);
    }
    
    @Override
    public Expression call(ZenPosition position, IEnvironmentGlobal environment, Expression receiver, Expression... arguments) {
        environment.error(position, "Operator 'call' not supported!");
        return new ExpressionInvalid(position);
    }
    
    @Override
    public void constructCastingRules(IEnvironmentGlobal environment, ICastingRuleDelegate rules, boolean followCasters) {
    
    }
    
    @Override
    public IZenIterator makeIterator(int numValues, IEnvironmentMethod methodOutput) {
        return null;
    }
    
    @Override
    public Class<?> toJavaClass() {
        return cls;
    }
    
    @Override
    public Type toASMType() {
        return Type.getType(toJavaClass());
    }
    
    @Override
    public int getNumberType() {
        return 0;
    }
    
    @Override
    public String getSignature() {
        return ZenTypeUtil.signature(toJavaClass());
    }
    
    @Override
    public boolean isPointer() {
        return true;
    }
    
    @Override
    public String getAnyClassName(IEnvironmentGlobal environment) {
        return getName();
    }
    
    @Override
    public String getName() {
        return toJavaClass().getCanonicalName();
    }
    
    @Override
    public Expression defaultValue(ZenPosition position) {
        return new ExpressionNull(position);
    }
    
    @Override
    public ICastingRule getCastingRule(ZenType type, IEnvironmentGlobal environment) {
        return super.getCastingRule(type, environment);
    }
}
