package com.netease.seckill.dto;

/**
 * 
 * 数据传输层，关注web和service之间的数据传递；与业务不相关；
 * 而Entity则关注DAO层
 * 
 */

/* Created by Jo on 10/18/16.
 * expose seckill url DTO
 * 
 * 暴露秒杀地址
 */
public class Exposer {

	//start seckill or not 是否可以开始暴露
	private boolean exposed;

	//encryption method，一种加密措施
	private String md5;

	//id
	private long seckillId;

	//system current time 系统当前时间（毫秒）
	private long now;
	//start time 秒杀的开启时间
	private long startTime;
	//end time  秒杀结束时间
	private long endTime;

	//不同的构造方法，是为了方便对象的初始化；
	
	public Exposer(boolean exposed, String md5, long seckillId) {
		this.exposed = exposed;
		this.md5 = md5;
		this.seckillId = seckillId;
	}

	public Exposer(boolean exposed, long seckillId,long now, long startTime, long endTime) {
		this.exposed = exposed;
		this.seckillId = seckillId;
		this.now = now;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Exposer(boolean exposed, long seckillId) {
		this.exposed = exposed;
		this.seckillId = seckillId;
	}

	public boolean isExposed() {
		return exposed;
	}

	public void setExposed(boolean exposed) {
		this.exposed = exposed;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public long getNow() {
		return now;
	}

	public void setNow(long now) {
		this.now = now;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	@Override public String toString() {
		final StringBuffer sb = new StringBuffer("Exposer{");
		sb.append("exposed=").append(exposed);
		sb.append(", md5='").append(md5).append('\'');
		sb.append(", seckillId=").append(seckillId);
		sb.append(", now=").append(now);
		sb.append(", startTime=").append(startTime);
		sb.append(", endTime=").append(endTime);
		sb.append('}');
		return sb.toString();
	}
}
