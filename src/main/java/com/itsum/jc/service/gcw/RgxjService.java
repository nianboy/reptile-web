package com.itsum.jc.service.gcw;

import com.itsum.jc.ENV;
import com.itsum.jc.extract.gcw.RgxjFetchService;
import com.itsum.jc.extract.gcw.dto.RGXJ_PriceDTO;
import com.itsum.jc.moudles.PageConstant;
import com.itsum.jc.service.BaseService;
import com.itsum.jc.service.SessionService;
import org.jsoup.helper.StringUtil;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RgxjService extends BaseService {

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(RgxjService.class);

    /**
     * 广材网人工询价保存
     * @param num
     * @param flag
     */
    public static void saveRgxj(int num, boolean flag) {
        String sessionId = SessionService.getGCW();
        if (StringUtil.isBlank(sessionId)) return;
        int countTotal = 0;
        int pageAction = checkPageRecord(num,PageConstant.GCW_RGXJ,PageConstant.GCW);
        if(pageAction == 0){
            return;
        }
        boolean pageOver = false;
        List<RGXJ_PriceDTO> listRGXJ = RgxjFetchService.fetchSearch_jcCrawler(sessionId, num,pageAction);
        if (listRGXJ != null && listRGXJ.size() > 0) {
            pageOver = listRGXJ.get(0).isPageOver();
            logger.info("广材网---人工询价页面---爬取数据量---" + listRGXJ.size() + "条");
            int count = 0;//记录重复记录
            for (int i = 0; i < listRGXJ.size(); i++) {
                if (flag) {
                    if (count > 100){
                        logger.info("广材网---人工询价页面---重复数量达到100条，跳出当前循环");
                        break;
                    }
                }
                RGXJ_PriceDTO rgxj_priceDTO = listRGXJ.get(i);
                if (!StringUtil.isBlank(rgxj_priceDTO.getPrice())) {//如果价格不为空
                    String id = rgxj_priceDTO.getArea() + rgxj_priceDTO.getName() + rgxj_priceDTO.getSpec() + rgxj_priceDTO.getPublishTime()+rgxj_priceDTO.getPrice();
                    if (ENV.db().query(id, RGXJ_PriceDTO.class) == null) {//查询id没保存过才保存
                        rgxj_priceDTO.setId(id);
                        ENV.db().save(rgxj_priceDTO);
                        countTotal++;
                    } else {
                        count++;
                    }
                } else {
                    count++;
                }
            }
        }
        logger.info("广材网---人工询价页面---保存成功总数量---" + countTotal + "条");

        if(pageOver){
            ENV.db().updateBySQL("DELETE FROM T_PAGE_RECORD WHERE TYPE=?",PageConstant.GCW);
        }
    }

    public static void main(String[] args) {
        ENV.init();
        saveRgxj(3,false);//最大38904页
    }
}
