package com.netease.seckill.dao;

import com.netease.seckill.entity.SuccessKill;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Jo on 10/17/16.
 */
public interface SuccessKillDao {

	/**
	 * 插入购买明细，可过滤重复
	 * insert success killed detail,can filter repeat
	 * @param seckillId
	 * @param userPhone
	 * @return insert lines 插入的行数；返回 0- 代表插入失败
	 */
	int insertSuccessKill(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);

	/**
	 * 根据Id查询successKill并携带秒杀产品对象实体
	 * query successKill with seckill by Id
	 * @param seckillId
	 * @return
	 */
	SuccessKill queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);



}
