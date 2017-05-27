package action;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;

import entity.User;
import util.HibernateUtil;

public class UserAction extends ActionSupport {
	private static final String BASEURL="http://121.42.140.71:8080/shiguangServer/";
	public static final String TOMATO_POWER="1";
	public static final String CUSTOM_POWER="2";
	private String phone;
	private Long userid;
	private File mPhoto;
	private String name;
	private String user2id;
	private String powertype;

	public String getPowertype() {
		return powertype;
	}

	public void setPowertype(String powertype) {
		this.powertype = powertype;
	}

	public String getUser2id() {
		return user2id;
	}

	public void setUser2id(String user2id) {
		this.user2id = user2id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public File getmPhoto() {
		return mPhoto;
	}
	public void setmPhoto(File mPhoto) {
		this.mPhoto = mPhoto;
	}
	/**
	 * 上传头像
	 * @return
	 * @throws IOException
	 */
	public String doPostIcon() throws IOException{
		 HttpServletRequest request=ServletActionContext.getRequest();
		 HttpServletResponse response=ServletActionContext.getResponse();
		 response.setCharacterEncoding("UTF-8");  
		 response.setContentType("application/json; charset=utf-8"); 
		 request.getAttribute("phone");
		 System.out.println("sessionId="+request.getSession().getId());
		 String dir=ServletActionContext.getServletContext().getRealPath("files");//存储的位置，再看看
		 String filename=getPhone()+".jpg";
		 File file=new File(dir,filename);//存储的图片名
		 FileUtils.copyFile(mPhoto, file);
		Session session=HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		User u=session.get(User.class,new Long(getUserid()));
		u.setIcon(BASEURL+"files/"+filename);
		session.update(u);
		session.getTransaction().commit();
		response.setStatus(200);
		return null;
	 }

	/**
	 * 	修改用户昵称
	 * @return
	 */
	 public String doUpdateInfo(){
		 HttpServletRequest request=ServletActionContext.getRequest();
		 HttpServletResponse response=ServletActionContext.getResponse();
		 response.setCharacterEncoding("UTF-8");
		 response.setContentType("application/json; charset=utf-8");
		 request.getAttribute("userid");
		 request.getAttribute("name");
		 Session session=HibernateUtil.getSessionFactory().openSession();
		 session.beginTransaction();
		 User u=session.get(User.class,new Long(getUserid()));
		 u.setName(getName());
		 try{
			 session.update(u);
			 response.setStatus(200);
		 }catch (HibernateException e){
			 response.setStatus(201);
		 }
		 session.getTransaction().commit();
		 session.close();
		 return null;
	 }

	/**
	 * 偷能量
	 * @return
	 */
	public String doStealPower(){
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		request.getAttribute("userid");
		request.getAttribute("user2id");
		request.getAttribute("powertype");

		Session session=HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		//被偷用户
		User u1=session.get(User.class,new Long(getUserid()));
		//主偷用户
		User u2=session.get(User.class,new Long(getUser2id()));
		int u2p=u2.getPower();
		if(getPowertype().equals(TOMATO_POWER)){	//如果偷的是番茄能量


		}else if(getPowertype().equals(CUSTOM_POWER)){//如果偷的是习惯能量

		}
		int randNum =100000+(int)(Math.random() * ((999999 - 100000) + 1));
	 	return null;
	}
}
