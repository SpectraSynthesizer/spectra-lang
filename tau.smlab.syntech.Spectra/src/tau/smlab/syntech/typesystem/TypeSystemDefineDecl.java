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
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;

import tau.smlab.syntech.spectra.DefineDecl;
import tau.smlab.syntech.spectra.Pattern;
import tau.smlab.syntech.spectra.SpectraPackage;
import tau.smlab.syntech.spectra.TemporalPrimaryExpr;

public class TypeSystemDefineDecl {

  public static TypeCheckIssue typeCheck(DefineDecl defineDecl) {
    
    if (isContainsCycles(defineDecl, new ArrayList<String>()))
    {
      return new TypeCheckError(SpectraPackage.Literals.DEFINE_DECL.getEIDAttribute(), IssueMessages.DEFINEDECL_CONTAINS_CYCLES);
    }
    
    if (defineDecl.getSimpleExpr() instanceof TemporalPrimaryExpr) {
      TemporalPrimaryExpr temporalPrimaryExpr = (TemporalPrimaryExpr) defineDecl.getSimpleExpr();
      if (temporalPrimaryExpr.getPredPatt() != null && temporalPrimaryExpr.getPredPatt() instanceof Pattern) {
        return new TypeCheckError(SpectraPackage.Literals.DEFINE_DECL__SIMPLE_EXPR,
            IssueMessages.CANT_CALL_A_PATTERN_FROM_HERE);
      }
    }

    // check if defineDecl isn't used
    // NOTE: Assuming no other files can acces defineDecl
    Collection<Setting> references = EcoreUtil.UsageCrossReferencer.find(defineDecl, defineDecl.eResource());
    if (references == null || references.size() == 0) {
      return new TypeCheckWarning(SpectraPackage.Literals.DEFINE_DECL.getEIDAttribute(),
          IssueMessages.DEFINEDECL_NOT_USED + ": " + defineDecl.getName());
    }
    return null;

  }

  private static boolean isContainsCycles(DefineDecl defineDecl, ArrayList<String> alreadySeenDefines) {
    if (alreadySeenDefines.contains(defineDecl.getName())) {
      return true;
    }
    alreadySeenDefines.add(defineDecl.getName()); // add the current one
    
    // case1: this define is a simple expression
    if (defineDecl.getSimpleExpr() instanceof TemporalPrimaryExpr) {
      TemporalPrimaryExpr temporalPrimaryExpr = (TemporalPrimaryExpr) defineDecl.getSimpleExpr();
      // check only the case where we directly reference a define
      if (temporalPrimaryExpr.getPointer() != null && temporalPrimaryExpr.getPointer() instanceof DefineDecl) {
        DefineDecl nestedDefineDecl = (DefineDecl) temporalPrimaryExpr.getPointer();
        return isContainsCycles(nestedDefineDecl, alreadySeenDefines);
      }
    }
    
    // case2: check all subexpressions (inefficient)? Hopefully only the topmost. 
    List<TemporalPrimaryExpr> temporalsPEnestedInExpression = EcoreUtil2
        .getAllContentsOfType(defineDecl.getSimpleExpr(), TemporalPrimaryExpr.class);
    if (temporalsPEnestedInExpression != null && temporalsPEnestedInExpression.size() > 0) {
      for (TemporalPrimaryExpr tpe : temporalsPEnestedInExpression) {
        // for every define continue cycle chek
        if (tpe.getPointer() != null && tpe.getPointer() instanceof DefineDecl) {
          DefineDecl nestedDefineDecl = (DefineDecl) tpe.getPointer();
          // here we need to clone to descend current branch only
          ArrayList<String> copy = new ArrayList<>();
          copy.addAll(alreadySeenDefines);
          if (isContainsCycles(nestedDefineDecl, copy)) {
            return true;
          }
        }
      }
    }

    return false;
  }
}