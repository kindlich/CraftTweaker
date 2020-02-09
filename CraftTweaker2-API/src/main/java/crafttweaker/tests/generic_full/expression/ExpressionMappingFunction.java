package crafttweaker.tests.generic_full.expression;

import crafttweaker.tests.generic_full.*;
import crafttweaker.tests.generic_full.type.*;
import org.objectweb.asm.*;
import stanhebben.zenscript.compiler.*;
import stanhebben.zenscript.definitions.*;
import stanhebben.zenscript.expression.*;
import stanhebben.zenscript.statements.*;
import stanhebben.zenscript.symbols.*;
import stanhebben.zenscript.type.*;
import stanhebben.zenscript.util.*;

import java.lang.reflect.*;
import java.util.*;

import static stanhebben.zenscript.util.ZenTypeUtil.*;

public class ExpressionMappingFunction extends Expression {
    
    private final Class<?> interfaceClass;
    private final List<ParsedFunctionArgument> arguments;
    private final List<Statement> statements;
    private final String descriptor;
    private final ZenType type;
    public Class<?> genericClass;
    private ZenType genericReturnType;
    
    public ExpressionMappingFunction(ExpressionMappingFunction fun, ZenTypeGenericSequence sequence) {
        super(fun.getPosition());
        this.interfaceClass = fun.interfaceClass;
        this.arguments = new ArrayList<>(fun.arguments);
        this.arguments.set(0, new GenericSequenceParsedFunctionArgument(this.arguments.get(0), sequence));
    
        ZenType genericType = arguments.get(0).getType();
        this.genericClass = genericType.equals(ZenType.ANY) ? Object.class : genericType.toJavaClass();
        
        this.statements = fun.statements;
        this.type = fun.type;
    
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(int i = 0; i < arguments.size(); i++) {
            ZenType t = arguments.get(i).getType();
            if(t.equals(ZenType.ANY)) {
                sb.append(signature(interfaceClass.getMethods()[0].getParameterTypes()[i]));
            } else {
                sb.append(t.getSignature());
            }
        }
        sb.append(")").append(signature(interfaceClass.getDeclaredMethods()[0].getReturnType()));
        this.descriptor = sb.toString();
        
        this.genericReturnType = ZenType.ANY;
    }
    
    public ExpressionMappingFunction(ExpressionJavaLambdaSimpleGeneric fun, ZenTypeGenericSequence genericSequence) {
        super(fun.getPosition());
        
        this.interfaceClass = fun.getInterfaceClass();
        this.arguments = new ArrayList<>(fun.getArguments());
        this.statements = fun.getStatements();
        
        this.type = fun.getType();
        
        arguments.set(0, new GenericSequenceParsedFunctionArgument(arguments.get(0), genericSequence));
        
        ZenType genericType = arguments.get(0).getType();
        this.genericClass = genericType.equals(ZenType.ANY) ? Object.class : genericType.toJavaClass();
        
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(int i = 0; i < arguments.size(); i++) {
            ZenType t = arguments.get(i).getType();
            if(t.equals(ZenType.ANY)) {
                sb.append(signature(interfaceClass.getMethods()[0].getParameterTypes()[i]));
            } else {
                sb.append(t.getSignature());
            }
        }
        sb.append(")").append(signature(interfaceClass.getDeclaredMethods()[0].getReturnType()));
        this.descriptor = sb.toString();
        this.genericReturnType = ZenTypeAny.ANY;
    }
    
    public Class getInterfaceClass() {
        return interfaceClass;
    }
    
    public Class getGenericClass() {
        return genericClass;
    }
    
    public List<ParsedFunctionArgument> getArguments() {
        return arguments;
    }
    
    public List<Statement> getStatements() {
        return statements;
    }
    
    @Override
    public ZenType getType() {
        return type;
    }
    
    @Override
    public void compile(boolean result, IEnvironmentMethod environment) {
        if(!result)
            return;
        
        Method method = interfaceClass.getMethods()[0];
        
        // generate class
        String clsName = environment.makeClassNameWithMiddleName(getPosition().getFile()
                .getClassName());
        
        ClassWriter cw = new ZenClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC, clsName, createMethodSignature(), "java/lang/Object", new String[]{internal(interfaceClass)});
        
        
        MethodOutput constructor = new MethodOutput(cw, Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        constructor.start();
        constructor.loadObject(0);
        constructor.invokeSpecial("java/lang/Object", "<init>", "()V");
        constructor.ret();
        constructor.end();
        
        MethodOutput output = new MethodOutput(cw, Opcodes.ACC_PUBLIC, method.getName(), descriptor, null, null);
        IEnvironmentClass environmentClass = new EnvironmentClass(cw, environment);
        IEnvironmentMethod environmentMethod = new EnvironmentMethod(output, environmentClass);
        
        for(int i = 0, j = 0; i < arguments.size(); i++) {
            ZenType typeToPut = arguments.get(i).getType();
            if(typeToPut.equals(ZenType.ANY))
                typeToPut = environment.getType(method.getGenericParameterTypes()[i]);
            if(typeToPut == null)
                typeToPut = environment.getType(method.getParameterTypes()[i]);
            
            environmentMethod.putValue(arguments.get(i)
                    .getName(), new SymbolArgument(i + 1 + j, typeToPut), getPosition());
            if(typeToPut.isLarge())
                j++;
        }
        
        output.start();
        for(Statement statement : statements) {
            statement.compile(environmentMethod);
            for(Statement subStatement : statement.getSubStatements()) {
                if(subStatement instanceof StatementReturn) {
                    genericReturnType = ((StatementReturn) subStatement).getReturnType();
                }
            }
        }
        output.ret();
        output.end();
        
        if(!Objects.equals(genericClass, Object.class)) {
            MethodOutput bridge = new MethodOutput(cw, Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_BRIDGE, method
                    .getName(), ZenTypeUtil.descriptor(method), null, null);
            bridge.loadObject(0);
            bridge.loadObject(1);
            bridge.checkCast(internal(genericClass));
            if(arguments.size() > 1) {
                for(int i = 1; i < arguments.size(); ) {
                    bridge.load(org.objectweb.asm.Type.getType(method.getParameterTypes()[i]), ++i);
                }
            }
            
            bridge.invokeVirtual(clsName, method.getName(), descriptor);
            bridge.returnType(org.objectweb.asm.Type.getReturnType(method));
            bridge.end();
        }
        
        environment.putClass(clsName, cw.toByteArray());
        
        // make class instance
        environment.getOutput().newObject(clsName);
        environment.getOutput().dup();
        environment.getOutput().construct(clsName);
    }
    
    private String createMethodSignature() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ljava/lang/Object;");
        sb.append(signature(interfaceClass));
        sb.deleteCharAt(sb.length() - 1);
        sb.append("<").append(signature(genericClass)).append(">").append(";");
        return sb.toString();
    }
    
    public ZenType getGenericReturnType() {
        return genericReturnType;
    }
}
