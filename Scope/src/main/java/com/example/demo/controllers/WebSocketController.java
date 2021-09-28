/*package com.example.demo.controllers;

import java.io.*;
import java.util.*;
import java.net.*;

import com.wowza.wms.application.*;
import com.wowza.wms.client.*;
import com.wowza.wms.logging.*;
import com.wowza.wms.server.*;
import com.wowza.wms.vhost.*;
import com.wowza.wms.http.*;
import com.wowza.util.*;
import com.wowza.wms.httpstreamer.model.*;
//import org.json.*;

public class HTTPConnectionCountsXML extends HTTProvider2Base
{
	class MyCounter
	{
		int total = 0;
	}
	private void outputConnectionInfo(StringBuffer ret, ConnectionCounter counter)
	{
		ret.append("<ConnectionsCurrent>"+counter.getCurrent()+"</ConnectionsCurrent>");
		ret.append("<ConnectionsTotal>"+counter.getTotal()+"</ConnectionsTotal>");
		ret.append("<ConnectionsTotalAccepted>"+counter.getTotalAccepted()+"</ConnectionsTotalAccepted>");
		ret.append("<ConnectionsTotalRejected>"+counter.getTotalRejected()+"</ConnectionsTotalRejected>");
	}

	private void outputIOPerformanceInfo(StringBuffer ret, IOPerformanceCounter ioPerformance)
	{
		ret.append("<MessagesInBytesRate>"+ioPerformance.getMessagesInBytesRate()+"</MessagesInBytesRate>");
		ret.append("<MessagesOutBytesRate>"+ioPerformance.getMessagesOutBytesRate()+"</MessagesOutBytesRate>");
	}

	private int toCount(Integer intObj, MyCounter counter)
	{
		int ret = intObj==null?0:intObj.intValue();
		counter.total += ret;
		return ret;
	}

	public void onHTTPRequest(IVHost inVhost, IHTTPRequest req, IHTTPResponse resp)
	{
		if (!doHTTPAuthentication(inVhost, req, resp))
			return;

		StringBuffer ret = new StringBuffer();

		String queryStr = req.getQueryString();
		Map<String, String> queryMap = HTTPUtils.splitQueryStr(queryStr);

		boolean isFlat = false;
		isFlat = this.properties.getPropertyBoolean("isFlat", isFlat);
		if (queryMap.containsKey("flat"))
			isFlat = true;

		try
		{
			List<String> vhostNames = VHostSingleton.getVHostNames();
			ret.append("<?xml version="1.0"?>
<WowzaMediaServer>");

			IServer server = Server.getInstance();

			if (!isFlat)
			{
				outputConnectionInfo(ret, server.getConnectionCounter());
				outputIOPerformanceInfo(ret, server.getIoPerformanceCounter());
			}

			Iterator<String> iter = vhostNames.iterator();
			while (iter.hasNext())
			{
				String vhostName = iter.next();
				IVHost vhost = (IVHost)VHostSingleton.getInstance(vhostName);
				if (vhost != null)
				{
					if (!isFlat)
					{
						ret.append("<VHost>");
						ret.append("<Name>"+URLEncoder.encode(vhostName, "UTF-8")+"</Name>");
						ret.append("<TimeRunning>"+vhost.getTimeRunningSeconds()+"</TimeRunning>");
						ret.append("<ConnectionsLimit>"+vhost.getConnectionLimit()+"</ConnectionsLimit>");
						outputConnectionInfo(ret, vhost.getConnectionCounter());
						outputIOPerformanceInfo(ret, vhost.getIoPerformanceCounter());
					}

					List<String> appNames = vhost.getApplicationNames();
					Iterator<String> appNameIterator = appNames.iterator();
					while (appNameIterator.hasNext())
					{
						String applicationName = appNameIterator.next();
						IApplication application = vhost.getApplication(applicationName);
						if (application == null)
							continue;

						if (!isFlat)
						{
							ret.append("<Application>");
							ret.append("<Name>"+URLEncoder.encode(applicationName, "UTF-8")+"</Name>");
							ret.append("<Status>loaded</Status>");
							ret.append("<TimeRunning>"+application.getTimeRunningSeconds()+"</TimeRunning>");

							outputConnectionInfo(ret, application.getConnectionCounter());
							outputIOPerformanceInfo(ret, application.getIoPerformanceCounter());
						}

						List<String> appInstances = application.getAppInstanceNames();
						Iterator<String> iterAppInstances = appInstances.iterator();
						while (iterAppInstances.hasNext())
						{
							String appInstanceName = iterAppInstances.next();
							IApplicationInstance appInstance = application.getAppInstance(appInstanceName);
							if (appInstance == null)
								continue;

							if (!isFlat)
							{
								ret.append("<ApplicationInstance>");
								ret.append("<Name>"+URLEncoder.encode(appInstance.getName(), "UTF-8")+"</Name>");
								ret.append("<TimeRunning>"+appInstance.getTimeRunningSeconds()+"</TimeRunning>");

								outputConnectionInfo(ret, appInstance.getConnectionCounter());
								outputIOPerformanceInfo(ret, appInstance.getIOPerformanceCounter());
							}

							Map<String, Integer> flashCounts = appInstance.getPlayStreamCountsByName();
							Map<String, Integer> smoothCounts = appInstance.getHTTPStreamerSessionCountsByName(IHTTPStreamerSession.SESSIONPROTOCOL_SMOOTHSTREAMING);
							Map<String, Integer> cupertinoCounts = appInstance.getHTTPStreamerSessionCountsByName(IHTTPStreamerSession.SESSIONPROTOCOL_CUPERTINOSTREAMING);
							Map<String, Integer> sanjoseCounts = appInstance.getHTTPStreamerSessionCountsByName(IHTTPStreamerSession.SESSIONPROTOCOL_SANJOSESTREAMING);
							Map<String, Integer> rtspCounts = appInstance.getRTPSessionCountsByName();
							Map<String, Integer> mpegdashCounts = appInstance.getHTTPStreamerSessionCountsByName(IHTTPStreamerSession.SESSIONPROTOCOL_MPEGDASHSTREAMING);

							List<String> publishStreams = appInstance.getStreams().getPublishStreamNames();

							Set<String> streamNames = new HashSet<String>();
							streamNames.addAll(publishStreams);
							streamNames.addAll(flashCounts.keySet());
							streamNames.addAll(smoothCounts.keySet());
							streamNames.addAll(cupertinoCounts.keySet());
							streamNames.addAll(sanjoseCounts.keySet());
							streamNames.addAll(rtspCounts.keySet());
							streamNames.addAll(mpegdashCounts.keySet());

							Iterator<String> siter = streamNames.iterator();
							while(siter.hasNext())
							{
								String streamName = siter.next();
								MyCounter counter = new MyCounter();

								if (isFlat)
								{
									int flashCount = toCount(flashCounts.get(streamName), counter);
									int cupertinoCount = toCount(cupertinoCounts.get(streamName), counter);
									int smoothCount = toCount(smoothCounts.get(streamName), counter);
									int sanjoseCount = toCount(sanjoseCounts.get(streamName), counter);
									int rtspCount = toCount(rtspCounts.get(streamName), counter);
									int mpegdashCount = toCount(mpegdashCounts.get(streamName), counter);

									ret.append("<Stream ");
									ret.append("vhostName=""+URLEncoder.encode(vhostName, "UTF-8")+"" ");
									ret.append("applicationName=""+URLEncoder.encode(applicationName, "UTF-8")+"" ");
									ret.append("appInstanceName=""+URLEncoder.encode(appInstanceName, "UTF-8")+"" ");
									ret.append("streamName=""+URLEncoder.encode(streamName, "UTF-8")+"" ");
									ret.append("sessionsFlash=""+flashCount+"" ");
									ret.append("sessionsCupertino=""+cupertinoCount+"" ");
									ret.append("sessionsSanJose=""+sanjoseCount+"" ");
									ret.append("sessionsSmooth=""+smoothCount+"" ");
									ret.append("sessionsRTSP=""+rtspCount+"" ");
									ret.append("sessionsMPEGDash=""+mpegdashCount+"" ");
									ret.append("sessionsTotal=""+counter.total+"" ");
									ret.append("/>");
								}
								else
								{
									ret.append("<Stream>");
									ret.append("<Name>"+URLEncoder.encode(streamName, "UTF-8")+"</Name>");
									ret.append("<SessionsFlash>"+toCount(flashCounts.get(streamName), counter)+"</SessionsFlash>");
									ret.append("<SessionsCupertino>"+toCount(cupertinoCounts.get(streamName), counter)+"</SessionsCupertino>");
									ret.append("<SessionsSanJose>"+toCount(sanjoseCounts.get(streamName), counter)+"</SessionsSanJose>");
									ret.append("<SessionsSmooth>"+toCount(smoothCounts.get(streamName), counter)+"</SessionsSmooth>");
									ret.append("<SessionsRTSP>"+toCount(rtspCounts.get(streamName), counter)+"</SessionsRTSP>");
									ret.append("<SessionsMPEGDash>" + toCount(mpegdashCounts.get(streamName), counter) + "</SessionsMPEGDash>");
									ret.append("<SessionsTotal>"+counter.total+"</SessionsTotal>");
									ret.append("</Stream>");
								}
							}

							if (!isFlat)
								ret.append("</ApplicationInstance>");
						}

						if (!isFlat)
							ret.append("</Application>");
					}

					if (!isFlat)
						ret.append("</VHost>");
				}
			}

			ret.append("</WowzaMediaServer>");
		}
		catch (Exception e)
		{
			WMSLoggerFactory.getLogger(HTTPServerVersion.class).error("HTTPServerInfoXML.onHTTPRequest: "+e.toString());
			e.printStackTrace();
		}

		try
		{
			resp.setHeader("Content-Type", "text/xml");

			OutputStream out = resp.getOutputStream();
			byte[] outBytes = ret.toString().getBytes();
			out.write(outBytes);
		}
		catch (Exception e)
		{
			WMSLoggerFactory.getLogger(HTTPServerVersion.class).error("HTTPServerInfoXML.onHTTPRequest: "+e.toString());
			e.printStackTrace();
		}

	}
}

*/