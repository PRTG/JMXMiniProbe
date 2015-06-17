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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.digester3.Digester;

public class Profiles {

	List<Profile> profiles = new Vector<Profile>();

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}
	public void addProfile(Profile profile) {
		this.profiles.add(profile);
	}
	public void addProfiles(Profiles profiles) {
		for(Profile curr:profiles.getProfiles()){
			this.profiles.add(curr);
		}
	}
	public static Digester describe4Digester(Digester digester, String base) throws Exception{
		String mybase = base +"profiles";
        digester.addObjectCreate( mybase , Profiles.class );
		mybase += "/profile";
        digester.addObjectCreate( mybase , Profile.class );
        Profile.describe4Digester(digester, mybase);
        digester.addSetNext( mybase, "addProfile" );
		return digester;
	}

	// -------------------------------------------
	public StringBuilder toXML(StringBuilder target, String prefix, String suffix){
		
		target.append(prefix);
		target.append("<profiles xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"Profile.xsd\">");
		target.append(suffix);
		String tmp = prefix + "\t";
		for(Profile curr: getProfiles()){
			target = curr.toXML(target, tmp, suffix);
		}
		target.append(prefix);
		target.append("</profiles>");
		target.append(suffix);
		return target;
	}
	// -------------------------------------------
	public String toXML(String prefix, String suffix){
		StringBuilder retVal = toXML(new StringBuilder(), prefix, suffix);
		return retVal.toString();
	}
	// -----------------------------------------------------------------------------------
	  public void sort(Comparator<Profile> comp){
	        Collections.sort(getProfiles(), comp);
		  
	  }
		// -----------------------------------------------------------------------------------
	  public void sort(){
	        sort(new Comparator<Profile>() {
	            @Override
	            public int compare(Profile p1, Profile p2) {
	            	int retVal = -1;
	            	String n1 = p1.getName();
	            	String n2 = p2.getName();
	            	if(n1 != null && n2 != null){
	  	            	retVal = n1.compareTo(n2);
	            	} else {
	            		if (n1 == null){
	            			retVal = 1;
	            		}
	            	}
//	                return (p2.intValue() > p1.intValue()) ? 1 : -1;
	            	return retVal;
	            }
	        });
		  
	  }
	
}
