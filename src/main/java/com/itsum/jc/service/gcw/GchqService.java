package com.itsum.jc.service.gcw;

import com.itsum.jc.ENV;
import com.itsum.jc.extract.gcw.GchqAreaCodeService;
import com.itsum.jc.extract.gcw.GchqFetchService;
import com.itsum.jc.extract.gcw.dto.Gchq_AreaCodeDTO;
import com.itsum.jc.extract.gcw.dto.Gchq_PriceDTO;
import com.itsum.jc.moudles.PageConstant;
import com.itsum.jc.service.BaseService;
import com.itsum.jc.service.SessionService;
import org.jsoup.helper.StringUtil;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class GchqService extends BaseService {

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(GchqService.class);
    /**
     * 保存广材网钢材行情数据
     * @param num
     */
    public static void saveGchq(int num, boolean flag) {
        String sessionId = SessionService.getGCW();
        if (StringUtil.isBlank(sessionId)) {
            return;
        }
        List<Gchq_AreaCodeDTO> listGchqArea = null;
        try {
            listGchqArea = GchqAreaCodeService.fetchAreaCodes(sessionId);
        } catch (IOException e) {
            logger.error("广材网---钢材行情页面---爬取数据异常");
            e.printStackTrace();
        }
        int countTotal = 0;
        if (listGchqArea != null && listGchqArea.size() > 0) {
            logger.info("广材网---钢材行情页面---爬取所有区域数据量---" + listGchqArea.size() + "条");
            for (int i = 0; i < listGchqArea.size(); i++) {
                if(!getAreaCodeRecord(listGchqArea.get(i).getCode(),PageConstant.GCW,PageConstant.GCW_GCHQ)){//验证区域重复
                    continue;
                }


                List<Gchq_PriceDTO> listGchq = GchqFetchService.fetchPriceByDateAndName(sessionId, listGchqArea.get(i), "", "", num);
                if (listGchq != null && listGchq.size() > 0) {
                    int count = 0;//记录重复记录
                    int countSucc = 0;//记录保存成功数量
                    logger.info("广材网---钢材行情页面---"+listGchqArea.get(i).getName()+"区域爬取详情数据量---" + listGchq.size() + "条");
                    for (int j = 0; j < listGchq.size(); j++) {
                        if (flag) {
                            if (count > 30) {
                                logger.info("广材网---钢材行情页面---重复数量达到30条，跳出当前区域数据循环");
                                break;
                            }
                        }
                        Gchq_PriceDTO gchq_priceDTO = listGchq.get(j);
                        if (!StringUtil.isBlank(gchq_priceDTO.getPrice())) {//判断价格非空
                            String id = gchq_priceDTO.getName() + gchq_priceDTO.getSpec() + gchq_priceDTO.getTexture() + gchq_priceDTO.getManufacturer() + gchq_priceDTO.getDate()+gchq_priceDTO.getPrice();//名称+规格+材质+产地+日期+价格
                            if (ENV.db().query(id, Gchq_PriceDTO.class) == null) {//检验id重复
                                gchq_priceDTO.setId(id);
                                ENV.db().save(gchq_priceDTO);
                                countSucc++;
                                countTotal++;
                            } else {
                                count++;//重复数据累计数
                            }
                        } else {
                            count++;//价格为空累计数
                        }
                    }
                    logger.info("广材网---钢材行情页面---"+listGchqArea.get(i).getName()+"区域保存成功数量---" + countSucc + "条");
                }


                saveAreaCodeRecord(listGchqArea.get(i).getCode(),PageConstant.GCW,PageConstant.GCW_GCHQ);//保存区域记录
            }
        }
        logger.info("广材网---钢材行情页面---保存成功总数量---" + countTotal + "条");
    }

    public static void main(String[] args) {
        ENV.init();
        saveGchq(5,true);
    }

}
