package org.mobicents.media.io.ice.harvest;

import java.net.InetAddress;
import java.nio.channels.Selector;
import java.util.List;

import org.apache.log4j.Logger;
import org.mobicents.media.io.ice.CandidateType;
import org.mobicents.media.io.ice.FoundationsRegistry;
import org.mobicents.media.io.ice.HostCandidate;
import org.mobicents.media.io.ice.IceComponent;
import org.mobicents.media.io.ice.IceMediaStream;
import org.mobicents.media.io.ice.LocalCandidateWrapper;
import org.mobicents.media.io.ice.ServerReflexiveCandidate;
import org.mobicents.media.server.io.network.PortManager;

/**
 * Gathers SRFLX candidates for the public address on which Media Server is installed.
 * 
 * @author Henrique Rosa (henrique.rosa@telestax.com)
 *
 */
public class ExternalCandidateHarvester implements CandidateHarvester {
	
	Logger logger = Logger.getLogger(ExternalCandidateHarvester.class);

	private final FoundationsRegistry foundations;

	private final InetAddress externalAddress;
	
	public ExternalCandidateHarvester(final FoundationsRegistry foundations, final InetAddress externalAddress) {
		super();
		this.foundations = foundations;
		this.externalAddress = externalAddress;
	}
	
	public void harvest(PortManager portManager, IceMediaStream mediaStream, Selector selector) throws HarvestException {
		harvest(mediaStream.getRtpComponent());
		if(mediaStream.supportsRtcp()) {
			harvest(mediaStream.getRtcpComponent());
		}
	}
	
	private void harvest(IceComponent component) {
		// Gather SRFLX candidate for each host candidate of the component
		List<LocalCandidateWrapper> rtpCandidates = component.getLocalCandidates();

		for (LocalCandidateWrapper candidateWrapper : rtpCandidates) {
			// Create one reflexive candidate for each host candidate
			if(candidateWrapper.getCandidate() instanceof HostCandidate) {
				HostCandidate hostCandidate = (HostCandidate) candidateWrapper.getCandidate();
				ServerReflexiveCandidate srflxCandidate = new ServerReflexiveCandidate(component, externalAddress, hostCandidate.getPort(), hostCandidate);
				this.foundations.assignFoundation(srflxCandidate);
				component.addLocalCandidate(new LocalCandidateWrapper(srflxCandidate, candidateWrapper.getChannel()));
			}
		}
	}

	public CandidateType getCandidateType() {
		return CandidateType.SRFLX;
	}

}