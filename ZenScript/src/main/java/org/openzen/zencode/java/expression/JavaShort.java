/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode.java.expression;

import org.openzen.zencode.java.util.MethodOutput;
import org.openzen.zencode.runtime.IAny;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.util.CodePosition;

/**
 *
 * @author Stan
 */
public class JavaShort extends AbstractJavaExpression
{
	private final short value;
	
	public JavaShort(CodePosition position, IMethodScope<IJavaExpression> scope, short value)
	{
		super(position, scope);
		
		this.value = value;
	}

	@Override
	public void compileValue(MethodOutput method)
	{
		method.constant(value);
	}
	
	@Override
	public void compileStatement(MethodOutput method)
	{
		
	}

	@Override
	public IGenericType<IJavaExpression> getType()
	{
		return getScope().getTypeCompiler().short_;
	}

	@Override
	public IAny getCompileTimeValue()
	{
		return null; // TODO
	}

	@Override
	public void validate()
	{
		
	}
}
