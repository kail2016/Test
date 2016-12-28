package com.netease.seckill.exception;

/**
 * Created by Jo on 10/18/16.
 * close seckill exception
 * 秒杀关闭异常
 */
public class SeckillCloseException extends SeckillException{
	public SeckillCloseException(String message) {
		super(message);
	}

	public SeckillCloseException(String message, Throwable cause) {
		super(message, cause);
	}
}
