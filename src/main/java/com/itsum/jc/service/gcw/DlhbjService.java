package com.itsum.jc.service.gcw;

import com.itsum.jc.ENV;
import com.itsum.jc.extract.gcw.DlhbjFetchService;
import com.itsum.jc.extract.gcw.dto.DLHBJ_PriceDTO;
import com.itsum.jc.moudles.PageConstant;
import com.itsum.jc.service.BaseService;
import com.itsum.jc.service.SessionService;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DlhbjService extends BaseService {

    public static final Logger logger = LoggerFactory.getLogger(DlhbjService.class);
    /**
     * 保存广材网电缆红本价
     * @param num
     */
    public static void saveDlhbj(int num,boolean flag) {
        String sessionId = SessionService.getGCW();
        if (StringUtil.isBlank(sessionId)) return;
        int countTotal = 0;//记录保存成功数量
        int pageAction = checkPageRecord(num,PageConstant.GCW_DLHBJ,PageConstant.GCW);
        if(pageAction == 0){
            return;
        }
        List<DLHBJ_PriceDTO> listDLHBJ = DlhbjFetchService.fetchSearch_jcCrawler(sessionId, num,pageAction);
        if (listDLHBJ != null && listDLHBJ.size() > 0) {
            logger.info("广材网---电缆红本价页面---爬取数据量---"+listDLHBJ.size()+"条");
            for (int i = 0; i < listDLHBJ.size(); i++) {
                String flagId = listDLHBJ.get(i).getSpec() + listDLHBJ.get(i).getCableProductId() + listDLHBJ.get(i).getCableProductPriceId();
                if(!getLinkRecord(flagId,PageConstant.GCW,PageConstant.GCW_DLHBJ)){
                    continue;
                }


                List<DLHBJ_PriceDTO> listDLHBJhistory = DlhbjFetchService.fetchHistory(sessionId, listDLHBJ.get(i),1);
                listDLHBJhistory.add(listDLHBJ.get(i));
                if(listDLHBJhistory != null && listDLHBJhistory.size() > 0){
                    int count = 0;//记录重复记录
                    int countSucc = 0;
                    logger.info("广材网---电缆红本价---爬取当前数据历史详情数据"+listDLHBJhistory.size()+"条");
                    for(int j = 0; j<listDLHBJhistory.size(); j++){
                        if(flag){
                            if(count > 10){
                                logger.info("广材网---电缆红本价页面---重复数量达到10条，跳出当前循环");
                                break;
                            }
                        }
                        DLHBJ_PriceDTO dlhbj_priceDTO = listDLHBJ.get(i);
                        if (!StringUtil.isBlank(dlhbj_priceDTO.getPrice())) {//判断价格非空
                            String id = dlhbj_priceDTO.getSpec() + dlhbj_priceDTO.getCableProductId()+dlhbj_priceDTO.getPublishTime()+dlhbj_priceDTO.getPrice();//规格+产品id+发布时间+价格
                            if (ENV.db().query(id, DLHBJ_PriceDTO.class) == null) {//判断id重复
                                dlhbj_priceDTO.setId(id);
                                ENV.db().save(dlhbj_priceDTO);
                                countSucc++;
                                countTotal++;
                            }else{
                                count++;
                            }
                        } else {
                            count++;
                        }
                    }
                    logger.info("广材网---电缆红本价页面---当前链接保存成功数量---"+countSucc+"条");
                }


                saveLinkRecord(flagId,PageConstant.GCW,PageConstant.GCW_DLHBJ);
                if(listDLHBJ.get(i).getPageNo() != null){
                    savePageRecord(listDLHBJ.get(i).getPageNo(),PageConstant.GCW_DLHBJ);
                }
            }
        }
        logger.info("广材网---电缆红本价页面---保存成功总数量---"+countTotal+"条");
    }

    public static void main(String[] args) {
        ENV.init();
        saveDlhbj(2,true);
    }

}
