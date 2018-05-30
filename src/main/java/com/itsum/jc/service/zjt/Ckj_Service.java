package com.itsum.jc.service.zjt;

import com.itsum.jc.ENV;
import com.itsum.jc.extract.zjtcn.Ckj_FetchService;
import com.itsum.jc.extract.zjtcn.dto.ZJT_CKJ_PriceDTO;
import com.itsum.jc.moudles.PageConstant;
import com.itsum.jc.service.BaseService;
import com.itsum.jc.service.SessionService;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;


/**
 * 造价通参考价数据保存
 */
public class Ckj_Service extends BaseService {

    public static final Logger logger = LoggerFactory.getLogger(Ckj_Service.class);

    public static void saveZJTCkj(int num, boolean flag) {
        String sessionId = SessionService.getZJT();
        if (StringUtil.isBlank(sessionId)) return;
//        String[] date = getDateArr();
//        for (int i = 0; i < date.length; i++) {
        int countTotal = 0;//记录保存成功数量
        List<String> listZJTCKJ = Ckj_FetchService.fetchSearch_jcCralwer(sessionId, "", num);
        if (listZJTCKJ != null && listZJTCKJ.size() > 0) {
            logger.info("造价通---参考价页面---爬取Spu链接数量---" + listZJTCKJ.size() + "条");
            for (int i = 0; i < listZJTCKJ.size(); i++) {
                if(!getLinkRecord(listZJTCKJ.get(i),PageConstant.ZJT,PageConstant.ZJT_CKJ)){
                    continue;
                }

                try {
                    List<ZJT_CKJ_PriceDTO> listZJTCKJDetail = Ckj_FetchService.fetchDetail(sessionId, listZJTCKJ.get(i));
                    if(listZJTCKJDetail != null && listZJTCKJDetail.size() > 0){
                        logger.info("造价通---参考价页面---爬取单前链接详情数据"+listZJTCKJDetail.size()+"条");
                        int count = 0;
                        int countSucc = 0;
                        for (int j = 0; j < listZJTCKJDetail.size(); j++) {
                            if (flag) {
                                if (count > 10) {
                                    logger.info("造价通---参考价页面---重复数量达到10条，跳出当前循环");
                                    break;
                                }
                            }
                            ZJT_CKJ_PriceDTO zjt_ckj_priceDTO = listZJTCKJDetail.get(j);
                            if (!StringUtil.isBlank(zjt_ckj_priceDTO.getPrice())) {
                                String id = zjt_ckj_priceDTO.getName() + zjt_ckj_priceDTO.getSpec() + zjt_ckj_priceDTO.getSource() + zjt_ckj_priceDTO.getPublishTime()+zjt_ckj_priceDTO.getPrice();//名称+规格+来源+发布时间+价格
                                if (ENV.db().query(id, ZJT_CKJ_PriceDTO.class) == null) {
                                    zjt_ckj_priceDTO.setId(id);
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
                        logger.info("造价通---参考价页面---当前链接保存成功数量---" + countSucc + "条");
                    }
                } catch (IOException e) {
                    logger.error("造价通---参考价页面---爬取数据异常");
                    e.printStackTrace();
                }


                saveLinkRecord(listZJTCKJ.get(i),PageConstant.ZJT,PageConstant.ZJT_CKJ);
            }
        }
        logger.info("造价通---参考价页面---保存成功总数量---" + countTotal + "条");
//        }
    }

    public static void main(String[] args) {
        ENV.init();
        saveZJTCkj(2,true);
    }
}
