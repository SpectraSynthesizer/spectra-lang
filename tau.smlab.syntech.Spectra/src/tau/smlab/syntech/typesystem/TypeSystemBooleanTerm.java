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

package tau.smlab.syntech.typesystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.xtext.EcoreUtil2;

import tau.smlab.syntech.spectra.BooleanTerm;
import tau.smlab.syntech.spectra.SpectraPackage;
import tau.smlab.syntech.spectra.TemporalBinaryExpr;
import tau.smlab.syntech.spectra.TemporalExpression;
import tau.smlab.syntech.spectra.TemporalPrimaryExpr;
import tau.smlab.syntech.spectra.TemporalUnaryExpr;
import tau.smlab.syntech.spectra.ValueInRange;
import tau.smlab.syntech.spectra.VarDecl;
import tau.smlab.syntech.spectra.VarType;

/**
 * 
 * @author Or Pistiner
 *
 */
public class TypeSystemBooleanTerm {

	private static final String CAPITAL_TRUE = "TRUE";
	private static final String CAPITAL_FALSE = "FALSE";

	private TypeSystemBooleanTerm() {}

	public static TypeCheckIssue typeCheck(BooleanTerm predicate) {
		BooleanAndString leftBooAndString = null;
		String issueMsg = null;
		if(predicate.getRelExpr() != null) {
			TemporalExpression predicateExpression = predicate.getRelExpr();

			//First, check that the predicate is Boolean
			leftBooAndString = TypeSystemUtils.isBooleanExpression(predicateExpression);
			if(!leftBooAndString.getBoolean()) {
				issueMsg = IssueMessages.REGEXP_PREDICATES_MUST_BE_BOOLEAN;
			}
			//Second, check that the predicate is not have any Past-LTL (Boolean) expressions
			else if(predicateExpression instanceof TemporalBinaryExpr || predicateExpression instanceof TemporalUnaryExpr) {
				issueMsg = IssueMessages.REGEXP_CANT_HAVE_PASTLTL_EXPR;
			}
			else {
				List<TemporalUnaryExpr> pastLTLUnaryExprsList = EcoreUtil2.getAllContentsOfType(predicateExpression, TemporalUnaryExpr.class);
				if((pastLTLUnaryExprsList != null && !pastLTLUnaryExprsList.isEmpty())) {
					issueMsg = IssueMessages.REGEXP_CANT_HAVE_PASTLTL_EXPR;
				}
				else {
					List<TemporalBinaryExpr> pastLTLBinaryExprsList = EcoreUtil2.getAllContentsOfType(predicateExpression, TemporalBinaryExpr.class);
					if((pastLTLBinaryExprsList != null && !pastLTLBinaryExprsList.isEmpty())) {
						issueMsg = IssueMessages.REGEXP_CANT_HAVE_PASTLTL_EXPR;
					}
				}
			}

			if(issueMsg != null) {
				return new TypeCheckError(SpectraPackage.Literals.BOOLEAN_TERM__REL_EXPR, issueMsg);
			}

			//Third, check that the predicate does not have any PRIMED variables
			List<TemporalPrimaryExpr> temporalPrimaryExprsList;
			if (predicateExpression instanceof TemporalPrimaryExpr) {
				temporalPrimaryExprsList = new ArrayList<>();
				temporalPrimaryExprsList.add((TemporalPrimaryExpr) predicateExpression);
			}
			else {
				temporalPrimaryExprsList = EcoreUtil2.getAllContentsOfType(predicateExpression, TemporalPrimaryExpr.class);
			}
			List<VarDecl> primedVarDecls = TypeSystemUtils.getPrimedVarDecls(temporalPrimaryExprsList);
			if (primedVarDecls != null && !primedVarDecls.isEmpty()) {
				return new TypeCheckError(SpectraPackage.Literals.BOOLEAN_TERM__REL_EXPR,
						IssueMessages.REGEXP_CANT_HAVE_PRIMED_VARS);
			}  		
		}
		else if(predicate.getPointer() != null && predicate.getValues() != null) {
			VarType varType = TypeSystemUtils.getVarType(predicate.getPointer().getType());
			if (varType.getDimensions() != null && !varType.getDimensions().isEmpty()) {
				return new TypeCheckError(SpectraPackage.Literals.BOOLEAN_TERM__POINTER,
						IssueMessages.REGEXP_IN_SET_PREDICATES_CANT_HAVE_ARRAYS);
			}
			if(varType.getSubr() != null) { //Integer typed variable
				int from = TypeSystemUtils.sizeDefineToInt(varType.getSubr().getFrom());
				int to = TypeSystemUtils.sizeDefineToInt(varType.getSubr().getTo());
				if(from < 0 || from > to) {
					//Should not happen
					return null;
				}
				Set<Integer> seenIntVals = new HashSet<>();
				int specifiedFrom = -1, specifiedTo = -1;
				for(ValueInRange value : predicate.getValues()) {
					if(value.isMulti()) {
						specifiedFrom = value.getFrom();
						specifiedTo = value.getTo();
						if(specifiedTo < specifiedFrom) {
							return new TypeCheckError(SpectraPackage.Literals.BOOLEAN_TERM__VALUES,
									IssueMessages.REGEXP_INVALID_VAL_RANGE + ": " + specifiedFrom + "-" + specifiedTo);
						}
						if(specifiedTo > to || specifiedFrom < from) {
							return new TypeCheckError(SpectraPackage.Literals.BOOLEAN_TERM__VALUES,
									IssueMessages.REGEXP_VAL_NOT_IN_VAR_DOM);
						}
					}
					else if(value.getConst() != null || value.getBooleanValue() != null || value.getInt() < from ||
							value.getInt() > to) {
						//A value NOT in the domain of the integer variable
						return new TypeCheckError(SpectraPackage.Literals.BOOLEAN_TERM__VALUES,
								IssueMessages.REGEXP_VAL_NOT_IN_VAR_DOM);
					}

					if(value.isMulti()) {
						for(int intValue = specifiedFrom; intValue <= specifiedTo; intValue++) {
							if(seenIntVals.contains(intValue)) {
								return new TypeCheckError(SpectraPackage.Literals.BOOLEAN_TERM__VALUES,
										IssueMessages.REGEXP_VAL_AT_MOST_ONCE + ": " + intValue + " appears more than once");
							}
							seenIntVals.add(intValue);
						}
					}
					else {
						if(seenIntVals.contains(value.getInt())) {
							return new TypeCheckError(SpectraPackage.Literals.BOOLEAN_TERM__VALUES,
									IssueMessages.REGEXP_VAL_AT_MOST_ONCE + ": " + value.getInt() + " appears more than once");
						}
						seenIntVals.add(value.getInt());
					}
				}
				if(seenIntVals.size() == (to-from+1)) {
					return handleTrivialRangePredicate(predicate);
				}
			}
			Set<String> seenVals;
			if(varType.getConst() != null && !varType.getConst().isEmpty()) { //Enum typed variable
				List<String> domainValues = TypeSystemUtils.getPermittedTypeConstants(varType);
				seenVals = new HashSet<>();
				for(ValueInRange value : predicate.getValues()) {
					if(value.getConst() == null || !domainValues.contains(value.getConst().getName())) {
						//A value NOT in the domain of the enum variable
						return new TypeCheckError(SpectraPackage.Literals.BOOLEAN_TERM__VALUES,
								IssueMessages.REGEXP_VAL_NOT_IN_VAR_DOM);
					}
					if(seenVals.contains(value.getConst().getName())) {
						return new TypeCheckError(SpectraPackage.Literals.BOOLEAN_TERM__VALUES,
								IssueMessages.REGEXP_VAL_AT_MOST_ONCE + ": " + value.getConst().getName() + " appears more than once");
					}
					seenVals.add(value.getConst().getName());
				}
				if(seenVals.size() == domainValues.size()) {
					return handleTrivialRangePredicate(predicate);
				}
			}
			else if(varType.getName() != null) { //Boolean typed variable
				seenVals = new HashSet<>();
				for(ValueInRange value : predicate.getValues()) {
					if(value.getBooleanValue() == null || (!CAPITAL_TRUE.equalsIgnoreCase(value.getBooleanValue()) &&
							!CAPITAL_FALSE.equalsIgnoreCase(value.getBooleanValue()))) {
						//A value that is NOT a constant Boolean value
						return new TypeCheckError(SpectraPackage.Literals.BOOLEAN_TERM__VALUES,
								IssueMessages.REGEXP_VAL_NOT_IN_VAR_DOM);
					}
					if(seenVals.contains(value.getBooleanValue())) {
						return new TypeCheckError(SpectraPackage.Literals.BOOLEAN_TERM__VALUES,
								IssueMessages.REGEXP_VAL_AT_MOST_ONCE + ": " + value.getBooleanValue() + " appears more than once");
					}
					seenVals.add(value.getBooleanValue());
				}
				if(seenVals.size() == 2) {
					return handleTrivialRangePredicate(predicate);
				}
			}
		}
		return null;
	}

	private static TypeCheckIssue handleTrivialRangePredicate(BooleanTerm predicate) {
		if(predicate.isNot()) {
			return new TypeCheckError(SpectraPackage.Literals.BOOLEAN_TERM__VALUES,
					IssueMessages.REGEXP_VAL_SET_EQUALS_DOM + ". The domain of " + predicate.getPointer().getName() + " minus the value set is an EMPTY SET (trivially evaluates to false)");
		}
		return new TypeCheckWarning(SpectraPackage.Literals.BOOLEAN_TERM__VALUES,
				IssueMessages.REGEXP_VAL_SET_EQUALS_DOM + ". This set equals the domain of " + predicate.getPointer().getName() + " (trivially evaluates to true)");
	}

}
