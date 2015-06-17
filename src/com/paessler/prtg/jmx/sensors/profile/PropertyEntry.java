 /*
 * Copyright (c) 2014-2015, Paessler AG <support@paessler.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.paessler.prtg.jmx.sensors.profile;

import org.apache.commons.digester3.Digester;

public class PropertyEntry{
	public String name;
	public Object value;

	// -------------------------------------------
	public String getName() {return name;}
	public void setName(String name) {this.name = name;	}
	// -------------------------------------------
	public Object getValue() {return value;	}
	public void setValue(Object value) {this.value = value;	}
	// -------------------------------------------

	public static Digester describe4Digester(Digester digester, String base) throws Exception{
			String mybase = base;
	        digester.addBeanPropertySetter( mybase+"/name", "name" );
	        digester.addBeanPropertySetter( mybase+"/value", "value" );
	        return digester;
	}
	// -------------------------------------------
	public StringBuilder toXML(StringBuilder target, String prefix, String suffix){
		
		target.append(prefix);
		target.append("<property>");
		target.append(suffix);
		String tmp = prefix + "\t";
		// -------------------------------------
		ProfileFactory.addXMLElement(target, "name", getName(), tmp, suffix);
		ProfileFactory.addXMLElement(target, "value", getValue(), tmp, suffix);
		// -------------------------------------
		target.append(tmp);
		target.append("</property>");
		target.append(suffix);
		return target;
	}
	// -------------------------------------------
	public String toXML(String prefix, String suffix){
		StringBuilder retVal = toXML(new StringBuilder(), prefix, suffix);
		return retVal.toString();
	}
}
