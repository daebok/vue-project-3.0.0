package com.rd.ifaes.common.rabbit.model;

public class MqLogModel extends MqBaseModel {
	
	private String logTime;
	

	public String getLogTime() {
		return logTime;
	}

	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}



	@Override
	public String toString() {
		return "MqLogModel [logTime=" + logTime + "]";
	}
	

}
