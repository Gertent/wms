package com.rmd.wms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.rmd.wms.bean.GroundingBill;
import com.rmd.wms.bean.vo.web.PageBean;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.GroundingBillMapper;
import com.rmd.wms.service.GroundingBillService;
/**
 * 
* @ClassName: GroundingBillServiceImpl 
* @Description: TODO(上架表接口实现) 
* @author ZXLEI
* @date Feb 22, 2017 11:41:04 AM 
*
 */
@Service("groundingBillService")
public class GroundingBillServiceImpl extends BaseService implements GroundingBillService{

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return this.getMapper(GroundingBillMapper.class).deleteByPrimaryKey(id);
	}

	@Override
	public int insert(GroundingBill record) {
		return this.getMapper(GroundingBillMapper.class).insert(record);
	}

	@Override
	public int insertSelective(GroundingBill record) {
		return this.getMapper(GroundingBillMapper.class).insertSelective(record);
	}

	@Override
	public GroundingBill selectByPrimaryKey(Integer id) {
		return this.getMapper(GroundingBillMapper.class).selectByPrimaryKey(id);
	}

	@Override
	public GroundingBill selectByInStockNo(String inStockNo) {
		return this.getMapper(GroundingBillMapper.class).selectByInStockNo(inStockNo);
	}

	@Override
	public int updateByPrimaryKeySelective(GroundingBill record) {
		return this.getMapper(GroundingBillMapper.class).updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(GroundingBill record) {
		return this.getMapper(GroundingBillMapper.class).updateByPrimaryKey(record);
	}


	@Override
	public GroundingBill selectByGroundingNo(String groundingNo) {
		return this.getMapper(GroundingBillMapper.class).selectByGroundingNo(groundingNo);
	}

	@Override
	public List<GroundingBill> ListGroundingBills(Map<String, Object> parmaMap) {
		return this.getMapper(GroundingBillMapper.class).selectAllByWhere(parmaMap);
	}

	@Override
	public int insertBatch(List<GroundingBill> list) {
	   
	    return this.getMapper(GroundingBillMapper.class).insertBatch(list);
	}

    @Override
    public List<Map<String, Object>> listAllByParmMap(Map<String, Object> parmaMap) {
        return this.getMapper(GroundingBillMapper.class).selectAllByParmMap(parmaMap);
    }

    @Override
	public PageBean<GroundingBill> ListGroundingBills(Integer page,
		Integer rows, Map<String, Object> parmaMap) {
	    PageHelper.startPage(page,rows);
	    return new PageBean<GroundingBill>(this.getMapper(GroundingBillMapper.class).selectAllByWhere(parmaMap));
	}
}
