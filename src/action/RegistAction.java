package action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.aliyun.api.gateway.demo.SingleSendSms;
import com.aliyun.api.gateway.demo.util.HttpUtils;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import entity.User;
import util.HibernateUtil;

public class RegistAction extends ActionSupport {
	private String userId;
	private String password;
	private String phone;
	private String name;
	private String identifyCodeC;//客户端传过来的验证码
	private String identifyCodeS;//服务端生成的验证码
	private String responseJson;
	
	public String getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	public String getIdentifyCodeC() {
		return identifyCodeC;
	}

	public void setIdentifyCodeC(String identifyCodeC) {
		this.identifyCodeC = identifyCodeC;
	}
	
	public String getIdentifyCodeS() {
		return identifyCodeS;
	}

	public void setIdentifyCodeS(String identifyCodeS) {
		this.identifyCodeS = identifyCodeS;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 检查数据库中是否已经有了注册的手机号
	 * @return
	 * @throws IOException 
	 */
	public Boolean checkAccount() throws IOException{
		boolean result;
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		request.getAttribute("phone");
		//创建会话对象
		Session session=HibernateUtil.getSessionFactory().getCurrentSession();
		//开始一个事物
		session.beginTransaction();
		//查询手机号
		String query_str="from User s where s.phone=:phone";
		Query<User> query=session.createQuery(query_str);
		query.setParameter("phone", getPhone());
		List<User> list=query.getResultList();
		if(list.size()!=0){//如果找到了就返回一个1
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter writer=response.getWriter();
			writer.write("1");
			writer.flush();
			writer.close();
		}else{//如果没找到就返回一个0
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter writer=response.getWriter();
			writer.write("0");
			writer.flush();
			writer.close();
			
		}
		
		session.getTransaction().commit();//提交事务
		HibernateUtil.getSessionFactory().close();//关闭会话工厂
		return true;
	}
	
	public Boolean checkAccount(String phone) throws IOException{
		boolean result;
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
			result=true;
		}else{//如果没找到就返回一个0
			result=false;
		}
		
		session.getTransaction().commit();//提交事务
		//这里不能把回话工厂关了，否则后面regist就无法操作数据库
		return result;
	}
	
	/**
	 * 发送验证码
	 * @return
	 * @throws IOException 
	 */
	
	public String sendIdentifyCode() throws IOException{
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		
		PrintWriter writer=response.getWriter();
		request.getAttribute("phone");
		if(checkAccount(getPhone())){
			response.setStatus(200);
			writer.write("-1");
		}else{
			SingleSendSms app = new SingleSendSms();
			int randNum =100000+(int)(Math.random() * ((999999 - 100000) + 1));
			String identifyCode=String.valueOf(randNum);
	        int result=app.sendMsg(getPhone(),"{'no':'"+identifyCode+"'}");
			//int result=1;
			
	        if(result==1){
	        	response.setStatus(HttpServletResponse.SC_OK);
	        
				writer.write("1");	//发送成功了就返回一个1
				writer.write(identifyCode);
				    
				HttpSession sessionMap = request.getSession();  
				sessionMap.setAttribute("identifyCodeS", identifyCode);
				sessionMap.setAttribute("phone",getPhone());
				System.out.println("验证码是："+identifyCode);
	        }else{
	        	response.setStatus(HttpServletResponse.SC_OK);
				writer.write("0");	//发送失败了就返回一个0
	        }
	       
		}
		writer.flush();
		writer.close();
        return null;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public String regist() throws IOException{
		 	
		HttpServletRequest request=ServletActionContext.getRequest();
		request.getAttribute("phone");
		request.getAttribute("password");
		request.getAttribute("identifyCodeC");
		request.getAttribute("name");
		System.out.println("phone："+getPhone());
		System.out.println("password:"+getPassword());
		System.out.println("identifyCodeC:"+getIdentifyCodeC());
		System.out.println("name:"+getName());
		
		
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8"); 
	    Session session=HibernateUtil.getSessionFactory().openSession();
		//开始一个事物
		session.beginTransaction();
		
		//如果客户端传上来的验证码和服务端的验证码一样的话就去注册
		HttpSession sessionMap = request.getSession();  
		System.out.println("sessionMap中的"+sessionMap.getAttribute("identifyCodeS"));
		System.out.println("sessionMap中的"+sessionMap.getAttribute("phone"));
		PrintWriter write=response.getWriter();
		//判断手机号
		if(getPhone().equals(sessionMap.getAttribute("phone"))){
			//判断验证码
			if(getIdentifyCodeC().equals(sessionMap.getAttribute("identifyCodeS"))){
				System.out.println("判断对了");
				User user1=new User();
				user1.setPhone(getPhone());
				user1.setPassword(getPassword());
				user1.setName(getName());
				session.save(user1);
				
				response.setStatus(200);
				Gson gson=new Gson();
				User user2=new User();
				user2.setUserid(user1.getUserid());
				user2.setName(getName());
				user2.setPhone(getPhone());
				responseJson=gson.toJson(user2);
				write.write(responseJson);
				System.out.println(responseJson);
			}else{
				response.setStatus(201);//验证码输入错误
			}
		}else{
			response.setStatus(202);//手机和之前发送的不同
		}
		write.flush();
		write.close();
		
		session.getTransaction().commit();//提交事务
		session.close();//关闭会话工厂
		return null;
	}
	
	private String getBodyData(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
        } catch (IOException e) {
        } finally {
        }
        return data.toString();
    }
	
//	public void p(){
//		//1，获取cipher 对象
//		Cipher cipher = Cipher.getInstance("RSA");
//		//2，通过秘钥对生成器KeyPairGenerator 生成公钥和私钥
//		KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
//		//使用公钥进行加密，私钥进行解密（也可以反过来使用）
//		PublicKey publicKey = keyPair.getPublic();
//		PrivateKey privateKey = keyPair.getPrivate();
//		//3,使用公钥初始化密码器
//		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//		//4，执行加密操作
//		byte[] result = cipher.doFinal(content.getBytes());
//		//使用私钥初始化密码器
//		cipher.init(Cipher.DECRYPT_MODE, privateKey);
//		//执行解密操作
//		byte[] deResult = cipher.doFinal(result);
//	}
}
