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
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZXLEI
 * @ClassName: LoginController
 * @Description: TODO(登陆，加载菜单)
 * @date Feb 20, 2017 2:20:04 PM
 */
@Controller
public class LoginController extends AbstractAjaxController {

    private Logger logger = Logger.getLogger(LoginController.class);

    @Resource(name = "warehouseService")
    private WarehouseService warehouseService;
    @Autowired
	private WebApplicationService webApplicationService;
	@Autowired
	private ApplicationMenuService applicationMenuService;
    @Autowired
    private WarehouseUserService warehouseUserService;

    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
    public ModelAndView checkLogin( @RequestParam(value = "wareId", required = false) Integer wareId, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            String acountUrlInfo="";
            User user = super.getCurrentUser();
            modelAndView.setViewName("index");
            modelAndView.addObject("user",user);
    		WebApplication app = webApplicationService.selectByAppNo(user.getAppNumber());
    		user.setSystemId(app.getId());
    		List<ApplicationMenu> modlist = applicationMenuService.selectMenuList(user.getId(), app.getId());
            for(ApplicationMenu o:modlist){
                if(SystemMenuFlag.rmd_wms_system_manage_message_level_2.name().equals(o.getMenuCode())){
                    acountUrlInfo=o.getMenuUrl();
                }
            }
            modelAndView.addObject("acountUrlInfo", acountUrlInfo);
            if(wareId!=null&&wareId>0) {
                Warehouse ware = warehouseService.selectByPrimaryKey(wareId);
                super.setCurrentWare(ware);
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
                modelAndView.addObject("warehouseList", warehouses);
                modelAndView.addObject("wareId", wareId);
                modelAndView.addObject("modlist", modlist);
            }else{//如果没选择仓库，只显示系统设置菜单
                List<ApplicationMenu> list=new ArrayList<>();
                int sysMenuId=0;
                for(ApplicationMenu o:modlist){
                    if(SystemMenuFlag.rmd_wms_system_manage_level_1.name().equals(o.getMenuCode())){//系统设置一级菜单
                        sysMenuId=o.getId();
                        list.add(o);
                        break;
                    }
                }
                if(sysMenuId>0){
                    for(ApplicationMenu o:modlist){
                        if(sysMenuId==o.getParentid()){//系统设置子菜单
                            list.add(o);
                        }
                    }
                }
                modelAndView.addObject("modlist", list);
            }
        } catch (Exception e) {
            logger.error("checkLogin：登录异常！"+ com.rmd.wms.constant.Constant.LINE,e);
        }
        return modelAndView;
    }

    /**
     * @param pageNo
     * @param request
     * @param modelmap
     * @return
     */
    @RequestMapping("/indexHome")
    public String indexHome(Integer pageNo, HttpServletRequest request, ModelMap modelmap) {
        return "/iframeHome";
    }

    @RequestMapping("/indexWareChage")
    @ResponseBody
    public String wareChange(@RequestParam(value = "wareId", required = false) String wareId,HttpServletRequest request) {
    	String result="false";
        if(StringUtils.isNotEmpty(wareId)){
            Warehouse warehouse = warehouseService.selectByPrimaryKey(Integer.valueOf(wareId));
            super.setCurrentWare(warehouse);
        	result="true";
        }
        return result;
    }

}
