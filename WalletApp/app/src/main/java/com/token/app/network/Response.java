package com.token.app.network;

/**
 * Created by suresh.kumar on 30/06/15.
 */
public class Response {
	private String error, status;

	public Response(String error, String status) {
		this.error = error;
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public String getStatus() {
		return status;
	}
}
