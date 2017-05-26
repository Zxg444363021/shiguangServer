package action;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;

import entity.User;
import util.HibernateUtil;

public class UserAction extends ActionSupport {
	private static final String BASEURL="http://121.42.140.71:8080/shiguangServer/";
	private String phone;
	private Long userid;
	private File mPhoto;
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
		 ServletInputStream is=request.getInputStream();
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
}
