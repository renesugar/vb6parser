/*
 * Copyright (C) 2016, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.vb6.parser.metamodel.statement.function.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.proleap.vb6.VisualBasic6Parser.FunctionStmtContext;
import io.proleap.vb6.parser.applicationcontext.VbParserContext;
import io.proleap.vb6.parser.metamodel.Module;
import io.proleap.vb6.parser.metamodel.call.FunctionCall;
import io.proleap.vb6.parser.metamodel.impl.ProcedureImpl;
import io.proleap.vb6.parser.metamodel.statement.StatementType;
import io.proleap.vb6.parser.metamodel.statement.StatementTypeEnum;
import io.proleap.vb6.parser.metamodel.statement.function.Function;
import io.proleap.vb6.parser.metamodel.type.Type;

public class FunctionImpl extends ProcedureImpl implements Function {

	protected final FunctionStmtContext ctx;

	protected final List<FunctionCall> functionCalls = new ArrayList<FunctionCall>();

	protected boolean isDeclaredAsArray;

	protected final StatementType statementType = StatementTypeEnum.Function;

	protected final Type type;

	/*
	 * LinkedHashSet, so that entries are ordered by their insertion order,
	 * giving precedence to types declared near to the assigned variable
	 */
	protected Set<Type> typesOfAssignedValues = new LinkedHashSet<Type>();

	public FunctionImpl(final String name, final Type type, final Module module, final FunctionStmtContext ctx) {
		super(name, module, ctx);

		this.ctx = ctx;
		this.type = type;
	}

	@Override
	public void addFunctionCall(final FunctionCall functionCall) {
		functionCalls.add(functionCall);
	}

	@Override
	public void addTypeOfAssignedValue(final Type type) {
		if (type != null) {
			typesOfAssignedValues.add(type);
		}
	}

	@Override
	public FunctionStmtContext getCtx() {
		return ctx;
	}

	@Override
	public List<FunctionCall> getFunctionCalls() {
		return functionCalls;
	}

	@Override
	public StatementType getStatementType() {
		return statementType;
	}

	@Override
	public Type getType() {
		final Type defType = VbParserContext.getInstance().getTypeInference().inferTypeFromDefType(module, name);
		final Type result = VbParserContext.getInstance().getTypeInference().inferType(type, defType,
				typesOfAssignedValues);
		return result;
	}

	@Override
	public Set<Type> getTypesOfAssignedValues() {
		return typesOfAssignedValues;
	}

	@Override
	public boolean hasReturnType() {
		return true;
	}

	@Override
	public boolean isDeclaredAsArray() {
		return isDeclaredAsArray;
	}

	@Override
	public void setDeclaredAsArray(final boolean isDeclaredAsArray) {
		this.isDeclaredAsArray = isDeclaredAsArray;
	}

}