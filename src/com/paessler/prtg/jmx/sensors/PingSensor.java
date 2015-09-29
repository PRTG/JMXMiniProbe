/*
 * Copyright (c) 2015, Paessler AG <support@paessler.com>
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

/**
 * @Author JR Andreassen
 */
package com.paessler.prtg.jmx.sensors;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.paessler.prtg.jmx.channels.Channel;
import com.paessler.prtg.jmx.channels.FloatChannel;
import com.paessler.prtg.jmx.channels.LongChannel;
import com.paessler.prtg.jmx.definitions.SensorConstants;
import com.paessler.prtg.jmx.definitions.PingSensorDefinition;
import com.paessler.prtg.jmx.responses.DataResponse;

// ----------------------
import org.icmp4j.IcmpPingUtil;
import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;

public class PingSensor extends RemoteSensor {
	
	//----------------------------------------------------------------------
	public PingSensor(){
		setDefinition(new PingSensorDefinition());
		setKind(PingSensorDefinition.KIND);
		setSensorName("PingSensor");
	}
	//----------------------------------------------------------------------
	public PingSensor(PingSensor tocopy){
		super(tocopy);
	}
	//----------------------------------------------------------------------
    @Override
    public Sensor copy(){
    	return new PingSensor(this);
    }
	//----------------------------------------------------------------------
	protected int pingCount = 5;
	public int getPingCount() {
		return pingCount;
	}
	public void setPingCount(int pingCount) {
		this.pingCount = pingCount;
	}

	//----------------------------------------------------------------------
	protected List<IcmpPingResponse> doPings(){
		List<IcmpPingResponse> retVal = null;
//	    try {

		      final IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest ();
		      request.setHost (getHost());
		      request.setTimeout(getTimeout());
		      retVal = new ArrayList<IcmpPingResponse>(getPingCount());
		      for (int count = 1; count <= getPingCount(); count ++) {
		        // delegate
		    	retVal.add(IcmpPingUtil.executePingRequest (request));
		        Thread.yield();
		      }
//	    }
//	    catch (final Throwable t){
	      // log
//	    if(get)
//	      t.printStackTrace ();
//	    }
		return retVal;
	}

	//----------------------------------------------------------------------
	//----------------------------------------------------------------------
	public class PingStats {
		public long min = Long.MAX_VALUE;
		public float avg = 0.0f;
		public long max = Long.MIN_VALUE;
		public float mDev = 0.0f;
		public long   loss = 0;

		private double sumSq = 0.0;
		private int    cnt = 0;

		
		protected void update(IcmpPingResponse val){
			if(val.getSuccessFlag()){
				int tmp =val.getRtt(); 
				min = (min < tmp ? min : tmp);
				avg += tmp;
				max = (max > tmp ? max : tmp);
				sumSq += tmp*tmp;
			}
			else
			{++loss;}
			++cnt;
		}
		//-----------------------------------------
		protected void calcFinal(){
			// http://serverfault.com/questions/333116/what-does-mdev-mean-in-ping8
			double tmp = avg / (cnt+loss);
			mDev = (float) Math.sqrt( (sumSq / (cnt+loss)) - (tmp * tmp)); 
			sumSq += 0.0f;
			avg = -1;
			if((cnt - loss) > 0){
				avg /= cnt - loss;
			}
			loss = 100*(loss/cnt);
		}
	}
	//----------------------------------------------------------------------
	//----------------------------------------------------------------------
	protected PingStats calcStats(List<IcmpPingResponse> pingResponses){
		PingStats retVal = new PingStats();
		for(IcmpPingResponse curr : pingResponses){
			retVal.update(curr);
		}
		retVal.calcFinal();
		return retVal;
	}
	//----------------------------------------------------------------------
	protected DataResponse calcStats(DataResponse response, List<IcmpPingResponse> pingResponses){
		PingStats stats = calcStats(pingResponses);
		int errcnt = 0;
		LongChannel lchannel = new LongChannel("Ping Time Min", Channel.UNIT_STR_TRESPONSE, stats.min);
		response.addChannel(lchannel); 
		if(stats.max < stats.min)	{lchannel.setWarning(1); errcnt++;}
		FloatChannel fchannel = new FloatChannel("Ping Time Avg", Channel.UNIT_STR_TRESPONSE, stats.avg);
//		if(stats.avg < 0)			{lchannel.setWarning(1); errcnt++;}
		response.addChannel(fchannel); 
		lchannel = new LongChannel("Ping Time Max",		Channel.UNIT_STR_TRESPONSE, stats.max);
		if(stats.max < stats.min)	{lchannel.setWarning(1); errcnt++;}
		response.addChannel(lchannel); 
		fchannel = new FloatChannel("Ping Time MDEV",	Channel.UNIT_STR_TRESPONSE, stats.mDev);
		response.addChannel(fchannel); 
		lchannel  = new LongChannel("Packet Loss", 		Channel.UNIT_STR_PERCENT, stats.loss);
		response.addChannel(lchannel);

		if(stats.cnt == stats.loss){
			lchannel.setWarning(1);
			if(errcnt > 1)	{response = getErrorResponse("Error", -1, "100 % Packet Loss");}
			else 			{response.setMessage("100 % Packet Loss");}
		}
		return response;
	}
	
//	{'sensorid': xxx, 'message': 'OK', 'channel': [
//	    {'kind': 'TimeResponse', 'name': 'Ping Time Min', 'value': 5.101, 'mode': 'float'}, 
//	    {'kind': 'TimeResponse', 'name': 'Ping Time Avg', 'value': 5.139, 'mode': 'float'}, 
//	    {'kind': 'TimeResponse', 'name': 'Ping Time Max', 'value': 5.193, 'mode': 'float'}, 
//	    {'kind': 'TimeResponse', 'name': 'Ping Time MDEV', 'value': 0.037, 'mode': 'float'}, 
//	    {'kind': 'Percent', 'name': 'Packet Loss', 'value': 0, 'mode': 'integer'}
	//----------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * @see com.paessler.prtg.jmx.sensors.Sensor#go()
	 */
	@Override
	public DataResponse go() {
        DataResponse response = new DataResponse(getSensorid(), "Ping");
		// TODO Auto-generated method stub
	    // handle exceptions
		try {
			List<IcmpPingResponse> pingResponses = doPings();
			response = calcStats(response, pingResponses);
		} catch (Exception e) {
			response = getErrorResponse("Exception", -1, e.getLocalizedMessage());
		}
		return response;
	}


	//----------------------------------------------------------------------
	@Override
	public void loadFromJson(JsonObject json)  throws Exception {
		// Delegate
		super.loadFromJson(json);
		//  Local parameters
        setPingCount(getJsonElementInt(json, SensorConstants.COUNT, getPingCount()));
	}
	//----------------------------------------------------------------------

}
