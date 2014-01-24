package com.summithillsoftware.ultimate;

public class CorruptObject extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CorruptObject() {
		super();
	}

	public CorruptObject(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public CorruptObject(String detailMessage) {
		super(detailMessage);
	}

	public CorruptObject(Throwable throwable) {
		super(throwable);
	}

}
