package com.seckill.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seckill.dto.SeckillExcution;
import com.seckill.enums.SeckillStatusEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillResult;
import com.seckill.entity.Seckill;
import com.seckill.service.SeckillService;


@Controller
@RequestMapping("/seckill") //url:/模块/资源/{id}/细分   
public class SeckillController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillService seckillService;

	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public String list(Model model){
		//list.jsp + model = ModelAndView
		List<Seckill> seckills = seckillService.getSeckillList();
		model.addAttribute("seckills", seckills);
		return "list";  //WEB-INF/jsp/list.jsp    查看 springmvc.xml
	}
	
	/**
	 * get seckill by seckillId
	 * @param seckillId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId,Model model){
		if(seckillId == null){
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if(null == seckill){
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill",seckill);
		return "detail";
	}
	
	
	/**
	 * 秒杀详情页面
	 * @param seckillId
	 * @return
	 */
	
	//ajax return json
	@RequestMapping(value = "/{seckillId}/exposer",method = RequestMethod.POST,
			produces = {"application/json;charset=UTF-8"})//http context
	@ResponseBody  //packging return result to json
	public SeckillResult<Exposer> exposer(@PathVariable Long seckillId){
		SeckillResult<Exposer> result;
		try{
			Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
			result = new SeckillResult<Exposer>(true,exposer);
		}catch (Exception e){
			logger.error(e.getMessage());
			result = new SeckillResult<Exposer>(false,e.getMessage());
		}
		return result;
	}
	
	/**
	 * 执行秒杀
	 * @param seckillId
	 * @param md5
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/{md5}/execution",method = RequestMethod.POST,
			produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<SeckillExcution> execute(@PathVariable("seckillId") Long seckillId,@PathVariable("md5") String md5,
			@CookieValue(value = "killPhone",required = false) Long phone){
		if(phone == null){ //springmvc valid
			return new SeckillResult<SeckillExcution>(false,"do not login");
		}
		try{
			SeckillExcution seckillExcution = seckillService.executeSeckill(seckillId, phone, md5);
			return new SeckillResult<SeckillExcution>(true,seckillExcution);
		}catch (RepeatKillException e){
			SeckillExcution seckillExcution = new SeckillExcution(seckillId, SeckillStatusEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExcution>(true,seckillExcution);
		}catch (SeckillCloseException e){
			SeckillExcution seckillExcution = new SeckillExcution(seckillId,SeckillStatusEnum.END);
			return new SeckillResult<SeckillExcution>(true,seckillExcution);
		}
		catch (Exception e){
			logger.error(e.getMessage());
			SeckillExcution seckillExcution = new SeckillExcution(seckillId,SeckillStatusEnum.INNER_ERROR);
			return new SeckillResult<SeckillExcution>(true,seckillExcution);
		}
	}

	/**
	 * 获取系统时间
	 * @return
	 */
	@RequestMapping(value = "/time/now",
			method = RequestMethod.GET,
			produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<Long> time(){
		Date date = new Date();
		return  new SeckillResult<Long>(true,date.getTime());
	}
}
