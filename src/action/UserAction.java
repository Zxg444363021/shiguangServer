package action;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import entity.AlternateRecord;
import entity.OnesRecord;
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
	public static final int CANTSTEAL=201;
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
		request.getAttribute("userid");//主偷用户
		request.getAttribute("user2id");//被偷用户
		request.getAttribute("powertype");

		Session session=HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		//主偷用户
		User u1=session.get(User.class,new Long(getUser1id()));
		//被偷用户
		User u2=session.get(User.class,new Long(getUser2id()));

        String result="";

		if(getPowertype().equals(TOMATO_POWER)){	//如果偷的是番茄能量
            int u2py=u2.getPower1Yesterday();//剩余能量
            int u2ps=u2.getPower1Stolen();	//目前为止已经被偷的能量
            double sum=u2py+u2ps;	//昨天生成的能量
            if(u2py/sum<=0.3){//剩余太少，不能偷
				u2.setPower1CanSteal(0);
				session.update(u2);
				response.setStatus(CANTSTEAL);//不能偷
            }else{
                //从剩余的能量中减去总能量的30%之后再乘0.5，获取一个随机值
                int randNum =(int)(Math.random() * (( u2py-sum*0.3)*0.5 + 1));
                //已被偷的能量加上
				u2.setPower1Stolen(u2.getPower1Stolen()+randNum);
                //剩余能量减去
				u2.setPower1Yesterday(u2.getPower1Yesterday()-randNum);
				//u1的总能量加上
				u1.setPower(u1.getPower()+randNum);

				AlternateRecord alternateRecord=new AlternateRecord();
				alternateRecord.setUser1id(getUser1id());
				alternateRecord.setUser2id(getUser2id());
				alternateRecord.setPower(randNum);
				alternateRecord.setType(Integer.parseInt(TOMATO_POWER));
				alternateRecord.setTime(new Timestamp(System.currentTimeMillis()));



				result=new Gson().toJson(alternateRecord);
				session.save(alternateRecord);
				//如果偷完之后能量太少，就设置为不能偷
				if(u2.getPower1Yesterday()/sum<0.3){
					u2.setPower1CanSteal(0);
				}
				session.update(u1);
				session.update(u2);
				response.setStatus(200);
            }
		}else if(getPowertype().equals(CUSTOM_POWER)){//如果偷的是习惯能量
            int u2py=u2.getPower2Yesterday();
            int u2ps=u2.getPower2Stolen();
            double sum=u2py+u2ps;
            if(u2py/sum<=0.3){//剩余太少，不能偷
				u2.setPower2CanSteal(0);
				session.update(u2);
				response.setStatus(CANTSTEAL);
            }else{
                //从剩余的能量中减去总能量的30%之后再乘0.5，获取一个随机值
                int randNum =(int)(Math.random() * (( u2py-sum*0.3)*0.5 + 1));
                //已被偷的能量加上
                u2.setPower2Stolen(u2.getPower2Stolen()+randNum);
                //剩余能量减去
                u2.setPower2Yesterday(u2.getPower2Yesterday()-randNum);
				u1.setPower(u1.getPower()+randNum);
				//产生一条记录
				AlternateRecord alternateRecord=new AlternateRecord();
				alternateRecord.setUser1id(getUser1id());
				alternateRecord.setUser2id(getUser2id());
				alternateRecord.setPower(randNum);
				alternateRecord.setType(Integer.parseInt(CUSTOM_POWER));
				alternateRecord.setTime(new Timestamp(System.currentTimeMillis()));
				result=new Gson().toJson(alternateRecord);
				session.save(alternateRecord);
				//如果偷完之后能量太少，就设置为不能偷
				if(u2.getPower2Yesterday()/sum<0.3){
					u2.setPower2CanSteal(0);
				}
				session.update(u1);
				session.update(u2);
				response.setStatus(200);
            }
		}else if(getPowertype().equals(WATERING)){//如果是浇水
			u2.setPower(u2.getPower()+10);
			AlternateRecord alternateRecord=new AlternateRecord();
			alternateRecord.setUser1id(getUser1id());
			alternateRecord.setUser2id(getUser2id());
			alternateRecord.setPower(10);
			alternateRecord.setType(Integer.parseInt(WATERING));
			alternateRecord.setTime(new Timestamp(System.currentTimeMillis()));
			result=new Gson().toJson(alternateRecord);
			session.save(alternateRecord);
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

	/**
	 * 获取排行榜
	 * 在此一并把朋友的power信息获取了
	 * @return
	 * @throws IOException
	 */
	public String doGetRank() throws IOException {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		Session session=HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		String query_str="select new User(userid,name,icon,power,tomatoN,power1Yesterday,power2Yesterday,power1CanSteal,power2CanSteal) from User order by power desc ";
		Query<User> query=session.createQuery(query_str);
		List<User> userList=query.getResultList();

		//获取到我今天所有的对别的交互记录
		String query_str2="from AlternateRecord a where a.user1id=:user1id and a.time>:time";
		Query<AlternateRecord> query2=session.createQuery(query_str2);
		query2.setParameter("user1id", getUserid());//我的id
		Timestamp today0clock=new Timestamp(System.currentTimeMillis());
		today0clock.setHours(0);
		today0clock.setMinutes(0);
		query2.setParameter("time",today0clock);
		List<AlternateRecord> alternateRecordList=query2.getResultList();
		//以user2id为key放在一个map中
		Map<Long,AlternateRecord> alternateRecordMap=new HashMap<Long, AlternateRecord>();
		for(int i=0;i<alternateRecordList.size();i++){
			alternateRecordMap.put(alternateRecordList.get(i).getUser2id(),alternateRecordList.get(i));
		}
		//对于userList,如果其userid在map中存在，说明
		User user;
		for(int i=0;i<userList.size();i++){
			user=userList.get(i);
			if(alternateRecordMap.containsKey(user.getUserid())){
				int type=alternateRecordMap.get(user.getUserid()).getType();
				if(type==1){	//如果有交互那一定不能再偷了
					user.setPower1CanSteal(0);
				}else if(type==2){
					user.setPower2CanSteal(0);
				}else if(user.getPower1Yesterday()!=0){	//如果没有过交互，且昨天产生了能量，那么可以偷
					user.setPower1CanSteal(1);
				}else if(user.getPower2Yesterday()!=0){
					user.setPower2CanSteal(1);
				}
			}
		}
		Gson gson=new Gson();
		String result=gson.toJson(userList);
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
	 * 获取交互
	 * @return
	 * @throws IOException
	 */
	public String doGetRecord() throws IOException {
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		request.getAttribute("userid");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		Session session=HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		String queryStr="from OnesRecord  where user2id=:user2id";
		Query<OnesRecord> query=session.createQuery(queryStr);
		query.setParameter("user2id",getUserid());
		List<OnesRecord> list=query.getResultList();
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
