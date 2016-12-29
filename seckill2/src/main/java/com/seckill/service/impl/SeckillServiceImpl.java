package com.seckill.service.impl;

import com.seckill.cache.RedisDao;
import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessKillDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExcution;
import com.seckill.entity.Seckill;
import com.seckill.enums.SeckillStatusEnum;
import com.seckill.entity.SuccessKill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jo on 10/18/16.
 */

//@Component @Service @Repository @Controller
@Service
@PropertySource(value = "classpath:sault.properties")
public class SeckillServiceImpl implements SeckillService{


	//注入Service依赖
	@Autowired  
	private SuccessKillDao successKillDao;
	@Autowired
	private SeckillDao seckillDao;
	@Value("${sault}")
	public String sault; //MD5盐值字符串，用户混淆Md5
	@Autowired
	private RedisDao redisDao;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 100);
	}

	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	/**
	 * 输出秒杀接口地址
	 * expose seckill url when seckill start,else expose system time and kill time
	 * @param seckillId
	 */
	public Exposer exposeSeckillUrl(long seckillId) {
		Seckill seckill = redisDao.getSeckill(seckillId);
		if(null == seckill) {
			seckill = getById(seckillId);
			if (seckill == null) {
				return new Exposer(false, seckillId);
			}
			else{
				redisDao.putSeckill(seckill);
			}
		}
		Date createTime = seckill.getCreateTime();
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date currentTime = new Date();
		//seckill success
		if(currentTime.after(startTime) && currentTime.before(endTime)){
			//conversion String to special String (can't reverse)
			//转换字符串为特殊字符串，不可逆
			String md5 = getMd5(seckillId);
			return new Exposer(true,md5,seckillId);
		}
		else{
			return new Exposer(false,seckillId,currentTime.getTime(),startTime.getTime(),endTime.getTime());
		}
	}

	private String getMd5(long seckillId){
		String base = seckillId+"/"+sault;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes()); //spring工具类
		return md5;
	}

	@Transactional  //rollback when runtimeException happend
	/**
	 * 使用注解控制事务方法的优点：
	 * 1：开发团队达成一致约定，明确标注事务方法的编程风格；
	 * 2：保证事务方法的执行时间尽可能短，不要穿插其它的网络操作，RPC/HTTP请求/或者剥离到方法外部
	 * 3：不是所有的方法都需要事务，如：只有一条修改操作，只读操作不需要事务控制，
	 */
	public SeckillExcution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		if(md5 == null || !md5.equals(getMd5(seckillId))){
			throw new SeckillException("seckill data rewrite");
		}
		
		//执行秒杀逻辑：减库存 + 记录购买行为
		Date currentTime = new Date();
		try {
			//record purchase message 记录购买行为
			int insertCount = successKillDao.insertSuccessKill(seckillId, userPhone);
			if(insertCount<=0){
				//repeat seckill 重复插入，即：重复秒杀异常
				throw new RepeatKillException("seckill repeated");
			} 
			else{
				//execute seckill:1.reduce product 2.record purchase message    
				//热点商品竞争
				int updateCount = seckillDao.reduceNumber(seckillId,currentTime);
				//do not update for record
				if(updateCount<=0){ 
					//没有更新记录
					throw new SeckillCloseException("seckill is close");
				}
				else{
					//秒杀成功
					SuccessKill successKill = successKillDao.queryByIdWithSeckill(seckillId,userPhone);
					return new SeckillExcution(seckillId, SeckillStatusEnum.SUCCESS,successKill);
				}

			}
			
			

		}catch (SeckillCloseException e1){
			throw e1;
		}catch (RepeatKillException e2){
			throw e2;
		}catch (Exception e){
			//rollback
			logger.error(e.getMessage(),e);
			
//			org.apache.ibatis.binding.BindingException: Invalid bound statement (not found):
//			com.seckill.dao.SuccessKillDao.insertSuccessKill
			throw new SeckillException("seckill inner error:"+e.getMessage());
		}
	}

	public SeckillExcution executeSeckillProcedure(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		 if(md5 == null || md5.equals(getMd5(seckillId))){
			 return new SeckillExcution(seckillId,SeckillStatusEnum.DATA_REWRITE);
		 }
		Date time = new Date();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("seckillId",seckillId);
		map.put("phone",userPhone);
		map.put("killTime",time);
		map.put("result",null);
		try {
			seckillDao.killByProcedure(map);
			int result = MapUtils.getInteger(map,"result",-2);
			if(result == 1){
				SuccessKill successKill = successKillDao.queryByIdWithSeckill(seckillId,userPhone);
				return new SeckillExcution(seckillId,SeckillStatusEnum.SUCCESS,successKill);
			}
			else{
				return new SeckillExcution(seckillId,SeckillStatusEnum.statusOf(result));
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return new SeckillExcution(seckillId,SeckillStatusEnum.INNER_ERROR);
		}
	}

}
