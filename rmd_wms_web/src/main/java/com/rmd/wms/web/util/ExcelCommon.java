package com.rmd.wms.web.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 
* @Description: 该类用于各种表格的导出Excel
* @author wangyf
 */
public class ExcelCommon {

	
	/**
	 * 将传入的数据转为excel<br/>
	 * @param column 		数据列名  含有columnViewData：XX和columnViewField：XX的集合
	 * @param data   		数据内容  前台显示的表格data数据
	 * @param excelTitle	excel标题名称
	 * 
	 */
	public File dataToExcel(JSONArray column,JSONArray data,String excelTitle,HttpServletRequest request) throws Exception{
		
		ArrayList<ArrayList<String>> allData = getExcelData(column, data);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		sheet.autoSizeColumn(1);
//		//创建标题
//		HSSFRow rowTitle = sheet.createRow(0);
//		HSSFCell cellTitle = rowTitle.createCell(0);
//		//标题栏合并
//		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,  allData.get(0).size()-1));
//		//标题居中
//		cellTitle.setCellStyle(getTitleCellStyle(workbook));
//		cellTitle.setCellValue(excelTitle);

		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		//用户数据
		for(int i=0;i<allData.size();i++){
			HSSFRow row = sheet.createRow(i);
			ArrayList<String> rowData = allData.get(i);
			for(int j=0;j<rowData.size();j++){
				HSSFCell cell = row.createCell(j);
				cell.setCellValue(rowData.get(j));
				cell.setCellStyle(style);
			}
		}
		
		String excelTempDir = request.getSession().getServletContext().getRealPath("/")+"temp/";
		File tempFile = new File(excelTempDir);
		if(!tempFile.exists()){
			tempFile.mkdir();
		}
		excelTempDir += System.currentTimeMillis()+".xls";
		File excelFile = new File(excelTempDir);
		FileOutputStream fos = new FileOutputStream(excelFile);
		workbook.write(fos);
		fos.close();
		return excelFile;
	}
	
	/**
	 * 将传入的档案数据转为excel<br/>
	 * @param column 		数据列名
	 * @param data   		数据内容
	 * @param excelTitle	excel标题名称
	 * 
	 */
	public HSSFWorkbook archiveDataToExcel(JSONArray column,JSONArray data,String excelTitle,HttpServletRequest request) throws Exception{
		
		ArrayList<ArrayList<String>> allData = getExcelData(column, data);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		//创建标题
		HSSFRow rowTitle = sheet.createRow(0);
		HSSFCell cellTitle = rowTitle.createCell(0);
		//标题栏合并
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,  allData.get(0).size()-1));
		//标题居中
		cellTitle.setCellStyle(getTitleCellStyle(workbook));
		cellTitle.setCellValue(excelTitle);
		
		//用户数据
		for(int i=0;i<allData.size();i++){
			HSSFRow row = sheet.createRow(i+1);
			ArrayList<String> rowData = allData.get(i);
			for(int j=0;j<rowData.size();j++){
				HSSFCell cell = row.createCell(j);
				if(i != 0){
					if(j == 0){
//						cell.setCellStyle(getTitleCellStyle(workbook));
						cell.setCellValue(i);
					}else{
						cell.setCellValue(rowData.get(j));
					}
				}else{
					cell.setCellValue(rowData.get(j));
				}
			}
		}

		return workbook;
	}
	
	/**
	 * 通过 查询到的结果转化导出EXCEL 需要的数据集合
	 * @param 	column	 通过getXXXColumnView方法得到的集合
	 * @param 	data	通过getXXXData方法得到的集合	    
	 */
	private ArrayList<ArrayList<String>> getExcelData(JSONArray column,JSONArray data){
		//字段名称
		ArrayList<String> columnName = new ArrayList<String>();
		//数据库字段名
		ArrayList<String> columnDbName = new ArrayList<String>();
		//需要导出的数据集合
		ArrayList<ArrayList<String>> allData = new ArrayList<ArrayList<String>>();
		
		
		JSONArray columnJsonArray = column;
		if(null != columnJsonArray){
			for(int i=0;i<columnJsonArray.size();i++){
				JSONObject obj = columnJsonArray.getJSONObject(i);
				if("false".equals(obj.getString("hidden"))){
					columnDbName.add(obj.getString("dataIndex"));
					columnName.add(obj.getString("text"));
				}
			}
		}
		
		allData.add(columnName);
		
		for(int j=0;j<data.size();j++){
			JSONObject dataJsonObject = data.getJSONObject(j);
			//每行数据内容
			ArrayList<String> rowData = new ArrayList<String>();
			for(int k=0;k<columnDbName.size();k++){
				if(null!=columnDbName.get(k)&&!"null".equals(dataJsonObject.getString(columnDbName.get(k)))){
					rowData.add(dataJsonObject.getString(columnDbName.get(k)));
				}else{
					rowData.add("");
				}
				
			}
			allData.add(rowData);
		}
		
		return allData;
	}
	
	private HSSFCellStyle getTitleCellStyle(HSSFWorkbook workbook){
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return cellStyle;
	}

	// 判断sheet是否为空
	public static String queryExcelisNull(Sheet sheet, int row, int colnum) {
		Row excel_row = sheet.getRow(row);
		if (excel_row == null)
			return "";
		Cell cell = excel_row.getCell(colnum);
		if (cell == null)
			return "";
		if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK)
			return "";
		else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
			return cell.getNumericCellValue() + "";
		else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
			return cell.getStringCellValue().toString();
		else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
			String value;
			try {
				value = cell.getRichStringCellValue().getString();
				return value;
			} catch (Exception e) {
				value = cell.getNumericCellValue() + "";
				return value;
			}
		}
		return null;
	}

	public static String getCallValue(Cell cell) {
		if (null == cell) {
			return null;
		}
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC: // 数字
				return cell.getNumericCellValue() + "";
			case Cell.CELL_TYPE_STRING: // 字符串
				return cell.getStringCellValue() + "";
			case Cell.CELL_TYPE_BOOLEAN: // Boolean
				return cell.getBooleanCellValue() + "";
			case Cell.CELL_TYPE_FORMULA: // 公式
				return cell.getCellFormula() + "";
			case Cell.CELL_TYPE_BLANK: // 空值
				return null;
			case Cell.CELL_TYPE_ERROR: // 故障
				return null;
			default:
				return null;
		}
	}
}
