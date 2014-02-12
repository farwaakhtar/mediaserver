package org.mobicents.media.core.ice.network.stun.messages;

/**
 * Represents a STUN Request message.
 */
public class StunRequest extends StunMessage {

	public StunRequest() {
		super();
	}

	public void setMessageType(char requestType)
			throws IllegalArgumentException {
		if (!isRequestType(requestType)) {
			throw new IllegalArgumentException((int) (requestType)
					+ " - is not a valid request type.");
		}
		super.setMessageType(requestType);
	}
}
