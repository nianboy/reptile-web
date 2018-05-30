package com.itsum.jc.service.zjt;

import com.itsum.jc.ENV;
import com.itsum.jc.extract.zjtcn.CitySite_FetchService;
import com.itsum.jc.extract.zjtcn.Xxj_FetchAreaService;
import com.itsum.jc.extract.zjtcn.Xxj_FetchByIdService;
import com.itsum.jc.extract.zjtcn.Xxj_FetchService;
import com.itsum.jc.extract.zjtcn.dto.ZJT_CitySiteDTO;
import com.itsum.jc.extract.zjtcn.dto.ZJT_CitySite_AreaDTO;
import com.itsum.jc.extract.zjtcn.dto.ZJT_XXJ_PriceDTO;
import com.itsum.jc.moudles.PageConstant;
import com.itsum.jc.service.BaseService;
import com.itsum.jc.service.SessionService;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * 造价通信息价保存
 */
public class Xxj_Service extends BaseService {

    public static Logger logger = LoggerFactory.getLogger(Xxj_Service.class);
    public static void saveZJTXxj(int num,boolean flag){
        String sessionId = SessionService.getZJT();
        if(StringUtil.isBlank(sessionId)) return;
        int countTotal = 0;
        boolean areaOver = false;
        boolean industryOver = false;
        boolean cityOver = false;
        List<ZJT_CitySiteDTO> listZJTCity = null;
        try {
            listZJTCity = CitySite_FetchService.fetchAll(sessionId);//获取所有站点
            if(listZJTCity != null && listZJTCity.size() > 0){
                logger.info("造价通---信息价页面---爬取站点数量---"+listZJTCity.size()+"条");
                for(int i = 0 ;i<listZJTCity.size() ; i++){//循环站点
                    if(!getAreaCodeRecord(listZJTCity.get(i).getCode(),PageConstant.ZJT,PageConstant.ZJT_XXJ)){//验证区域重复
                        continue;
                    }


                    String[] industryArrCom = getIndustryComplate();//获取所有行业
                    String[] industryArr = getIndustry();//获取所有行业短字符串
                    if(industryArrCom.length > 0){
                        logger.info("造价通---信息价页面---获取固定行业---"+industryArrCom.length+"个");
                        for(int j = 0 ; j<industryArrCom.length ; j++){//循环行业
                            if(!getAreaCodeRecord(industryArrCom[j],PageConstant.ZJT,PageConstant.ZJT_XXJ)){//验证行业重复
                                continue;
                            }

                            String link = listZJTCity.get(i).getLink().concat(industryArrCom[j]);//拼接url
                            List<ZJT_CitySite_AreaDTO> listZJTCityArea = Xxj_FetchAreaService.fetchAll(sessionId,link);//获取所有城市
                            if(listZJTCityArea != null && listZJTCityArea.size() > 0){
                                logger.info("造价通---信息价页面---爬取---"+listZJTCity.get(i).getName()+"---站点下的城市---"+listZJTCityArea.size()+"个");
                                for(int k = 0 ; k<listZJTCityArea.size() ; k++){//循环城市
                                    if(!getCityCodeRecord(listZJTCityArea.get(k).getCode(),PageConstant.ZJT,PageConstant.ZJT_XXJ)){//验证行业重复
                                        continue;
                                    }


                                    List<String> listZJTPrice = Xxj_FetchService.fetchSearch_jcCrawler(sessionId, listZJTCity.get(i),listZJTCityArea.get(k),industryArr[j], num);//获取所有链接
                                    if (listZJTPrice != null && listZJTPrice.size() > 0) {
                                        logger.info("造价通---信息价页面---爬取" + listZJTCity.get(i).getName() + "站点下" + getIndustryName()[j] + "行业下的城市" + listZJTCityArea.get(k).getName() + "的Spu链接数量" + listZJTPrice.size() + "条");
                                        for (int l = 0; l < listZJTPrice.size(); l++) {//循环链接
                                            List<ZJT_XXJ_PriceDTO> listDetail = Xxj_FetchByIdService.fetchDetail(sessionId, listZJTPrice.get(l), listZJTCity.get(i).getCode(), listZJTCity.get(i).getName(), listZJTCityArea.get(k).getName());
                                            if(listDetail != null && listDetail.size() > 0){
                                                logger.info("造价通---信息价页面---当前链接爬取详情数据---"+listDetail.size()+"条");
                                                int count = 0;
                                                int countSucc = 0;
                                                for (int m = 0; m < listDetail.size(); m++) {//循环数据
                                                    if (flag) {
                                                        if (count > 10) {
                                                            logger.info("造价通---信息价页面---重复数据达到10条,退出当前循环");
                                                            break;
                                                        }
                                                    }
                                                    ZJT_XXJ_PriceDTO zjt_xxj_priceDTO = listDetail.get(m);
                                                    if (!StringUtil.isBlank(zjt_xxj_priceDTO.getPriceNtax())) {
                                                        String id = zjt_xxj_priceDTO.getCode() + zjt_xxj_priceDTO.getName() + zjt_xxj_priceDTO.getSpec() + zjt_xxj_priceDTO.getCity() + zjt_xxj_priceDTO.getPublishTime() + zjt_xxj_priceDTO.getPriceNtax();//编号+名称+规格+城市+发布时间
                                                        if (ENV.db().query(id, ZJT_XXJ_PriceDTO.class) == null) {
                                                            zjt_xxj_priceDTO.setId(id);
                                                            ENV.db().save(zjt_xxj_priceDTO);
                                                            countSucc++;
                                                            countTotal++;
                                                        } else {
                                                            count++;
                                                        }
                                                    } else {
                                                        count++;
                                                    }
                                                }
                                                logger.info("造价通---信息价页面---当前链接保存成功数据---"+countSucc+"条");
                                            }
                                        }
                                    }


                                    saveCityCodeRecord(listZJTCityArea.get(k).getCode(),PageConstant.ZJT,PageConstant.ZJT_XXJ);//保存行业记录
                                    if(k == listZJTCityArea.size()-1){//循环完整
                                        cityOver = true;
                                    }
                                }
                            }


                            saveAreaCodeRecord(industryArrCom[j],PageConstant.ZJT,PageConstant.ZJT_XXJ);//保存行业记录
                            if(j == industryArrCom.length-1){//循环完整
                                industryOver = true;
                            }
                        }
                    }


                    saveAreaCodeRecord(listZJTCity.get(i).getCode(),PageConstant.ZJT,PageConstant.ZJT_XXJ);//保存区域记录
                    if(i == listZJTCity.size()-1){//循环完整
                        areaOver = true;
                    }
                }
                logger.info("造价通---信息价页面---保存成功总数据---"+countTotal+"条");
            }
        } catch (IOException e) {
            logger.error("造价通信息价页面抓取站点失败",e);
            e.printStackTrace();
        }

        if(cityOver && industryOver && areaOver){
            ENV.db().updateBySQL("DELETE FROM T_PAGE_RECORD WHERE TYPE=", PageConstant.ZJT);
        }
    }

    public static void main(String[] args) {
        ENV.init();
        saveZJTXxj(1,true);
    }

}
