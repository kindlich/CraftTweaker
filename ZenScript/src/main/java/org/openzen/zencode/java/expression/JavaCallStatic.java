/*
 * This file is part of ZenCode, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 openzen.org <http://zencode.openzen.org>
 */
package org.openzen.zencode.java.expression;

import java.util.List;
import org.openzen.zencode.java.method.IJavaMethod;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.type.IGenericType;

/**
 *
 * @author Stan
 */
public class JavaCallStatic extends AbstractJavaExpression
{
	private final IJavaMethod method;
	private final List<IJavaExpression> arguments;
	
	public JavaCallStatic(CodePosition position, IMethodScope<IJavaExpression> scope, IJavaMethod method, List<IJavaExpression> arguments)
	{
		super(position, scope);
		
		this.method = method;
		this.arguments = arguments;
	}

	@Override
	public void compileValue(MethodOutput output)
	{
		compileCall(output);
		
		if (method.getReturnType() == getScope().getTypeCompiler().void_)
			throw new RuntimeException("Method has no return value");
	}
	
	@Override
	public void compileStatement(MethodOutput output)
	{
		compileCall(output);
		
		if (method.getReturnType() != getScope().getTypeCompiler().void_)
			output.pop(method.getReturnType());
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return method.getReturnType();
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return null;
	}

	@Override
	public void validate()
	{
		if (!method.accepts(arguments))
			getScope().getErrorLogger().errorInvalidMethodCall(getPosition(), method.getMethodName(), arguments);
	}
	
	private void compileCall(MethodOutput output)
	{
		for (IJavaExpression argument : arguments) {
			argument.compileValue(output);
		}
		
		output.invokeStatic(
					method.getDeclaringClass(),
					method.getMethodName(),
					method.getMethodSignature());
	}
}
