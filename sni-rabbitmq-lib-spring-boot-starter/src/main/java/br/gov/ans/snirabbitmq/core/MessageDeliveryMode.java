package br.gov.ans.snirabbitmq.core;

public enum MessageDeliveryMode {
	/**
	 * Non persistent.
	 */
	NON_PERSISTENT(1),

	/**
	 * Persistent.
	 */
	PERSISTENT(2);
	int code;
	MessageDeliveryMode(int code){
		this.code = code;
	}
	public static int toInt(MessageDeliveryMode mode) {
		return mode.code;
	}

	public static MessageDeliveryMode fromInt(int modeAsNumber) {
		switch (modeAsNumber) {
		case 1:
			return NON_PERSISTENT;
		case 2:
			return PERSISTENT;
		default:
			return null;
		}
	}

}
