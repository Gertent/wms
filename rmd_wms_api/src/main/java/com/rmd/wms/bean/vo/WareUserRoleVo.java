package com.rmd.wms.bean.vo;



import java.io.Serializable;
import java.util.Date;

/**
 * 仓库人员
 */
public class WareUserRoleVo implements Serializable {

    private static final long serialVersionUID = -6113274712895969702L;

    private Integer id;
    private String loginname;
    private String rolename;
    private String password;
    private String realname;
    private String sex;
    private Date birthday;
    private String tel;
    private String mobile;
    private String position;
    private String email;
    private Integer status;
    private String note;
    private Integer deptid;
    private String deptName;
    private String deptNames;
    private String jobNum;
	private Integer isWareUser;
    

	public Integer getId()
    {
      return this.id;
    }
    
    public void setId(Integer id)
    {
      this.id = id;
    }
    
    public String getLoginname()
    {
      return this.loginname;
    }
    
    public void setLoginname(String loginname)
    {
      this.loginname = loginname;
    }
    
    public String getRolename()
    {
      return this.rolename;
    }
    
    public void setRolename(String rolename)
    {
      this.rolename = rolename;
    }
    
    public String getPassword()
    {
      return this.password;
    }
    
    public void setPassword(String password)
    {
      this.password = password;
    }
    
    public String getSex()
    {
      return this.sex;
    }
    
    public void setSex(String sex)
    {
      this.sex = sex;
    }
    
    public Date getBirthday()
    {
      return this.birthday;
    }
    
    public void setBirthday(Date birthday)
    {
      this.birthday = birthday;
    }
    
    public String getTel()
    {
      return this.tel;
    }
    
    public void setTel(String tel)
    {
      this.tel = tel;
    }
    
    public String getMobile()
    {
      return this.mobile;
    }
    
    public void setMobile(String mobile)
    {
      this.mobile = mobile;
    }
    
    public String getPosition()
    {
      return this.position;
    }
    
    public void setPosition(String position)
    {
      this.position = position;
    }
    
    public String getEmail()
    {
      return this.email;
    }
    
    public void setEmail(String email)
    {
      this.email = email;
    }
    
    public Integer getStatus()
    {
      return this.status;
    }
    
    public void setStatus(Integer status)
    {
      this.status = status;
    }
    
    public String getNote()
    {
      return this.note;
    }
    
    public void setNote(String note)
    {
      this.note = note;
    }
    
    public Integer getDeptid()
    {
      return this.deptid;
    }
    
    public void setDeptid(Integer deptid)
    {
      this.deptid = deptid;
    }
    
    public String getDeptName()
    {
      return this.deptName;
    }
    
    public void setDeptName(String deptName)
    {
      this.deptName = deptName;
    }
    
    public String getDeptNames()
    {
      return this.deptNames;
    }
    
    public void setDeptNames(String deptNames)
    {
      this.deptNames = deptNames;
    }
    
    public String getRealname()
    {
      return this.realname;
    }
    
    public void setRealname(String realname)
    {
      this.realname = realname;
    }
    
    public String getJobNum() {
		return jobNum;
	}

	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}
    
    public Integer getIsWareUser() {
		return isWareUser;
	}

	public void setIsWareUser(Integer isWareUser) {
		this.isWareUser = isWareUser;
	}
    
}
