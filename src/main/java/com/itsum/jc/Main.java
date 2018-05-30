package com.itsum.jc;

import com.itsum.jc.extract.gcw.GCW_LoginService;
import com.itsum.jc.extract.zjtcn.ZJT_LoginService;
import com.itsum.jc.moudles.PageConstant;
import com.itsum.jc.moudles.TSession;
import com.itsum.jc.service.gcw.*;
import com.itsum.jc.service.zjt.Ckj_Service;
import com.itsum.jc.service.zjt.Scj_Service;
import com.itsum.jc.service.zjt.Xxj_Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 入口方法
 * 
 * @author Jason Ma
 *
 */
public class Main {
	
	public static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		System.setProperty("log4j.skipJansi", "true");
		try{
			ENV.init();

			//广材网
			Thread threadGCW = new Thread(new Runnable() {

				@Override
				public void run() {
					logger.info("广材网爬虫任务候车中.......");
					while (true){
//						LocalDateTime localDateTime = LocalDateTime.now();
//						DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH");
//						String hour = localDateTime.format(dateTimeFormatter);//获取当前小时
						//if("19".equals(hour)){
							logger.info("sleep.......");
							//参数为true代表验证数据重复
							ScjService.saveScj(10, false);
							XxjService.saveXxj(100, false);
//							GchqService.saveGchq(5, false);
							GchqService.saveGchq(20, false);
//							DlhbjService.saveDlhbj(5, false);
							DlhbjService.saveDlhbj(50, false);
//							RgxjService.saveRgxj(20, false);
							RgxjService.saveRgxj(100, false);
							try {
								Thread.sleep(30*60000);//60000为一分钟
							} catch (InterruptedException e) {
							}
						//}
					}
				}
			});

			Thread threadZJT = new Thread(new Runnable() {
				@Override
				public void run() {
					logger.info("造价通爬虫任务候车中.......");
					while (true) {
						logger.info("sleep.......");
						Ckj_Service.saveZJTCkj(20, false);
						Scj_Service.saveZJTScj(40, false);
						Xxj_Service.saveZJTXxj(1, true);
						try {
							Thread.sleep(30 * 60000);
						} catch (InterruptedException e) {
						}
					}
				}
			});
			logger.info("启动广材网爬虫");
			threadGCW.start();
			logger.info("启动造价通爬虫");
			threadZJT.start();
			
			try {
				logger.info("启动广材网爬虫");
				threadGCW.join();
				logger.info("启动造价通爬虫");
				threadZJT.join();
			} catch (InterruptedException e) {
				logger.error("InterruptedException:", e);
			}
		}catch (Throwable e) {
			logger.error("Exception:", e);
		}
		
		
	}

	public static void validSession(){
		List<TSession> GCWList = ENV.db().queryList("TYPE=?", "CREATE_DATE ASC", TSession.class, "1");
		if(GCWList != null && GCWList.size() > 0){
			boolean isValidGCW = GCW_LoginService.isValidSession(GCWList.get(0).getSessionId());
			logger.info("广材网session循环验证，状态为"+isValidGCW);
		}
		List<TSession> ZJTList = ENV.db().queryList("TYPE=?", "CREATE_DATE ASC", TSession.class, "2");
		if(ZJTList != null && ZJTList.size() > 0){
			ZJT_LoginService.isValidSession(ZJTList.get(0).getSessionId());
			boolean isvalidZJT = ZJT_LoginService.isValidSession(ZJTList.get(0).getSessionId());
			logger.info("造价通session循环验证，状态为"+isvalidZJT);
		}
	}

}
