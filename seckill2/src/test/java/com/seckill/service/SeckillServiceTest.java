package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExcution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Jo on 10/19/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
		"classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	SeckillService seckillService;

	@Test
	public void testGetSeckillList() throws Exception {
		List<Seckill> seckills = seckillService.getSeckillList();
		logger.info("1.seckills={}",seckills);
	}

	@Test
	public void testGetById() throws Exception {
		long id = 1000l;
		Seckill seckill = seckillService.getById(id);
		logger.info("2.seckill={}",seckill);
	}

	//集成测试代码完整逻辑，注意可重复执行
	@Test
	public void testSeckillLogic() throws Exception {
		long id = 1002l;
		Exposer exposer = seckillService.exposeSeckillUrl(id);
		if(exposer.isExposed()){
			logger.info("exposer={}",exposer);
			String md5 = exposer.getMd5();
			long phone = 18868831752l;
			try{
				SeckillExcution seckillExcution = seckillService.executeSeckill(id, phone, md5);
				logger.info("seckillExcuation={}"+seckillExcution);
			}catch (RepeatKillException e){
				logger.error(e.getMessage());
			}catch (SeckillCloseException e){
				logger.error(e.getMessage());
			}
		}
		else{
			//seckill don't start 秒杀未开启
			logger.warn("exposer={}",exposer);
		}
	}

	@Test
	public void testExecuteSeckill() throws Exception {
//		long id = 1002l;
//		long phone = 18868831752l;
//		try{
//			String md5 = "2bf959ac10da022a1bc9a7c98c4b42c8";
//			SeckillExcution seckillExcution = seckillService.executeSeckill(id, phone, md5);
//			logger.info("seckillExcuation={}"+seckillExcution);
//		}catch (RepeatKillException e){
//			logger.error(e.getMessage());
//		}catch (SeckillCloseException e){
//			logger.error(e.getMessage());
//		}

	}
	@Test
	public void executeSeckillProcedure(){
		long seckillId = 1004;
		long phone = 18868831756l;
		Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
		if(exposer.isExposed()){
			String md5 = exposer.getMd5();
			SeckillExcution seckillExcution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
			logger.info("============="+seckillExcution.getStatusInfo());
		}
	}

}