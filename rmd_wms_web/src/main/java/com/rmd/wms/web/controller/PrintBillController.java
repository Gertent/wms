package com.rmd.wms.web.controller;

import com.rmd.oms.service.OrderBaseService;
import com.rmd.wms.bean.OrderLogisticsInfo;
import com.rmd.wms.bean.StockOutBill;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.vo.web.LogisticsBillPrint;
import com.rmd.wms.constant.Constant;
import com.rmd.wms.service.*;
import com.rmd.wms.web.common.AbstractAjaxController;
import com.rmd.wms.web.util.PdfGenerate;
import com.rmd.wms.web.util.PropertiesLoader;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 
* @Description: 生成pdf文件进行预览打印
* @author wangyf
*
 */
@Controller
@RequestMapping(value = "/printpdf")
public class PrintBillController extends AbstractAjaxController {

	private Logger logger = Logger.getLogger(PrintBillController.class);
     
    //出库单
	@Resource(name = "stockOutBillService") 
	private StockOutBillService stockOutBillService;

	//出库单物流信息
	@Resource(name = "logisticsBillService") 
	private LogisticsBillService logisticsBillService;
	
	//出库单收货人信息
	@Resource(name="orderLogisticsInfoService") 
	private OrderLogisticsInfoService orderLogisticsInfoService;
	
	//出库单商品
	@Resource(name="stockOutInfoService")
	private StockOutInfoService stockOutInfoService;
	
	//出库单商品货位锁定
	@Resource(name="locationGoodsBindOutService") 
	private LocationGoodsBindOutService locationGoodsBindOutService;
	
	//商品货位绑定
	@Resource(name="locationGoodsBindService") 
	private LocationGoodsBindService locationGoodsBindService;
	
	//承运商
	@Resource(name = "logisticsCompanyService")
	private LogisticsCompanyService logisticsCompanyService; // 物流公司
	
	//OMS订单服务
	@Resource(name = "orderBaseService")
	private OrderBaseService orderBaseService;
	//发货单
	@Resource(name = "deliveryBillService")
	private DeliveryBillService deliveryBillService;
	//仓库
	@Resource(name = "warehouseService")
	private WarehouseService warehouseService;
	
	@Autowired
	private HttpServletRequest request;
	
	protected static PropertiesLoader propertiesLoader =
            new PropertiesLoader("classpath:/application.properties");
	
	
	/**
	 * 运单批量界面模板
	 * @param ids
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/logicBillPrint",method=RequestMethod.GET)
	public ModelAndView logicBillPrint(@RequestParam(value="ids") String ids ,Model model){
		ModelAndView mv = new ModelAndView("outstock/logisticsPdf");
		List<LogisticsBillPrint> logisticsBillPrints=new ArrayList<LogisticsBillPrint>(10);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StockOutBill stockOutBill=new StockOutBill();  //出库单
			String[] IdArray = ids.split(",");
			if (IdArray.length>0) {
				for (String dId : IdArray) {
					LogisticsBillPrint logisticsBillPrint=new LogisticsBillPrint();
					//出库单
					stockOutBill=stockOutBillService.selectByPrimaryKey(Integer.valueOf(dId));
					if (stockOutBill!=null) {
						//查询收货人信息
						OrderLogisticsInfo orderLogisticsInfo=orderLogisticsInfoService.selectByOrderNo(stockOutBill.getOrderNo());
						//订单详情
						//OrderPackageInfoVo orderPackageInfoVo=  orderBaseService.selectOrderPackageInfo(stockOutBill.getOrderNo());
						//仓库信息
						Warehouse warehouse= warehouseService.selectByPrimaryKey(stockOutBill.getWareId());
						
						//寄件人信息
						if(warehouse!=null){
							logisticsBillPrint.setSenderFromName(warehouse.getContactName()); 		//寄件人姓名
							logisticsBillPrint.setSenderAddr(warehouse.getAddress()); 				//寄件地址
							logisticsBillPrint.setSenderMobile(warehouse.getContactTel()); 			//电话
							logisticsBillPrint.setSenderCompanyName("猎鹰全球国际电子商务有限公司");
						}
						//收件人信息
						if(orderLogisticsInfo!=null){
							logisticsBillPrint.setToName(orderLogisticsInfo.getReceivername());		//收件人姓名
							logisticsBillPrint.setToProvince(orderLogisticsInfo.getProvName());		//省
							logisticsBillPrint.setToCity(orderLogisticsInfo.getCityName());			//市
							//logisticsBillPrint.setToDistrict(orderPackageInfoVo.getOrderReceiveAddress().getArea());//县/区
							logisticsBillPrint.setToAddr(orderLogisticsInfo.getDetailedAddress());	//详细地址
							logisticsBillPrint.setToMobile(orderLogisticsInfo.getReceiveMobile());	//手机
							logisticsBillPrint.setToTel(orderLogisticsInfo.getReceiveTel());		//电话		
						}
						//物品信息
						logisticsBillPrint.setQuanlity(stockOutBill.getGoodsAmount());			//数量
						logisticsBillPrint.setWeight(new BigDecimal(stockOutBill.getWeight() == null ? BigDecimal.ZERO.toString() : stockOutBill.getWeight().toString()));    //重量
						//费用
						
					}
				
					logisticsBillPrints.add(logisticsBillPrint);
					mv.addObject("logisticsBillPrints", logisticsBillPrints);
				}
			
			}
		} catch (Exception e) {
			logger.error(Constant.LINE + "运单批量界面模板", e);
		}
	    return mv;
	}	
	
    /**
     * 生成pdf文件，并进入pdf预览界面
     * */
    @RequestMapping("/logicGeneratePdf")
	public ModelAndView logicGeneratePdf(HttpServletRequest request){
    	 ModelAndView mv = new ModelAndView("outstock/viewPdf");
        String ids=request.getParameter("ids");
        String pdfName=UUID.randomUUID().toString();
	    try {
	    	String path = request.getContextPath();
	    	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
	    	String htmlPath = basePath+"/printpdf/logicBillPrint?ids="+ids;//propertiesLoader.getProperty("statistic.url") + "contractPdfTemplate?orderId="+acOrder.getId();
	        String diskFolderpath = propertiesLoader.getProperty("pdf.template.path");
	        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd/HH");   
	        /**构建图片保存的目录**/ 
	        String logoRealPathDir = diskFolderpath; //+ dateformat.format(new Date())
	        /**根据真实路径创建目录**/   
	        File logoSaveFile = new File(logoRealPathDir); 
	        if(!logoSaveFile.exists()) logoSaveFile.mkdirs(); 
	        /**拼成完整的文件保存路径加文件**/   	        
	        String pdfPath = logoRealPathDir + File.separator + pdfName+".pdf";
	    	 String[] args = new String[3];
	        args[0] = htmlPath;
	        args[1] = pdfPath;
	        args[2] = "1.2";
	        PdfGenerate.generatePdf(args);
	    } catch (Exception e) {
	    	logger.error(Constant.LINE + "生成pdf文件失败", e);
	    }
	    mv.addObject("pdfName", pdfName);
	    return mv;
	}
   
    /**
     * pdf预览
     * */
    @RequestMapping("/displayPDF")
    public void displayPDF(HttpServletResponse response) {
    	String pdfName=request.getParameter("pdfName");
        try {
        	String diskFolderpath = propertiesLoader.getProperty("pdf.template.path");
        	String pdfPath=diskFolderpath+pdfName+".pdf";
            File file = new File(pdfPath);
            FileInputStream fileInputStream = new FileInputStream(file);
            response.setHeader("Content-Disposition", "attachment;fileName=test.pdf");
            response.setContentType("multipart/form-data");
            OutputStream outputStream = response.getOutputStream();
            IOUtils.write(IOUtils.toByteArray(fileInputStream), outputStream);
        } catch(Exception e) {
        	logger.error(Constant.LINE + "pdf文件预览失败", e);
        }
    }    
    
}
