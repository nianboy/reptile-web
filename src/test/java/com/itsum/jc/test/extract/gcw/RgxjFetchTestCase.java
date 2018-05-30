package com.itsum.jc.test.extract.gcw;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itsum.jc.extract.gcw.RgxjFetchService;
import com.itsum.jc.extract.gcw.dto.RGXJ_PriceDTO;

public class RgxjFetchTestCase {
	
	public static final Logger logger = LoggerFactory.getLogger(RgxjFetchTestCase.class);
	
	private static String sessionId = "ed0cee1f-e033-42a6-a452-89f91821b676";

	@Test
	public void test() {
		List<RGXJ_PriceDTO> priceList = RgxjFetchService.fetchSearch(sessionId, 2);
		if(priceList != null && priceList.size() > 0){
			logger.info("total:{}", priceList.size());
			for(RGXJ_PriceDTO p : priceList){
				logger.info(p.toString());
			}
		}
	}

}
