package vkapi;

public class VKException extends Exception {
	public static final int LOGIN_PW = 1;
	public static final int PROTOCOL = 2;

	private int code;
	private String message;

	public VKException(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
