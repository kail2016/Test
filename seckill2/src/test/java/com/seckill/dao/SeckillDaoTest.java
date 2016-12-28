package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Jo on 10/18/16.
 */

/**
 * load springIoc container when start junit
 * 配置spring和junit整合，junit启动时加载springIOC容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	
	 //注入Dao实现类依赖
	@Autowired
	private SeckillDao seckillDao;

	@Test
	public void testQueryById() throws Exception {
		long id = 1000;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill);
	}

	@Test
	public void testReduceNumber() throws Exception {
		Date killTime = new Date();
		int reduceNumber = seckillDao.reduceNumber(1000,killTime);
		System.out.println("reduceNumber:"+reduceNumber);
	}


	@Test
	public void testQueryAll() throws Exception {
		List<Seckill> seckills = seckillDao.queryAll(0, 100);
		System.out.println(seckills);
	}
}