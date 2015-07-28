/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package org.openzen.zencode;

import java.io.IOException;
import java.util.List;
import org.openzen.zencode.annotations.OperatorType;
import org.openzen.zencode.lexer.Token;
import org.openzen.zencode.parser.expression.ParsedCallArguments;
import org.openzen.zencode.symbolic.Modifier;
import org.openzen.zencode.symbolic.definition.IImportable;
import org.openzen.zencode.symbolic.expression.IPartialExpression;
import org.openzen.zencode.symbolic.member.IMember;
import org.openzen.zencode.symbolic.method.ICallable;
import org.openzen.zencode.symbolic.method.MethodHeader;
import org.openzen.zencode.symbolic.scope.IMethodScope;
import org.openzen.zencode.symbolic.symbols.IZenSymbol;
import org.openzen.zencode.symbolic.type.IGenericType;
import org.openzen.zencode.symbolic.type.ITypeDefinition;
import org.openzen.zencode.util.CodePosition;
import org.openzen.zencode.util.Strings;

/**
 *
 * @author Stan
 * @param <E>
 */
public class AbstractErrorLogger<E extends IPartialExpression<E>>
	implements ICodeErrorLogger<E>
{
	private boolean hasErrors;
	
	public AbstractErrorLogger()
	{
		hasErrors = false;
	}
	
	public void error(CodePosition position, String message)
	{
		hasErrors = true;
	}
	
	public void warning(CodePosition position, String message)
	{
		
	}
	
	@Override
	public boolean hasErrors()
	{
		return hasErrors;
	}
	
	@Override
	public void errorSymbolNameAlreadyExists(CodePosition position, String name)
	{
		error(position, "Value already defined in this scope: " + name);
	}

	@Override
	public void errorCannotAssignInConstantScope(CodePosition position)
	{
		error(position, "Cannot assign to a variable in a constant scope");
	}

	@Override
	public void errorNoBreakableControlStatement(CodePosition position)
	{
		error(position, "No control loop statement to break from");
	}
	
	@Override
	public void errorNoContinuableControlStatement(CodePosition position)
	{
		error(position, "No control loop statement to continue from");
	}

	@Override
	public void errorInvalidNumberOfGenericArguments(CodePosition position, ITypeDefinition<E> type, List<IGenericType<E>> genericArguments)
	{
		error(position, "Invalid number of generic arguments for " + type);
	}

	@Override
	public void errorCannotCastImplicit(CodePosition position, IGenericType<E> fromType, IGenericType<E> toType)
	{
		error(position, "No implicit cast available from " + fromType + " to " + toType);
	}

	@Override
	public void errorCannotCastExplicit(CodePosition position, IGenericType<E> fromType, IGenericType<E> toType)
	{
		error(position, "Cannot cast " + fromType + " to " + toType);
	}

	@Override
	public void errorCannotAssignTo(CodePosition position, IPartialExpression<E> target)
	{
		error(position, "Cannot assign to this value");
	}

	@Override
	public void errorNotAType(CodePosition position, IImportable<E> importable)
	{
		error(position, "Not a valid type");
	}

	@Override
	public void errorCannotBeNull(CodePosition position)
	{
		error(position, "Value cannot be null");
	}

	@Override
	public void errorInvalidNumberOfArguments(CodePosition position)
	{
		error(position, "Invalid number of arguments");
	}

	@Override
	public void errorCannotCastArrayTo(CodePosition position, IGenericType<E> toType)
	{
		error(position, "Cannot cast array to " + toType);
	}

	@Override
	public void errorNotAValidParameterName(CodePosition position)
	{
		error(position, "Not a valid parameter name");
	}

	@Override
	public void errorUnexpectedEndOfFile(CodePosition position)
	{
		error(position, "Unexpected end of file");
	}

	@Override
	public void errorCannotCastMapTo(CodePosition position, IGenericType<E> toType)
	{
		error(position, "Cannot cast map to " + toType);
	}

	@Override
	public void errorKeyRequired(CodePosition position)
	{
		error(position, "Missing key");
	}

	@Override
	public void errorCouldNotResolveBracket(CodePosition position, List<Token> tokens)
	{
		StringBuilder builder = new StringBuilder();
		builder.append('<');
		Token last = null;
		for (Token token : tokens) {
			if (last != null)
				builder.append(' ');

			builder.append(token.getValue());
			last = token;
		}
		builder.append('>');

		error(position, "Could not resolve " + builder.toString());
	}

	@Override
	public void errorNotAValidMethod(CodePosition position)
	{
		error(position, "Not a valid method");
	}

	@Override
	public void errorNoMatchingMethod(CodePosition position, List<ICallable<E>> methods, ParsedCallArguments arguments)
	{
		// TODO: assemble a more useful error message, since this causes common confusion
		
		if (methods.size() == 1)
			error(position, "Method arguments don't match");
		else
			error(position, methods.size() + " methods available but none match the given arguments");
	}

	@Override
	public void errorNoSuchDollarVariable(CodePosition position, String name)
	{
		error(position, "No such dollar variable: $" + name);
	}

	@Override
	public void errorCannotCombineTypes(CodePosition position, IGenericType<E> type1, IGenericType<E> type2)
	{
		error(position, "Cannot combine " + type1 + " with " + type2);
	}

	@Override
	public void errorCannotConvertToChar(CodePosition position, String value)
	{
		error(position, "Cannot convert \"" + value + "\" to a char value");
	}

	@Override
	public void errorCouldNotResolveSymbol(CodePosition position, String name)
	{
		error(position, "Could not resolve " + name);
	};

	@Override
	public void errorNotAValidMemberToken(CodePosition position, Token token)
	{
		error(position, "Not a valid member: " + token.getValue());
	}

	@Override
	public void errorNoLabeledControlStatement(CodePosition position, String label)
	{
		error(position, "No control statement with the label " + label);
	}

	@Override
	public void errorCaseOutsideSwitch(CodePosition position)
	{
		error(position, "Cannot have a case outside a switch statement");
	}

	@Override
	public void errorDefaultOutsideSwitch(CodePosition position)
	{
		error(position, "Cannot have a default outside a switch statement");
	}

	@Override
	public void errorNoSuchIterator(CodePosition position, IGenericType<E> type, int numVariables)
	{
		error(position, type + " has no iterator for " + numVariables + " variables");
	}

	@Override
	public void errorCouldNotResolvePackage(CodePosition position, String packageName)
	{
		error(position, "Could not resolve " + packageName);
	}

	@Override
	public void errorNotAType(CodePosition position, IZenSymbol<E> value, String name)
	{
		error(position, name + " is not a valid type");
	}

	@Override
	public void errorDuplicateCase(CodePosition position, E value)
	{
		error(position, "Duplicate case label");
	}

	@Override
	public void errorDuplicateDefault(CodePosition position)
	{
		error(position, "Duplicate default");
	}

	@Override
	public void errorMissingReturnValue(CodePosition position)
	{
		error(position, "Missing return value");
	}

	@Override
	public void errorInvalidSwitchValueType(CodePosition position, E value)
	{
		error(position, "Invalid enum value type: " + value.getType());
	}

	@Override
	public void errorCannotBeNullable(CodePosition position, IGenericType<E> type)
	{
		error(position, "Cannot be nullable: " + type);
	}

	@Override
	public void errorInvalidExpression(CodePosition position, IPartialExpression<E> expression)
	{
		error(position, "Invalid expression");
	}

	@Override
	public void errorCannotCall(CodePosition position, IPartialExpression<E> expression)
	{
		error(position, "Cannot call such value");
	}

	@Override
	public void errorInvalidOperatorArguments(CodePosition position, OperatorType operator, MethodHeader<E> header, IGenericType<E>... expectedTypes)
	{
		StringBuilder errorBuilder = new StringBuilder();
		errorBuilder.append("Illegal parameters for ");
		errorBuilder.append(operator.getOperatorString());
		errorBuilder.append(" operator (expected ");
		Strings.join(expectedTypes, ", ");
		errorBuilder.append(")");
		
		error(position, errorBuilder.toString());
	}

	@Override
	public void errorConstructorHasReturnType(CodePosition position)
	{
		error(position, "Constructors can't have return types");
	}

	@Override
	public void errorVoidParameter(CodePosition position, String name)
	{
		error(position, "Parameter type cannot be void");
	}

	@Override
	public void errorCannotLoadInclude(CodePosition position, String filename)
	{
		error(position, "Could not load included file " + filename);
	}

	@Override
	public void errorModifiersForInclude(CodePosition position)
	{
		error(position, "Include cannot have modifiers");
	}

	@Override
	public void errorAnnotationsForInclude(CodePosition position)
	{
		error(position, "Include cannot have annotations");
	}

	@Override
	public void errorModifiersForStatement(CodePosition position)
	{
		error(position, "Statement cannot have modifiers");
	}

	@Override
	public void errorAnnotationsForStatement(CodePosition position)
	{
		error(position, "Statement cannot have annotations");
	}

	@Override
	public void errorCannotLoadInclude(CodePosition position, String filename, IOException exception)
	{
		error(position, "Could not load included file " + filename + ": " + exception.getMessage());
	}

	@Override
	public void errorNotAPackage(CodePosition position, String name)
	{
		error(position, name + " is not a package");
	}

	@Override
	public void errorAlreadyDefinedInPackage(CodePosition position, String name, String packageName)
	{
		error(position, name + " is already defined in " + packageName);
	}

	@Override
	public void errorDuplicateModifier(CodePosition position, Modifier modifier)
	{
		error(position, "Duplicate " + modifier.getName() + " modifier");
	}

	@Override
	public void errorIncompatibleModifier(CodePosition position, Modifier a, Modifier b)
	{
		error(position, "Cannot combine modifiers " + a.getName() + " and " + b.getName());
	}

	@Override
	public void errorMultipleSuperclasses(CodePosition position, String className)
	{
		error(position, "Class " + className + " cannot have multiple superclasses");
	}

	@Override
	public void errorNoConstructorsForType(CodePosition position, IGenericType<E> type)
	{
		error(position, "This type has no constructors");
	}

	@Override
	public void errorNoSuchMember(CodePosition position, IImportable<E> importable, String name)
	{
		error(position, "Could not find a member named " + name + " in this type");
	}

	@Override
	public void errorNamedWildcardImport(CodePosition position, List<String> importName)
	{
		error(position, "Cannot rename a wildcard import");
	}
	
	@Override
	public void errorNoGetterForMember(CodePosition position, String name)
	{
		error(position, "Member " + name + " has no getter");
	}

	@Override
	public void errorNoSetterForMember(CodePosition position, String name)
	{
		error(position, "Member " + name + " has no setter");
	}

	@Override
	public void errorNoSuchOperator(CodePosition position, IGenericType<E> type, OperatorType operator)
	{
		error(position, "No such operator: " + operator.getOperatorString());
	}

	@Override
	public void errorAmbiguousMethodCall(CodePosition position)
	{
		error(position, "Ambiguous method call");
	}

	@Override
	public void errorFunctionHasNoMembers(CodePosition position)
	{
		error(position, "Functions have no members");
	}

	@Override
	public void errorNullHasNoMembers(CodePosition position)
	{
		error(position, "Null has no members");
	}

	@Override
	public void errorVoidHasNoMembers(CodePosition position)
	{
		error(position, "Void has no members");
	}

	@Override
	public void errorNotAStaticMember(CodePosition position, IMember<E> member)
	{
		error(position, "This member is not static");
	}

	@Override
	public void errorValueOutsideRange(CodePosition position, long value)
	{
		error(position, "Value outside range: " + value);
	}

	@Override
	public void errorInvalidMethodCall(CodePosition position, String methodName, List<E> expressions)
	{
		error(position, "Invalid method call");
	}

	@Override
	public void errorSuperIsNotValue(CodePosition position, IMethodScope<E> scope)
	{
		error(position, "super is not a proper value");
	}

	@Override
	public void errorNoThisInConstant(CodePosition position)
	{
		error(position, "constants cannot dereference this");
	}
}
