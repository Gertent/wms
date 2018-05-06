package com.rmd.wms.web.controller;

import com.rmd.bms.entity.ApplicationMenu;
import com.rmd.bms.entity.User;
import com.rmd.bms.entity.WebApplication;
import com.rmd.bms.service.ApplicationMenuService;
import com.rmd.bms.service.WebApplicationService;
import com.rmd.wms.bean.Warehouse;
import com.rmd.wms.bean.WarehouseUser;
import com.rmd.wms.bean.vo.app.MoveInUsersParam;
import com.rmd.wms.enums.WarehouseStatus;
import com.rmd.wms.service.WarehouseService;
import com.rmd.wms.service.WarehouseUserService;
import com.rmd.wms.web.common.AbstractAjaxController;
import com.rmd.wms.web.enums.SystemMenuFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 加载主页信息
 * @author zuoguodong
 */
@Controller
public class IndexController extends AbstractAjaxController {

	@Autowired
	private WebApplicationService webApplicationService;
	@Resource(name = "warehouseService")
    private WarehouseService warehouseService;
	@Autowired
	private ApplicationMenuService applicationMenuService;
	@Autowired
	private WarehouseUserService warehouseUserService;

	/**
	 * 初始化主页信息(先进入选择仓库的界面，选择仓库后，在进入首页)
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/index")
	public ModelAndView index(Model model, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("login");
		User user = super.getCurrentUser();
		WebApplication app = webApplicationService.selectByAppNo(user.getAppNumber());
		user.setSystemId(app.getId());
		modelAndView.addObject("userName", user.getLoginname());

		String authorityFlag="0";//
		List<ApplicationMenu> modlist = applicationMenuService.selectMenuList(user.getId(), app.getId());
		for(ApplicationMenu o:modlist){
			if(SystemMenuFlag.rmd_wms_basic_manage_warehouse_level_2.name().equals(o.getMenuCode())||SystemMenuFlag.rmd_wms_system_manage_role_level_2.name().equals(o.getMenuCode())){//是否有仓库管理、角色管理权限
				authorityFlag="1";
			}
		}
		//获取当前用户对应仓库
		MoveInUsersParam moveInUsersParam=new MoveInUsersParam();
		List<Integer> userList=new ArrayList();
		userList.add(user.getId());
		moveInUsersParam.setUserIds(userList);
		List<WarehouseUser> warehouseUserList=warehouseUserService.selectByWareIdAndUserIds(moveInUsersParam);
		List<Warehouse> warehouseList= warehouseService.selectByCriteria(new Warehouse());
		List<Warehouse> warehouses=new ArrayList<>();
		for(Warehouse warehouse:warehouseList){
			if(warehouse.getStatus()== WarehouseStatus.A001.getValue()||warehouse.getStatus()==WarehouseStatus.A002.getValue()){
				for(WarehouseUser warehouseUser:warehouseUserList){
					if(warehouse.getId()==warehouseUser.getWareId()){
						warehouses.add(warehouse);
						break;
					}
				}
			}
		}

        modelAndView.addObject("warehouseList",warehouses);
		modelAndView.addObject("authorityFlag",authorityFlag);
		return modelAndView;
	}
	
	@RequestMapping(value = "/error")
	public ModelAndView error(Model model, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("error");
		return modelAndView;
	}
}
