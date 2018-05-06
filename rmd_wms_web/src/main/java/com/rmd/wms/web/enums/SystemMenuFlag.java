package com.rmd.wms.web.enums;

/**
 * 菜单权限标识
 * Created by wangyf on 2017/5/3.
 */
public enum SystemMenuFlag {

    rmd_wms_system_manage_level_1("系统设置"),rmd_wms_basic_manage_warehouse_level_2("仓库管理"),rmd_wms_system_manage_role_level_2("角色管理"),rmd_wms_system_manage_message_level_2("账户信息");


    public String getInfo() {
        return info;
    }

    private final String info;

    private SystemMenuFlag(String info) {
        this.info=info;
    }
}
