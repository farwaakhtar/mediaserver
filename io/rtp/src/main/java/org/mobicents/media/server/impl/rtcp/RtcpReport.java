package org.mobicents.media.server.impl.rtcp;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an abstraction of an RTCP Report.
 * 
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 * 
 */
public abstract class RtcpReport extends RtcpHeader {

	/**
	 * Source that generated the report
	 */
	protected long ssrc;

	/**
	 * Reports coming from other sync sources
	 */
	protected List<RtcpReportBlock> receiverReports;
	
	protected RtcpReport() {
		this.receiverReports = new ArrayList<RtcpReportBlock>(RtcpPacket.MAX_SOURCES);
	}

	protected RtcpReport(boolean padding, long ssrc, int packetType) {
		super(padding, packetType);
		this.ssrc = ssrc;
		this.receiverReports = new ArrayList<RtcpReportBlock>(RtcpPacket.MAX_SOURCES);
	}

	/**
	 * Tells whether this reports was generated by a sender or a receiver.
	 * 
	 * @return Whether this is a Sender Report or not.
	 */
	public abstract boolean isSender();
	
	public long getSsrc() {
		return this.ssrc;
	}
	
	public RtcpReportBlock[] getReceiverReports() {
		RtcpReportBlock[] blocks = new RtcpReportBlock[this.receiverReports.size()];
		return this.receiverReports.toArray(blocks);
	}

	public void addReceiverReport(RtcpReportBlock rtcpReceptionReportItem) {
		if(this.count >= RtcpPacket.MAX_SOURCES) {
			throw new ArrayIndexOutOfBoundsException("Reached maximum number of items: "+ RtcpPacket.MAX_SOURCES);
		}
		this.receiverReports.add(rtcpReceptionReportItem);
		this.count++;
	}
}