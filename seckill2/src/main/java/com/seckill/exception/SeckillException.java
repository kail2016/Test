package com.seckill.exception;

/**
 * Created by Jo on 10/18/16.
 * seckill exception
 * Spring回滚事务，只接收运行期异常；
 * 
 * 秒杀相关的异常
 */
public class SeckillException extends RuntimeException{

	public SeckillException(String message) {
		super(message);
	}

	public SeckillException(String message, Throwable cause) {
		super(message, cause);
	}
}
