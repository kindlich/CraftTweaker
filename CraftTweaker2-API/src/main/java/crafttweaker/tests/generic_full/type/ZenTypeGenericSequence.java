package crafttweaker.tests.generic_full.type;

import crafttweaker.tests.generic_full.*;
import crafttweaker.tests.generic_full.expression.*;
import org.objectweb.asm.*;
import org.objectweb.asm.Type;
import stanhebben.zenscript.annotations.*;
import stanhebben.zenscript.compiler.*;
import stanhebben.zenscript.expression.*;
import stanhebben.zenscript.expression.partial.*;
import stanhebben.zenscript.type.*;
import stanhebben.zenscript.type.casting.*;
import stanhebben.zenscript.type.natives.*;
import stanhebben.zenscript.util.*;

import java.lang.reflect.*;
import java.util.function.*;

public class ZenTypeGenericSequence extends ZenType {
    
    private static final Class<?> cls = MySequence.class;
    private Supplier<ZenType> supplier;
    
    public ZenTypeGenericSequence(ZenType genericType) {
        super();
        this.supplier = () -> genericType;
    }
    
    public ZenTypeGenericSequence(Supplier<ZenType> supplier) {
        super();
        this.supplier = supplier;
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
        return getMember(position, environment, value, name, true);
    }
    
    private IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, IPartialExpression value, String name, boolean recursionGuard) {
        if(recursionGuard && this.getGenericType() == ZenType.ANY) {
            return updateType(value, environment).getMember(position, environment, value, name, false);
        }
        
        switch(name) {
            case "filter":
            case "forEach":
            case "map":
                return new PartialGenericSequenceMember(position, name, environment, this, value);
        }
        environment.error(position, "No such member: " + name);
        return new ExpressionInvalid(position);
    }
    
    private ZenTypeGenericSequence updateType(IPartialExpression value, IEnvironmentGlobal environment) {
        final IJavaMethod method;
        
        if(value instanceof ExpressionCallStatic) {
            method = ((ExpressionCallStatic) value).getMethod();
        } else if(value instanceof ExpressionCallVirtual) {
            method = ((ExpressionCallVirtual) value).getMethod();
        } else {
            return this;
        }
        
        if(method instanceof JavaMethod) {
            java.lang.reflect.Type genericReturnType = ((JavaMethod) method).getMethod()
                    .getGenericReturnType();
            
            if(genericReturnType instanceof ParameterizedType) {
                ZenType type = environment.getType(((ParameterizedType)genericReturnType).getActualTypeArguments()[0]);
                return new ZenTypeGenericSequence(() -> type);
            }
        }
        
        return this;
    }
    
    @Override
    public IPartialExpression getStaticMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
        environment.error(position, "No such static member: " + name);
        return new ExpressionInvalid(position);
    }
    
    @Override
    public Expression call(ZenPosition position, IEnvironmentGlobal environment, Expression receiver, Expression... arguments) {
        if(receiver != null) {
            environment.error(position, "Cannot call a sequence");
            return new ExpressionInvalid(position);
        }
        
        return new ExpressionSequenceNew(position, supplier.get(), arguments);
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
        return null;
    }
    
    public ZenType getGenericType() {
        return supplier.get();
    }
    
    public void setGenericType(ZenType genericType) {
        this.supplier = () -> genericType;
    }
}
