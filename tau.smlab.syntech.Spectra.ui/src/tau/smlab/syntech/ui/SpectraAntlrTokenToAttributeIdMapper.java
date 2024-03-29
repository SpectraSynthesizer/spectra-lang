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

package tau.smlab.syntech.ui;

import org.eclipse.xtext.ide.editor.syntaxcoloring.DefaultAntlrTokenToAttributeIdMapper;

public class SpectraAntlrTokenToAttributeIdMapper extends DefaultAntlrTokenToAttributeIdMapper {
	@Override
	protected String calculateId(String tokenName, int tokenType) {
//		if ("RULE_HASH_COMMENT".equals(tokenName)) {
//			return HighlightingStyles.COMMENT_ID;
//		}
//		if ("RULE_ID".equals(tokenName)) {
//
//			return HighlightingStyles.COMMENT_ID;
//		}
		if ("'assumption'".equals(tokenName) || ("'asm'".equals(tokenName)) || ("'input'".equals(tokenName)) || ("'in'".equals(tokenName)) || ("'envvar'".equals(tokenName)) || ("'env'".equals(tokenName)))
		{
			return SpectraHighlightingConfiguration.ASSUMPTION_ID;
		}
		if ("'guarantee'".equals(tokenName) || ("'gar'".equals(tokenName)) || ("'output'".equals(tokenName)) || ("'out'".equals(tokenName)) || ("'sysvar'".equals(tokenName)) || ("'sys'".equals(tokenName)))
		{
			return SpectraHighlightingConfiguration.GUARANTEE_ID;
		}
		if ("'weight'".equals(tokenName))
		{
			return SpectraHighlightingConfiguration.WEIGHT_ID;
		}
		if ("'regexp'".equals(tokenName))
		{
			return SpectraHighlightingConfiguration.REGEXP_ID;
		}
		if ("'regtest'".equals(tokenName))
		{
			return SpectraHighlightingConfiguration.REGEXPTEST_ID;
		}
		return super.calculateId(tokenName, tokenType);
		
		
	}
}
