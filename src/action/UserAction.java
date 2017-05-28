package action;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Power;
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
	public static final String WATERING="3";
	private String phone;
	private Long user1id;
	private File mPhoto;
	private String name;
	private Long user2id;
	private String powertype;
	private Long userid;

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getPowertype() {
		return powertype;
	}

	public void setPowertype(String powertype) {
		this.powertype = powertype;
	}

    public Long getUser2id() {
        return user2id;
    }

    public void setUser2id(Long user2id) {
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

    public Long getUser1id() {
        return user1id;
    }

    public void setUser1id(Long user1id) {
        this.user1id = user1id;
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
	 * 偷能量,或者是浇水
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
		User u1=session.get(User.class,new Long(getUser1id()));
		//主偷用户
		User u2=session.get(User.class,new Long(getUser2id()));
        //被偷用户
		Power p1=session.get(Power.class,new Long(getUser1id()));
        //主偷用户
        Power p2=session.get(Power.class,new Long(getUser2id()));

		if(getPowertype().equals(TOMATO_POWER)){	//如果偷的是番茄能量
            int u1p=p1.getPower1Yesterday();
            int u1ps=p1.getPower1Stolen();
            double sum=u1p+u1ps;
            if(u1p/sum<=0.3){//剩余太少，不能偷

            }else{
                //从剩余的能量中减去总能量的30%之后再乘0.5，获取一个随机值
                int randNum =(int)(Math.random() * (( u1p-sum*0.3)*0.5 + 1));
                //已被偷的能量加上
                p1.setPower1Stolen(p1.getPower1Stolen()+randNum);
                //剩余能量减去
                p1.setPower1Yesterday(p1.getPower1Yesterday()-randNum);
                p2.setPower(p2.getPower()+randNum);
                session.update(p1);
                session.update(p2);

            }
		}else if(getPowertype().equals(CUSTOM_POWER)){//如果偷的是习惯能量
            int u1p=p1.getPower2Yesterday();
            int u1ps=p1.getPower2Stolen();
            double sum=u1p+u1ps;
            if(u1p/sum<=0.3){//剩余太少，不能偷

            }else{
                //从剩余的能量中减去总能量的30%之后再乘0.5，获取一个随机值
                int randNum =(int)(Math.random() * (( u1p-sum*0.3)*0.5 + 1));
                //已被偷的能量加上
                p1.setPower2Stolen(p1.getPower2Stolen()+randNum);
                //剩余能量减去
                p1.setPower2Yesterday(p1.getPower2Yesterday()-randNum);
                p2.setPower(p2.getPower()+randNum);
                session.update(p1);
                session.update(p2);

            }
		}else if(getPowertype().equals(WATERING)){//如果是浇水

		}
        session.getTransaction().commit();
		session.close();
	 	return null;
	}
}
