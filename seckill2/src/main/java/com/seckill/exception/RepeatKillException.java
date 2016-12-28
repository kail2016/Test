package com.seckill.exception;

/**
 * Created by Jo on 10/18/16.
 * repeat seckill exception(runnable exception)
 * 重复秒杀异常（运行期异常），
 */
public class RepeatKillException extends SeckillException{
	public RepeatKillException(String message) {
		super(message);
	}

	public RepeatKillException(String message, Throwable cause) {
		super(message, cause);
	}
}
