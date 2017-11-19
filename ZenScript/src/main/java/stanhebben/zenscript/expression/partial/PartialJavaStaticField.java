package stanhebben.zenscript.expression.partial;

import java.lang.reflect.Field;

import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.compiler.IEnvironmentMethod;
import stanhebben.zenscript.compiler.ITypeRegistry;
import stanhebben.zenscript.expression.Expression;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.symbols.SymbolJavaStaticField;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.util.ZenPosition;

public class PartialJavaStaticField implements IPartialExpression {

	private final Class<?> clazz;
	private final Field field;
	private final String name;
	private final IEnvironmentGlobal envo;
	private final ZenPosition position;
	
	public PartialJavaStaticField(Class<?> clazz, Field field, String name, IEnvironmentGlobal envo, ZenPosition position) {
		
		this.clazz = clazz;
		this.field = field;
		this.name = name;
		this.envo = envo;
		this.position = position;
		
		System.out.println("PJSW GENERATED--------------");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Expression eval(IEnvironmentGlobal environment) {
		return new ExpressionJavaStaticField(position, clazz, field, environment);
	}

	@Override
	public Expression assign(ZenPosition position, IEnvironmentGlobal environment, Expression other) {
		// TODO Auto-generated method stub
		System.out.println("Assign CALLED");
		return null;
	}

	@Override
	public IPartialExpression getMember(ZenPosition position, IEnvironmentGlobal environment, String name) {
		// TODO Auto-generated method stub
		System.out.println("GetMember CALLED");
		return null;
	}

	@Override
	public Expression call(ZenPosition position, IEnvironmentMethod environment, Expression... values) {
		// TODO Auto-generated method stubhjhj
		System.out.println("CALL CALLED");
		return null;
	}

	@Override
	public ZenType[] predictCallTypes(int numArguments) {
		// TODO Auto.generated method stub 
		System.out.println("predictCallTypes CALLED");
		return null;
	}

	@Override
	public IZenSymbol toSymbol() {
		return new SymbolJavaStaticField(clazz, field, envo);
	}

	@Override
	public ZenType getType() {
		System.out.println("getType called");
		return envo.getType(field.getGenericType());
	}

	@Override
	public ZenType toType(IEnvironmentGlobal environment) {
		return envo.getType(field.getGenericType());
	}

}
