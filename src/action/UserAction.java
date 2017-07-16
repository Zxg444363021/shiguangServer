package action;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import entity.AlternateRecord;
import entity.Power;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;

import entity.User;
import org.hibernate.query.Query;
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
	public String doStealPower() throws IOException {
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

        String result="";

		if(getPowertype().equals(TOMATO_POWER)){	//如果偷的是番茄能量
            int u1p=p1.getPower1Yesterday();
            int u1ps=p1.getPower1Stolen();
            double sum=u1p+u1ps;
            if(u1p/sum<=0.3){//剩余太少，不能偷
				p1.setCanPower1Steal(0);
				session.update(p1);
				response.setStatus(201);
            }else{
                //从剩余的能量中减去总能量的30%之后再乘0.5，获取一个随机值
                int randNum =(int)(Math.random() * (( u1p-sum*0.3)*0.5 + 1));
                //已被偷的能量加上
                p1.setPower1Stolen(p1.getPower1Stolen()+randNum);
                //剩余能量减去
                p1.setPower1Yesterday(p1.getPower1Yesterday()-randNum);
				int ppp=p2.getPower()+randNum;
				p2.setPower(ppp);

				u1.setPower(p1.getPower());
				u2.setPower(ppp);

				AlternateRecord alternateRecord=new AlternateRecord();
				alternateRecord.setUser1Id(getUser1id());
				alternateRecord.setUser2Id(getUser2id());
				alternateRecord.setPower(randNum);
				alternateRecord.setType(Integer.parseInt(TOMATO_POWER));
				alternateRecord.setTime(new Timestamp(System.currentTimeMillis()));
				result=new Gson().toJson(alternateRecord);
				session.save(alternateRecord);
				//如果偷完之后能量太少，就设置为不能偷
				if(p1.getPower()/(p1.getPower1Yesterday()+p1.getPower1Stolen())<0.3){
					p1.setCanPower1Steal(0);
				}
				session.update(p1);
				session.update(p2);
				session.update(u1);
				session.update(u2);
				response.setStatus(200);
            }
		}else if(getPowertype().equals(CUSTOM_POWER)){//如果偷的是习惯能量
            int u1p=p1.getPower2Yesterday();
            int u1ps=p1.getPower2Stolen();
            double sum=u1p+u1ps;
            if(u1p/sum<=0.3){//剩余太少，不能偷
				p1.setCanPower2Steal(0);
				session.update(p1);
				response.setStatus(201);
            }else{
                //从剩余的能量中减去总能量的30%之后再乘0.5，获取一个随机值
                int randNum =(int)(Math.random() * (( u1p-sum*0.3)*0.5 + 1));
                //已被偷的能量加上
                p1.setPower2Stolen(p1.getPower2Stolen()+randNum);
                //剩余能量减去
                p1.setPower2Yesterday(p1.getPower2Yesterday()-randNum);
                int ppp=p2.getPower()+randNum;
                p2.setPower(ppp);

				u1.setPower(p1.getPower());
				u2.setPower(ppp);
				//产生一条记录
				AlternateRecord alternateRecord=new AlternateRecord();
				alternateRecord.setUser1Id(getUser1id());
				alternateRecord.setUser2Id(getUser2id());
				alternateRecord.setPower(randNum);
				alternateRecord.setType(Integer.parseInt(CUSTOM_POWER));
				alternateRecord.setTime(new Timestamp(System.currentTimeMillis()));
				result=new Gson().toJson(alternateRecord);
				session.save(alternateRecord);
				//如果偷完之后能量太少，就设置为不能偷
				if(p1.getPower2Yesterday()/(p1.getPower2Yesterday()+p1.getPower2Stolen())<0.3){
					p1.setCanPower2Steal(0);
				}
                session.update(p1);
                session.update(p2);
				session.update(u1);
				session.update(u2);

				response.setStatus(200);

            }
		}else if(getPowertype().equals(WATERING)){//如果是浇水
			p2.setPower(p2.getPower()+10);
			u2.setPower(u2.getPower()+10);
			AlternateRecord alternateRecord=new AlternateRecord();
			alternateRecord.setUser1Id(getUser1id());
			alternateRecord.setUser2Id(getUser2id());
			alternateRecord.setPower(10);
			alternateRecord.setType(Integer.parseInt(WATERING));
			alternateRecord.setTime(new Timestamp(System.currentTimeMillis()));
			result=new Gson().toJson(alternateRecord);
			session.save(alternateRecord);
			session.update(p2);
			session.update(u2);
			response.setStatus(200);

		}
		PrintWriter printWriter=response.getWriter();
		printWriter.write(result);
		printWriter.flush();
		printWriter.close();
        session.getTransaction().commit();
		session.close();
	 	return null;
	}

	public String doGetRank() throws IOException {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		Session session=HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		String query_str="select new User(userid,name,icon,power,tomatoN) from User order by power desc ";
		Query<User> query=session.createQuery(query_str);
		List<User> list=query.getResultList();
		Gson gson=new Gson();
		String result=gson.toJson(list);
		response.setStatus(200);
		PrintWriter writer;
		writer=response.getWriter();
		writer.write(result);
		writer.flush();
		writer.close();
		session.getTransaction().commit();
		session.close();
		return null;
	}

	/**
	 * 获取朋友的信息
	 * @return
	 * @throws IOException
	 */
	public String doGetFriendInfo() throws IOException {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		request.getAttribute("user1id");
		request.getAttribute("user2id");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		Session session=HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		//先查一下交互记录，如果有user1对user2的交互了，就不让他偷了
		String query_str="from AlternateRecord a where a.user1Id=:user1Id and a.user2Id=:user2Id and a.time>:time";
		Query<AlternateRecord> query=session.createQuery(query_str);
		query.setParameter("user1Id", getUser1id());
		query.setParameter("user2Id", getUser2id());
		Timestamp today0clock=new Timestamp(System.currentTimeMillis());
		today0clock.setHours(0);
		today0clock.setMinutes(0);
		query.setParameter("time",today0clock);
		List<AlternateRecord> list=query.getResultList();

		Power p=session.get(Power.class,new Long(getUser2id()));
		session.getTransaction().commit();
		session.close();
		if(list.size()==0){

		}else{
			for(AlternateRecord a:list){
				if(a.getType()==1){
					p.setCanPower1Steal(0);
				}else if(a.getType()==2){
					p.setCanPower2Steal(0);
				}
			}
		}
		response.setStatus(200);
		PrintWriter writer;
		writer=response.getWriter();
		p.setDate(null);
		writer.write(new Gson().toJson(p));
		writer.flush();
		writer.close();
		return null;
	}


	public String doGetRecord() throws IOException {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		request.getAttribute("userid");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		Session session=HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		String queryStr="from AlternateRecord a where a.user2Id=:user2id";
		Query<AlternateRecord> query=session.createQuery(queryStr);
		query.setParameter("user2id",getUserid());
		List<AlternateRecord> list=query.getResultList();
		if(list.size()!=0){
			PrintWriter printWriter=response.getWriter();
			printWriter.write(new Gson().toJson(list));
			printWriter.flush();
			printWriter.close();
		}
		session.getTransaction().commit();
		session.close();
		return null;
	}
}
