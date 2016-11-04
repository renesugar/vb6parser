/*
 * Copyright (C) 2016, Ulrich Wolffgang <u.wol@wwu.de>
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the BSD 3-clause license. See the LICENSE file for details.
 */

package io.proleap.vb6.parser.visitor.impl;

import org.antlr.v4.runtime.misc.NotNull;

import io.proleap.vb6.VisualBasic6Parser;
import io.proleap.vb6.VisualBasic6Parser.EnumerationStmtContext;
import io.proleap.vb6.parser.metamodel.Enumeration;
import io.proleap.vb6.parser.metamodel.Module;
import io.proleap.vb6.parser.metamodel.Program;
import io.proleap.vb6.parser.metamodel.impl.ClazzModuleImpl;
import io.proleap.vb6.parser.metamodel.impl.StandardModuleImpl;

/**
 * Visitor for collecting type declarations in the AST.
 */
public class VbTypeVisitorImpl extends AbstractVbParserVisitorImpl {

	protected final boolean isClazzModule;

	protected final boolean isStandardModule;

	protected final String moduleName;

	protected final Program program;

	public VbTypeVisitorImpl(final Program program, final String moduleName, final boolean isClazzModule,
			final boolean isStandardModule) {
		super(null);

		this.program = program;
		this.moduleName = moduleName;
		this.isClazzModule = isClazzModule;
		this.isStandardModule = isStandardModule;
	}

	@Override
	public Boolean visitEnumerationStmt(@NotNull final VisualBasic6Parser.EnumerationStmtContext ctx) {
		module.addEnumeration(ctx);

		return visitChildren(ctx);
	}

	@Override
	public Boolean visitEnumerationStmt_Constant(
			@NotNull final VisualBasic6Parser.EnumerationStmt_ConstantContext ctx) {
		final EnumerationStmtContext enumerationCtx = (EnumerationStmtContext) ctx.getParent();
		final Enumeration enumeration = (Enumeration) getSemanticGraphElement(enumerationCtx);

		enumeration.addEnumerationConstant(ctx);

		return visitChildren(ctx);
	}

	@Override
	public Boolean visitModule(@NotNull final VisualBasic6Parser.ModuleContext ctx) {
		final Module result;

		if (isClazzModule) {
			result = new ClazzModuleImpl(moduleName, program, ctx);
		} else if (isStandardModule) {
			result = new StandardModuleImpl(moduleName, program, ctx);
		} else {
			result = null;
		}

		module = result;

		return visitChildren(ctx);
	}

}