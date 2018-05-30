package com.itsum.jc.service.gcw;

import com.itsum.jc.ENV;
import com.itsum.jc.extract.gcw.ScjFetchService;
import com.itsum.jc.extract.gcw.dto.SCJ_PriceDTO;
import com.itsum.jc.extract.gcw.dto.SpuDTO;
import com.itsum.jc.moudles.PageConstant;
import com.itsum.jc.service.BaseService;
import com.itsum.jc.service.SessionService;
import org.jsoup.helper.StringUtil;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ScjService extends BaseService {

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(ScjService.class);
    /**
     * 保存广材网市场价数据
     * @param num
     */
    public static void saveScj(int num,boolean flag) {
        String sessionId = SessionService.getGCW();
        if (StringUtil.isBlank(sessionId)) return;
        int pageAction = checkPageRecord(num,PageConstant.GCW_SCJ,PageConstant.GCW);
        if(pageAction == 0){
            return;
        }
        int countTotal = 0;//记录保存成功数量
        List<SpuDTO> listSpuDTO = ScjFetchService.fetchSearchLink_jcCrawler(sessionId, num,pageAction);
        if (listSpuDTO != null && listSpuDTO.size() > 0) {
            logger.info("广材网---市场价页面---爬取Spu链接数量---" + listSpuDTO.size() + "条");
            for (int i = 0; i < listSpuDTO.size(); i++) {
                String spuName = listSpuDTO.get(i).getName();
                if(!getLinkRecord(spuName,PageConstant.GCW,PageConstant.GCW_SCJ)){
                    continue;
                }


                List<SCJ_PriceDTO> listSCJ = ScjFetchService.fetchDeteil(sessionId, spuName, num);
                if (listSCJ != null && listSCJ.size() > 0) {
                    int count = 0;//记录重复记录
                    int countSucc = 0;
                    logger.info("广材网---市场价页面---当前链接爬取详情数据量---" + listSCJ.size() + "条");
                    for (int j = 0; j < listSCJ.size(); j++) {
                        if(flag){
                            if(count > 20){
                                logger.info("广材网---市场价页面---当前链接重复数量达到20条，跳出当前循环");
                                break;
                            }
                        }
                        if (!StringUtil.isBlank(listSCJ.get(j).getTaxPriceMarket())) {//判断价格非空
                            SCJ_PriceDTO scj_priceDTO = listSCJ.get(j);
                            String id = scj_priceDTO.getCityName() + scj_priceDTO.getName() + scj_priceDTO.getSpec() + scj_priceDTO.getTexture()+scj_priceDTO.getBrand()+scj_priceDTO.getCompanyName()+scj_priceDTO.getTaxPriceMarket();//城市+产品名称+规格+材质+品牌+公司名称+含税市场价
                            if (ENV.db().query(id, SCJ_PriceDTO.class) == null) {//验证id重复
                                scj_priceDTO.setId(id);
                                ENV.db().save(scj_priceDTO);
                                countSucc++;
                                countTotal++;
                            }else{
                                count++;
                            }
                        }else{
                            count++;
                        }
                    }
                    logger.info("广材网---市场价页面---当前链接保存成功数量---" + countSucc + "条");
                }


                saveLinkRecord(spuName,PageConstant.GCW,PageConstant.GCW_SCJ);
                if(listSpuDTO.get(i).getPageNo() != null){
                    savePageRecord(listSpuDTO.get(i).getPageNo(),PageConstant.GCW_SCJ);
                }
            }
        }
        logger.info("广材网---市场价页面---保存成功总数量---" + countTotal + "条");
    }

    public static void main(String[] args) {
        ENV.init();
        saveScj(3,false);
    }

}
