package com.netease.seckill.dao;

import com.netease.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Jo on 10/17/16.
 */
public interface SeckillDao {

	/**
	 * reduce product number 减库存
	 * @param seckillId
	 * @param killTime
	 * @return if effect line number >=1 ,means update lines. if return 0 means reduce fail
	 * 如果影响函数>1,标示更新记录的行数
	 */
	int reduceNumber( @Param("seckillId") long seckillId,@Param("killTime") Date killTime);

	/**
	 * query seckill product 
	 *  根据Id查询秒杀对象
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(long seckillId);

	/**
	 * query products from seckill  根据偏移量查询秒杀商品列表
	 * @param offset
	 * @param limit
	 * @return
	 * 
	 * 否则无法找到 @Param("offset")告诉mybatis形参是offset，以便mapper中找到#{offset}，
	 * 
	 */
	List<Seckill> queryAll(@Param("offset")int offset,@Param("limit")int limit);

	void killByProcedure(Map<String,Object> paramMap);

}
