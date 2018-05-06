package com.rmd.wms.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.rmd.bms.entity.User;
import com.rmd.wms.bean.LogisticsFreightBill;
import com.rmd.wms.bean.vo.web.LogisFreiBillVo;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.service.LogisticsFreightBillService;
import com.rmd.wms.web.common.AbstractAjaxController;
import com.rmd.wms.web.util.DateJsonValueProcessor;
import com.rmd.wms.web.util.DateUtil;
import com.rmd.wms.web.util.DownloadUtils;
import com.rmd.wms.web.util.ExcelCommon;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 运费单控制器
 *
 * @author : liu
 * @Date : 2017/6/23
 */
@RequestMapping(value = "logisFreiBill")
@Controller
public class LogisticsFreightBillController extends AbstractAjaxController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogisticsFreightBillService logisticsFreightBillService;


    @RequestMapping(value = "jumpView", method = RequestMethod.GET)
    public ModelAndView jumpView(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("logistics/logisticsFreightBillManage");
    }

    /**
     * 查询列表
     *
     * @param request
     * @param response
     * @param page
     * @param rows
     * @param param
     * @return
     */
    @RequestMapping(value = "getLogisFreiBills", method = RequestMethod.POST)
    @ResponseBody
    public Object getLogisFreiBills(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                    @RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows,
                                    @ModelAttribute LogisFreiBillVo param) {
        JSONObject json = new JSONObject();
        if (param == null) {
            param = new LogisFreiBillVo();
        }
        param.setWareId(super.getCurrentWareId());
        try {
            PageBean<LogisticsFreightBill> data = logisticsFreightBillService.getLFBillsByPage(page, rows, param);
            json.put("rows", data.getList());
            json.put("total", data.getTotal());
            getSessionOfShiro().setAttribute(com.rmd.wms.web.constant.Constant.LOGIS_FREI_BILLS_PARAM, param);
        } catch (Exception e) {
            json.put("status", "500");
            json.put("message", "查询失败");
            logger.error(Constant.LINE + "查询异常", e);
        }
        return json;
    }

    /**
     * 修改额外费用
     *
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping(value = "/alterExtraCharges", method = RequestMethod.POST)
    @ResponseBody
    public Object alterExtraCharges(HttpServletRequest request, HttpServletResponse response, @ModelAttribute LogisticsFreightBill param) {
        JSONObject json = new JSONObject();
        if (param == null || param.getId() == null || param.getExtraCharges() == null) {
            json.put("status", "101");
            json.put("message", "参数错误");
            return json;
        }
        LogisticsFreightBill bill = logisticsFreightBillService.selectByPrimaryKey(param.getId());
        if (bill == null) {
            json.put("status", "102");
            json.put("message", "物流运费单不存在");
            return json;
        }
        if (bill.getDoChange() != null && Constant.TYPE_STATUS_YES == bill.getDoChange()) {
            json.put("status", "103");
            json.put("message", "该运费明细单的运费只允许修改一次！");
            return json;
        }
        try {
            User currentUser = super.getCurrentUser();
            param.setUpdateUserId(currentUser.getId());
            param.setUpdateUserName(super.getCurrentUserName());
            param.setUpdateTime(new Date());
            param.setDoChange(Constant.TYPE_STATUS_YES);
            logisticsFreightBillService.updateByPrimaryKeySelective(param);
            json.put("status", "200");
            json.put("message", "修改成功");
        } catch (Exception e) {
            json.put("status", "500");
            json.put("message", "修改失败");
            logger.error(Constant.LINE + "修改异常", e);
        }
        return json;
    }

    /**
     * 导出运费详情单excel
     *
     * @param request
     * @param response
     * @param columns
     */
    @RequestMapping(value = "/lfBillExport", method = RequestMethod.POST)
    public void lfBillExport(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "columns", required = true) String columns) {
        JSONArray columnJarr = JSONArray.fromObject(columns);
        LogisFreiBillVo param = (LogisFreiBillVo) getSessionOfShiro().getAttribute(com.rmd.wms.web.constant.Constant.LOGIS_FREI_BILLS_PARAM);
        List<LogisticsFreightBill> data = logisticsFreightBillService.getLFBillsByCriteria(param);
        String name = com.rmd.wms.web.constant.Constant.LF_BILL_EXPORT_FILE_NAME + DateUtil.format(new Date(), "yyyyMMdd");
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss", "Date"));
        JSONArray dataJsonArray = JSONArray.fromObject(data, config);
        JSONArray resultArray = new JSONArray();
        net.sf.json.JSONObject json;
        int size = dataJsonArray.size();
        for (int i = 0; i < size; i++) {
            json = dataJsonArray.getJSONObject(i);
            json.put("orderNum", i + 1);
            // 处理运费价格
            if (json.get("extraCharges") != null && new BigDecimal(String.valueOf(json.get("extraCharges"))).compareTo(BigDecimal.ZERO) > 0) {
                json.put("alterPrice", new BigDecimal(json.get("basePrice").toString()).add(new BigDecimal(json.get("extraCharges").toString())));
            } else {
                json.put("extraCharges", "");
                json.put("alterPrice", "");
            }
            resultArray.add(json);
        }
        try {
            ExcelCommon excelUtil = new ExcelCommon();
            File excel = excelUtil.dataToExcel(columnJarr, resultArray, name, request);
            DownloadUtils.download(request, response, excel, name + ".xls");
        } catch (Exception e) {
            logger.error(Constant.LINE + "数据导出失败", e);
        }
    }


}
