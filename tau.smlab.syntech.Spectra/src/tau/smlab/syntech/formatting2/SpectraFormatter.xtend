/*
Copyright (c) since 2015, Tel Aviv University and Software Modeling Lab

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of Tel Aviv University and Software Modeling Lab nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Tel Aviv University and Software Modeling Lab 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
*/

/*
 * generated by Xtext 2.10.0
 */
package tau.smlab.syntech.formatting2

import org.eclipse.xtext.formatting2.AbstractFormatter2
import org.eclipse.xtext.formatting2.IFormattableDocument
import tau.smlab.syntech.services.SpectraGrammarAccess
import tau.smlab.syntech.spectra.Decl
import tau.smlab.syntech.spectra.Define
import tau.smlab.syntech.spectra.DefineDecl
import tau.smlab.syntech.spectra.Import
import tau.smlab.syntech.spectra.LTLAsm
import tau.smlab.syntech.spectra.LTLGar
import tau.smlab.syntech.spectra.Model
import tau.smlab.syntech.spectra.Pattern
import tau.smlab.syntech.spectra.PatternParam
import tau.smlab.syntech.spectra.PatternParamList
import tau.smlab.syntech.spectra.Predicate
import tau.smlab.syntech.spectra.Subrange
import tau.smlab.syntech.spectra.TemporalAdditiveExpr
import tau.smlab.syntech.spectra.TemporalAndExpr
import tau.smlab.syntech.spectra.TemporalBinaryExpr
import tau.smlab.syntech.spectra.TemporalExpression
import tau.smlab.syntech.spectra.TemporalIffExpr
import tau.smlab.syntech.spectra.TemporalImpExpr
import tau.smlab.syntech.spectra.TemporalMultiplicativeExpr
import tau.smlab.syntech.spectra.TemporalOrExpr
import tau.smlab.syntech.spectra.TemporalPrimaryExpr
import tau.smlab.syntech.spectra.TemporalRelationalExpr
import tau.smlab.syntech.spectra.TemporalRemainderExpr
import tau.smlab.syntech.spectra.TemporalUnaryExpr
import tau.smlab.syntech.spectra.TypeConstant
import tau.smlab.syntech.spectra.TypeDef
import tau.smlab.syntech.spectra.TypedParam
import tau.smlab.syntech.spectra.TypedParamList
import tau.smlab.syntech.spectra.Var
import tau.smlab.syntech.spectra.VarDecl
import tau.smlab.syntech.spectra.VarOwner
import tau.smlab.syntech.spectra.VarType
import tau.smlab.syntech.spectra.WeightDef
import javax.inject.Inject
import tau.smlab.syntech.spectra.DefineArray

class SpectraFormatter extends AbstractFormatter2 {
	// @Inject extension SpectraGrammarAccess
	@Inject SpectraGrammarAccess grammarAccess;

	def dispatch void format(Model model, extension IFormattableDocument document) {
		model.prepend[setNewLines(0, 0, 1); noSpace].append[newLine]
		model.regionFor.keyword(grammarAccess.modelAccess.specKeyword_1_1.value).append[oneSpace] // "module"
		for (Import imports : model.getImports()) {
			imports.format;
		}
		for (Decl elements : model.getElements()) {
			elements.format;
		}
	}

	def dispatch void format(Import _import, extension IFormattableDocument document) {
		_import.regionFor.keyword(grammarAccess.importAccess.importKeyword_0.value).prepend[noSpace].append[oneSpace] // "import"
	}

	def dispatch void format(Var _var, extension IFormattableDocument document) {
		_var.kind.format;
		_var.getVar.format;
	}

	def dispatch void format(VarOwner varOwner, extension IFormattableDocument document) {
	}

	def dispatch void format(VarDecl varDecl, extension IFormattableDocument document) {
		varDecl.type.format;
	}

	def dispatch void format(VarType varType, extension IFormattableDocument document) {
		varType.regionFor.keyword(grammarAccess.varTypeAccess.getLeftCurlyBracketKeyword_0_2_0().value).prepend [
			oneSpace
		].append[noSpace] // "{"
		varType.regionFor.keyword(grammarAccess.varTypeAccess.getRightCurlyBracketKeyword_0_2_3().value).prepend [
			noSpace
		].append[noSpace] // "}"
		varType.regionFor.keyword(grammarAccess.varTypeAccess.getCommaKeyword_0_2_2_0().value).prepend[noSpace].append [
			oneSpace
		] // ","
		varType.subr.format;
//    for (TypeConstant typeConstant : varType.const) {
//      typeConstant.format;
//    }
		varType.type.format;
	}

	def dispatch void format(Subrange subrange, extension IFormattableDocument document) {
		subrange.regionFor.keyword(grammarAccess.subrangeAccess.getFullStopFullStopKeyword_1().value).prepend[noSpace].
			append[noSpace] // '..'
	}

	def dispatch void format(TypeConstant typeConstant, extension IFormattableDocument document) {
	}

	def dispatch void format(TypeDef typeDef, extension IFormattableDocument document) {
		typeDef.regionFor.keyword(grammarAccess.typeDefAccess.typeKeyword_0.value).prepend[setNewLines(0, 0, 2)].append [
			oneSpace
		] // "type"
		typeDef.type.format;
	}

	def dispatch void format(Define define, extension IFormattableDocument document) {
		define.regionFor.keyword(grammarAccess.defineAccess.defineKeyword_0.value).prepend[setNewLines(0, 0, 2)].append [
			oneSpace
		] // "define"
		for (DefineDecl defineDecl : define.defineList) {
			defineDecl.format;
		}
		define.regionFor.keyword(grammarAccess.TOK_SEMIAccess.getSemicolonKeyword().value).append[setNewLines(1, 1, 1)]
	}

	def dispatch void format(DefineDecl defineDecl, extension IFormattableDocument document) {
		defineDecl.regionFor.keyword(grammarAccess.defineDeclAccess.getColonEqualsSignKeyword_0_0_1().value).surround [
			oneSpace
		] // ":="
		defineDecl.simpleExpr.format;
		defineDecl.regionFor.keyword(grammarAccess.defineDeclAccess.getColonEqualsSignKeyword_0_1_2().value).surround [
			oneSpace
		] // ":="
		format(defineDecl.innerArray, document);
	}

	def dispatch void format(DefineArray defineArray, extension IFormattableDocument document) {

		for (DefineArray innerArray : defineArray.innerArrays) {
			format(innerArray, document);
		}
		for (TemporalExpression temporalExpr : defineArray.simpleExprs) {
			temporalExpr.format;
		}
	}

	def dispatch void format(TemporalImpExpr temporalImpExpr, extension IFormattableDocument document) {
		temporalImpExpr.left.format;
		temporalImpExpr.regionFor.keyword(
			grammarAccess.temporalImpExprAccess.getOperatorHyphenMinusGreaterThanSignKeyword_1_1_0_0().value).surround [
			oneSpace
		] // "->"
		temporalImpExpr.regionFor.keyword(
			grammarAccess.temporalImpExprAccess.getOperatorImpliesKeyword_1_1_0_1().value).surround[oneSpace] // implies
		temporalImpExpr.implication.format;
	}

	def dispatch void format(TemporalIffExpr temporalIffExpr, extension IFormattableDocument document) {
		for (TemporalExpression temporalOrExpr : temporalIffExpr.elements) {
			temporalOrExpr.format;
		}
	}

	def dispatch void format(TemporalOrExpr temporalOrExpr, extension IFormattableDocument document) {
		for (String op : temporalOrExpr.operator) {
			temporalOrExpr.regionFor.keyword(op).surround[oneSpace]
			temporalOrExpr.regionFor.keyword(
				grammarAccess.temporalOrExprAccess.getOperatorVerticalLineKeyword_1_1_0_0().value).append [
				setNewLines(0, 0, 1)
			].prepend[oneSpace] // '|'
		}
		for (TemporalExpression temporalAndExpr : temporalOrExpr.elements) {
			temporalAndExpr.format;
		}
	}

	def dispatch void format(TemporalAndExpr temporalAndExpr, extension IFormattableDocument document) {
		temporalAndExpr.regionFor.keyword(
			grammarAccess.temporalAndExprAccess.getOperatorAmpersandKeyword_1_1_0_0().value).surround[oneSpace] // "&"
		temporalAndExpr.regionFor.keyword(grammarAccess.temporalAndExprAccess.getOperatorAndKeyword_1_1_0_1().value).
			surround[oneSpace] // "and"
		for (TemporalExpression temporalRelationalExpr : temporalAndExpr.elements) {
			temporalRelationalExpr.format;
		}
	}

	def dispatch void format(TemporalRelationalExpr temporalRelationalExpr, extension IFormattableDocument document) {
		temporalRelationalExpr.regionFor.keyword(temporalRelationalExpr.operator).surround[oneSpace]
		temporalRelationalExpr.left.format;
		temporalRelationalExpr.right.format;
	}

	def dispatch void format(TemporalRemainderExpr temporalRemainderExpr, extension IFormattableDocument document) {
		temporalRemainderExpr.left.format;
		temporalRemainderExpr.right.format;
	}

	def dispatch void format(TemporalAdditiveExpr temporalAdditiveExpr, extension IFormattableDocument document) {
		for (TemporalExpression temporalMultiplicativeExpr : temporalAdditiveExpr.elements) {
			temporalMultiplicativeExpr.format;
		}
	}

	def dispatch void format(TemporalMultiplicativeExpr temporalMultiplicativeExpr,
		extension IFormattableDocument document) {
		for (TemporalExpression temporalBinaryExpr : temporalMultiplicativeExpr.elements) {
			temporalBinaryExpr.format;
		}
	}

	def dispatch void format(TemporalBinaryExpr temporalBinaryExpr, extension IFormattableDocument document) {
//    for (TemporalUnaryExpr temporalUnaryExpr : temporalBinaryExpr.elements) {
//      temporalUnaryExpr.format;
//    }
	}

	def dispatch void format(TemporalUnaryExpr temporalUnaryExpr, extension IFormattableDocument document) {
//    temporalUnaryExpr.tpe.format;
		temporalUnaryExpr.tue.format;
	}

	def dispatch void format(TemporalPrimaryExpr temporalPrimaryExpr, extension IFormattableDocument document) {
		// temporalPrimaryExpr.tpe.format;
		// temporalPrimaryExpr.regionFor.keyword(grammarAccess.temporalPrimaryExprAccess.getOperatorNextKeyword_2_1_3_0_0().value).prepend[setNewLines(1, 1, 1)]
		temporalPrimaryExpr.regionFor.keyword(
			grammarAccess.temporalPrimaryExprAccess.getLeftParenthesisKeyword_2_1_4_1().value).prepend[oneSpace].append [
			noSpace
		] // "("
		temporalPrimaryExpr.regionFor.keyword(
			grammarAccess.temporalPrimaryExprAccess.getRightParenthesisKeyword_2_1_4_3().value).prepend[noSpace].append [
			oneSpace
		] // ")"
		temporalPrimaryExpr.regionFor.keyword(
			grammarAccess.temporalPrimaryExprAccess.getCommaKeyword_2_1_0_1_0_2_0.value).prepend[noSpace].append [
			oneSpace
		] // ","
		val open = temporalPrimaryExpr.regionFor.keyword("{")
		val close = temporalPrimaryExpr.regionFor.keyword("}")
		interior(open, close)[indent]
		format(temporalPrimaryExpr.tpe, document);

	}

//  def dispatch void format(TemporalPrimaryExprHelper temporalPrimaryExprHelper,
//    extension IFormattableDocument document) {
//    // temporalPrimaryExprHelper.c.format;
//    temporalPrimaryExprHelper.predPatt.format;
//    for (TemporalExpression temporalExpression : temporalPrimaryExprHelper.predPattParams) {
//      temporalExpression.format;
//    }
//    temporalPrimaryExprHelper.pointer.format;
//    temporalPrimaryExprHelper.temporalExpression.format;
//  }
//  def dispatch void format(TemporalExpression temporalExpr, extension IFormattableDocument document) {
//  	val open = temporalExpr.regionFor.keyword(grammarAccess.temporalExpressionAccess.value)//"}"
//	val close = temporalExpr.regionFor.keyword(grammarAccess.patternAccess.getRightCurlyBracketKeyword_3_3().value)//"{"
//    interior(open, close)[indent]
//}
	def dispatch void format(Predicate predicate, extension IFormattableDocument document) {
		predicate.regionFor.keyword(grammarAccess.predicateAccess.predicateKeyword_0.value).prepend [
			setNewLines(0, 0, 2)
		].append[oneSpace] // "predicate"
		predicate.params.format;
		predicate.body.format;
		val open = predicate.regionFor.keyword(grammarAccess.predicateAccess.leftCurlyBracketKeyword_3_1_0.value) // "{"
		val close = predicate.regionFor.keyword(grammarAccess.predicateAccess.rightCurlyBracketKeyword_3_1_2.value) // "}"
		interior(open, close)[indent]

	}

	def dispatch void format(TypedParamList typedParamList, extension IFormattableDocument document) {

		for (TypedParam typeParam : typedParamList.params) {
			typeParam.format;
		}
	}

	def dispatch void format(TypedParam typedParam, extension IFormattableDocument document) {
		typedParam.type.format;
	}

	def dispatch void format(Pattern pattern, extension IFormattableDocument document) {
		pattern.regionFor.keyword(grammarAccess.patternAccess.patternKeyword_0.value).prepend[setNewLines(0, 0, 2)].
			append[oneSpace] // "pattern"
		pattern.params.format;
		for (VarDecl varDecl : pattern.varDeclList) {
			varDecl.format;
		}
		for (TemporalExpression initial : pattern.initial) {
			initial.format;
			initial.regionFor.keyword('|').append[setNewLines(0, 0, 1)].prepend[oneSpace]

		}
		for (TemporalExpression safety : pattern.safety) {
			safety.format;
			safety.regionFor.keyword('|').append[setNewLines(0, 0, 1)].prepend[oneSpace]

		}
		for (TemporalExpression justice : pattern.justice) {
			justice.format;
			justice.regionFor.keyword('|').append[setNewLines(0, 0, 1)].prepend[oneSpace]

		}
		pattern.regionFor.keyword(grammarAccess.patternAccess.getGKeyword_3_2_0_1_0_0.value).append[oneSpace].prepend [
			setNewLines(1, 1, 1)
		] // 'G'
		pattern.regionFor.keyword(grammarAccess.patternAccess.GFKeyword_3_2_0_3_0_0.value).append[oneSpace].prepend [
			setNewLines(1, 1, 1)
		] // 'GF'
		val open = pattern.regionFor.keyword(grammarAccess.patternAccess.getLeftCurlyBracketKeyword_3_0().value) // "{"
		val close = pattern.regionFor.keyword(grammarAccess.patternAccess.getRightCurlyBracketKeyword_3_3().value) // "}"
		interior(open, close)[indent]

	}

	def dispatch void format(PatternParamList patternParamList, extension IFormattableDocument document) {
		for (PatternParam patternParam : patternParamList.params) {
			patternParam.format;
			patternParam.regionFor.keyword("=").surround[oneSpace]

		}
	}

	def dispatch void format(PatternParam patternParam, extension IFormattableDocument document) {
	}

	def dispatch void format(WeightDef weightDef, extension IFormattableDocument document) {
		weightDef.definition.format;
	}

	def dispatch void format(LTLGar ltlGar, extension IFormattableDocument document) {
		if (ltlGar.getName() != null) {
			ltlGar.regionFor.keyword(grammarAccess.LTLGarAccess.guaranteeKeyword_0_0.value).prepend [
				setNewLines(0, 0, 2)
			].append[oneSpace] // "guarantee"
			ltlGar.regionFor.keyword(grammarAccess.LTLGarAccess.garKeyword_0_1.value).prepend[setNewLines(0, 0, 2)].
				append[oneSpace] // "gar"
			ltlGar.regionFor.keyword(grammarAccess.LTLGarAccess.getColonKeyword_1_2().value).prepend[noSpace].append [
				setNewLines(1, 1, 1)
			] // ':'
		} else {
			ltlGar.regionFor.keyword(grammarAccess.LTLGarAccess.guaranteeKeyword_0_0.value).prepend [
				setNewLines(0, 0, 2)
			].append[setNewLines(1, 1, 1)]
		}

		ltlGar.regionFor.keyword(grammarAccess.LTLGarAccess.safetyGKeyword_2_0_0_2_0_0.value).append[oneSpace].prepend [
			setSpace("  ")
		] // "G"
		ltlGar.regionFor.keyword(grammarAccess.LTLGarAccess.justiceGFKeyword_2_0_0_4_0_0.value).append[oneSpace].prepend [
			setSpace("  ")
		] // "GF"
		format(ltlGar.temporalExpr, document);

	}

	def dispatch void format(LTLAsm ltlAsm, extension IFormattableDocument document) {
		if (ltlAsm.getName() != null) {
			ltlAsm.regionFor.keyword(grammarAccess.LTLAsmAccess.assumptionKeyword_0_0.value).prepend [
				setNewLines(0, 0, 2)
			].append[oneSpace] // "assumption"
			ltlAsm.regionFor.keyword(grammarAccess.LTLAsmAccess.asmKeyword_0_1).prepend[setNewLines(0, 0, 2)].append [
				oneSpace
			] // "asm"
			ltlAsm.regionFor.keyword(grammarAccess.LTLAsmAccess.getColonKeyword_1_2().value).prepend[noSpace].append [
				setNewLines(1, 1, 1)
			] // ':'
		} else {
			ltlAsm.regionFor.keyword(grammarAccess.LTLAsmAccess.assumptionKeyword_0_0.value).prepend [
				setNewLines(0, 0, 2)
			].append[setNewLines(1, 1, 1)] // "assumption"
		}
		ltlAsm.regionFor.keyword(grammarAccess.LTLAsmAccess.safetyGKeyword_2_0_0_2_0_0.value).append[oneSpace] // "G"
		ltlAsm.regionFor.keyword(grammarAccess.LTLAsmAccess.justiceGFKeyword_2_0_0_4_0_0.value).append[oneSpace] // "GF"
		ltlAsm.temporalExpr.format;
	}
}
