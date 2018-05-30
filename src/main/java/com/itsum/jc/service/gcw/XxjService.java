package com.itsum.jc.service.gcw;

import com.itsum.jc.ENV;
import com.itsum.jc.extract.gcw.XxjFetchService;
import com.itsum.jc.extract.gcw.dto.XXJ_PriceDTO;
import com.itsum.jc.moudles.PageConstant;
import com.itsum.jc.service.BaseService;
import com.itsum.jc.service.SessionService;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class XxjService extends BaseService {

    public static final Logger logger = LoggerFactory.getLogger(XxjService.class);
    /**
     * 保存广材网信息价数据
     * @param num
     */
    public static void saveXxj(int num,boolean flag) {
        String sessionId = SessionService.getGCW();
        if (StringUtil.isBlank(sessionId)) return;
        int pageAction = checkPageRecord(num,PageConstant.GCW_XXJ,PageConstant.GCW);
        if(pageAction == 0){
            return;
        }
//        String date[] = getDateArr();
        int countTotal = 0;//记录保存成功数量
//        for (int i = 0; i < date.length; i++) {
            List<XXJ_PriceDTO> listXXJ = XxjFetchService.fetchSearch_jcCrawler(sessionId, "", "", num,pageAction);
            if (listXXJ != null && listXXJ.size() > 0) {
                logger.info("广材网---信息价页面---爬取详情数据量---"+listXXJ.size()+"条");
                int count = 0;//记录重复数据
                for (int j = 0; j < listXXJ.size(); j++) {
                    if(flag){
                        if(count > 100){
                            logger.info("广材网---信息价页面---重复数量达到100条，跳出当前循环");
                            break;
                        }
                    }
                    String taxPriceMarket = listXXJ.get(j).getTaxPriceMarket();
                    if (!StringUtil.isBlank(taxPriceMarket)) {//如果价格不为空才保存
                        XXJ_PriceDTO xxj_priceDTO = listXXJ.get(j);
                        String id = xxj_priceDTO.getCityName()+xxj_priceDTO.getName() + xxj_priceDTO.getSpec()+xxj_priceDTO.getMajor();//城市名字+名称+规格+专业
                        if (ENV.db().query(id, XXJ_PriceDTO.class) == null) {//id没有保存过才保存
                            xxj_priceDTO.setId(id);
                            ENV.db().save(xxj_priceDTO);
                            countTotal++;
                        }else{
                            count++;
                        }
                    }else{
                        count++;
                    }
                }
            }
//        }
        logger.info("广材网---信息价页面---保存成功总数量---"+countTotal+"条");
    }

    public static void main(String[] args) {
        ENV.init();
        saveXxj(12,true);
    }


}
