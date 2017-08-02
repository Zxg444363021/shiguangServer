package action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ResponseBean;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;

import entity.User;
import util.HibernateUtil;


public class LoginAction extends ActionSupport {
	private String phone;
	private String password;
	private  String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	private String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 查询数据库中是否有帐号
	 * @param phone
	 * @return 帐号的密码或null
	 * @throws IOException
	 */
	public User checkAccount(String phone) throws IOException{
		User result=null;
		//创建会话对象
		Session session=HibernateUtil.getSessionFactory().openSession();
		//开始一个事物
		session.beginTransaction();
		//查询手机号
		String query_str="from User s where s.phone=:phone";
		Query<User> query=session.createQuery(query_str);
		query.setParameter("phone", getPhone());
		List<User> list=query.getResultList();
		if(list.size()!=0){//如果找到了就返回一个1
			result=list.get(0);
		}else{//如果没找到就返回一个0
			result=null;
		}
		
		session.getTransaction().commit();//提交事务
		//这里不能把回话工厂关了，否则后面regist就无法操作数据库
		return result;
	}
	public String login() throws IOException{
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8"); 
		request.getAttribute("phone");
		request.getAttribute("password");
		
		Session session=HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		PrintWriter writer=response.getWriter(); 
		User user=checkAccount(getPhone());
		ResponseBean responseBean=new ResponseBean();
		response.setStatus(200);
		if(user!=null){
			if(user.getPassword().equals(getPassword())){
				user.setPassword(null);
				responseBean.setCode(ResponseBean.LOGIN_SUCC);
				responseBean.setMessage("200");
				responseBean.setUser(user);
				Gson gson=new Gson();
				String result=gson.toJson(responseBean);
				writer.write(result);
				System.out.println("login success");
			}else{
				responseBean.setCode(ResponseBean.LOGIN_FAIL);
				responseBean.setMessage("201");
				Gson gson=new Gson();
				String result=gson.toJson(responseBean);
				writer.write(result);
				System.out.println("login fail201");
			}
		}else{
			responseBean.setCode(ResponseBean.LOGIN_FAIL);
			responseBean.setMessage("202");
			Gson gson=new Gson();
			String result=gson.toJson(responseBean);
			writer.write(result);
			System.out.println("login fail202");
		}
		writer.flush();
		writer.close();
		session.getTransaction().commit();
		session.close();
		return null;
	}
}
