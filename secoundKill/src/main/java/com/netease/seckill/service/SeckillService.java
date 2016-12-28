package com.netease.seckill.service;

import com.netease.seckill.dto.Exposer;
import com.netease.seckill.dto.SeckillExcution;
import com.netease.seckill.entity.Seckill;
import com.netease.seckill.exception.RepeatKillException;
import com.netease.seckill.exception.SeckillCloseException;
import com.netease.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在“使用者”角度设计接口
 * 三个方面：方法定义粒度，参数（简练直接），返回类型（return类型要友好（不能用map之类） / 异常）
 * 
 * Created by Jo on 10/18/16.
 */
public interface SeckillService {

	/**
	 * get all seckill
	 * 查询所有的秒杀记录
	 * @return
	 */
	List<Seckill> getSeckillList();

	/**
	 * 查询单个秒杀记录
	 * get seckill by id
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);

	/**
	 * 秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
	 * 防止：用于提前知道秒杀地址，写秒杀插件
	 * expose seckill url when seckill start,else expose system time and kill time
	 * @param seckillId
	 */
	Exposer exposeSeckillUrl(long seckillId);

	/**
	 * 执行秒杀操作
	 * execute seckill
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 */
	SeckillExcution executeSeckill(long seckillId,long userPhone,String md5) throws SeckillException,RepeatKillException,SeckillCloseException;

	SeckillExcution executeSeckillProcedure(long seckillId,long userPhone,String md5) throws SeckillException,RepeatKillException,SeckillCloseException;
}
