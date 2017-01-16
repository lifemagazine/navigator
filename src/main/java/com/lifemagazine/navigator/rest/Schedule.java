package com.lifemagazine.navigator.rest;

public class Schedule {

	 private Object id;
	 private String operatorName;
	 private String serviceName;
	 private int srcLocationId;
	 private String srcPortName;
	 private String srcCountryCode;
	 private int dstLocationId;
	 private String dstPortName;
	 private String dstCountryCode;
	 private String depTime;
	 private String arrTime;
	 private int duration;
	 private String status;
	 
	 public Object getId() {
			return id;
		}
		public void setId(Object id) {
			this.id = id;
		}
		public String getOperatorName() {
			return operatorName;
		}
		public void setOperatorName(String operatorName) {
			this.operatorName = operatorName;
		}
		public String getServiceName() {
			return serviceName;
		}
		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}
		public int getSrcLocationId() {
			return srcLocationId;
		}
		public void setSrcLocationId(int srcLocationId) {
			this.srcLocationId = srcLocationId;
		}
		public String getSrcPortName() {
			return srcPortName;
		}
		public void setSrcPortName(String srcPortName) {
			this.srcPortName = srcPortName;
		}
		public String getSrcCountryCode() {
			return srcCountryCode;
		}
		public void setSrcCountryCode(String srcCountryCode) {
			this.srcCountryCode = srcCountryCode;
		}
		public int getDstLocationId() {
			return dstLocationId;
		}
		public void setDstLocationId(int dstLocationId) {
			this.dstLocationId = dstLocationId;
		}
		public String getDstPortName() {
			return dstPortName;
		}
		public void setDstPortName(String dstPortName) {
			this.dstPortName = dstPortName;
		}
		public String getDstCountryCode() {
			return dstCountryCode;
		}
		public void setDstCountryCode(String dstCountryCode) {
			this.dstCountryCode = dstCountryCode;
		}
		public String getDepTime() {
			return depTime;
		}
		public void setDepTime(String depTime) {
			this.depTime = depTime;
		}
		public String getArrTime() {
			return arrTime;
		}
		public void setArrTime(String arrTime) {
			this.arrTime = arrTime;
		}
		public int getDuration() {
			return duration;
		}
		public void setDuration(int duration) {
			this.duration = duration;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
}
