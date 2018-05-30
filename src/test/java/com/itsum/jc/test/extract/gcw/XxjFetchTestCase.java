package com.itsum.jc.test.extract.gcw;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itsum.jc.extract.gcw.XxjFetchService;
import com.itsum.jc.extract.gcw.dto.XXJ_PriceDTO;

public class XxjFetchTestCase {
	
	public static final Logger logger = LoggerFactory.getLogger(XxjFetchTestCase.class);
	
	private static String sessionId = "c899a77a-cb47-4053-8140-2849d1d42079";

	@Test
	public void test() {
		List<XXJ_PriceDTO> priceList = XxjFetchService.fetchSearch(sessionId, "2018", "04", 1);
		if(priceList != null && priceList.size() > 0){
			for(XXJ_PriceDTO p : priceList){
				logger.info(p.toString());
			}
		}
	}

}
