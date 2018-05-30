package com.itsum.jc.service.zjt;

import com.itsum.jc.ENV;
import com.itsum.jc.extract.zjtcn.Scj_FetchService;
import com.itsum.jc.extract.zjtcn.dto.ZJT_SCJ_PriceDTO;
import com.itsum.jc.moudles.PageConstant;
import com.itsum.jc.service.BaseService;
import com.itsum.jc.service.SessionService;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * 造价通市场价数据保存
 */
public class Scj_Service extends BaseService {

    public static final Logger logger = LoggerFactory.getLogger(Scj_Service.class);

    public static void saveZJTScj(int num, boolean flag) {
        String sessionId = SessionService.getZJT();
        if (StringUtil.isBlank(sessionId)) return;
        int countTotal = 0;
        List<ZJT_SCJ_PriceDTO> listZJT = Scj_FetchService.fetchSearch_jcCrawler(sessionId, "", "", num);
        if (listZJT != null && listZJT.size() > 0) {
            logger.info("造价通---市场价页面---爬取Spu链接数量---" + listZJT.size() + "条");
            for (int i = 0; i < listZJT.size(); i++) {
                String link = listZJT.get(i).getLink();
                if(!getLinkRecord(link,PageConstant.ZJT,PageConstant.ZJT_SCJ)){
                    continue;
                }


                try {
                    List<ZJT_SCJ_PriceDTO> listDetail = Scj_FetchService.fetchDetail_jcCrawler(sessionId, listZJT.get(i));
                    if (listDetail != null && listDetail.size() > 0) {
                        logger.info("造价通---市场价页面---爬取单前链接详情数据"+listDetail.size()+"条");
                        int count = 0;
                        int countSucc = 0;
                        for (int j = 0; j < listDetail.size(); j++) {
                            if (flag) {
                                if (count > 10) {
                                    logger.info("造价通---市场价页面---重复数量达到10条，跳出当前循环");
                                    break;
                                }
                            }
                            ZJT_SCJ_PriceDTO zjt_ckj_priceDTO = listDetail.get(j);
                            if (!StringUtil.isBlank(zjt_ckj_priceDTO.getTaxPrice())) {//价格不为空
                                String id = zjt_ckj_priceDTO.getName() + zjt_ckj_priceDTO.getSpec() + zjt_ckj_priceDTO.getCompanyName() + zjt_ckj_priceDTO.getPublishTime()+zjt_ckj_priceDTO.getTaxPrice();//名称+型号+供应商
                                zjt_ckj_priceDTO.setId(id);
                                if (ENV.db().query(id, ZJT_SCJ_PriceDTO.class) == null) {//id没保存过才保存
                                    ENV.db().save(zjt_ckj_priceDTO);
                                    countSucc++;
                                    countTotal++;
                                } else {
                                    count++;
                                }
                            } else {
                                count++;
                            }
                        }
                        logger.info("造价通---市场价页面---当前链接保存成功数量---" + countSucc + "条");
                    }
                } catch (IOException e) {
                    logger.error("造价通---市场价页面---爬取数据异常");
                    e.printStackTrace();
                }


                saveLinkRecord(link,PageConstant.ZJT,PageConstant.ZJT_SCJ);
            }
            logger.info("造价通---市场价页面---保存成功总数量---" + countTotal + "条");
        }
    }

    public static void main(String[] args) {
        ENV.init();
        saveZJTScj(2,true);
    }
}
