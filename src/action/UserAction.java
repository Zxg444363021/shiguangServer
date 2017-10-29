package action;

import java.io.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ResponseBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import entity.*;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.opensymphony.xwork2.ActionSupport;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

/*

<property name="connection.username">shiguang</property>
<property name="connection.password">shiguang20170522</property>
 */


public class UserAction extends ActionSupport {
    private static final String BASEURL = "http://121.42.140.71:8080/shiguangServer/";
    private static final String TOMATO_POWER = "1";
    private static final String CUSTOM_POWER = "2";
    private static final String WATERING = "3";
    public static final String STEP_POWER = "4";
    private String phone;
    private Long user1id;
    private File mPhoto;
    private String name;
    private Long user2id;
    private String powertype;
    private Long userid;
    private int tomatoTime;    //番茄时间单位分钟
    private int currStep;

    public int getCurrStep() {
        return currStep;
    }

    public void setCurrStep(int currStep) {
        this.currStep = currStep;
    }

    private int getTomatoTime() {
        return tomatoTime;
    }

    public void setTomatoTime(int tomatoTime) {
        this.tomatoTime = tomatoTime;
    }


    private Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    private String getPowertype() {
        return powertype;
    }

    public void setPowertype(String powertype) {
        this.powertype = powertype;
    }

    private Long getUser2id() {
        return user2id;
    }

    public void setUser2id(Long user2id) {
        this.user2id = user2id;
    }

    private String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private Long getUser1id() {
        return user1id;
    }

    public void setUser1id(Long user1id) {
        this.user1id = user1id;
    }

    private File getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(File mPhoto) {
        this.mPhoto = mPhoto;
    }

    /**
     * 上传头像
     *
     * @return
     * @throws IOException
     */
    public String doPostIcon() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        request.getAttribute("phone");
        System.out.println("sessionId=" + request.getSession().getId());
        String dir = ServletActionContext.getServletContext().getRealPath("files");//存储的位置，再看看
        String filename = getPhone() + ".jpg";
        File file = new File(dir, filename);//存储的图片名
        FileUtils.copyFile(mPhoto, file);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        User u = session.get(User.class, new Long(getUserid()));
        u.setIcon(BASEURL + "files/" + filename);
        session.update(u);
        session.getTransaction().commit();
        response.setStatus(200);
        return null;
    }

    /**
     * 修改用户昵称
     *
     * @return
     */
    public String doUpdateInfo() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        request.getAttribute("userid");
        request.getAttribute("name");
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        User u = session.get(User.class, new Long(getUserid()));
        u.setName(getName());
        try {
            session.update(u);
            response.setStatus(200);
        } catch (HibernateException e) {
            response.setStatus(201);
        }
        session.getTransaction().commit();
        session.close();
        return null;
    }

    /**
     * 偷能量,或者是浇水
     *
     * @return
     */
    public String doStealPower() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        request.getAttribute("user1id");//主偷用户
        request.getAttribute("user2id");//被偷用户
        request.getAttribute("powertype");

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        //主偷用户（就是我）
        User u1 = session.get(User.class, new Long(getUser1id()));
        //被偷用户
        User u2 = session.get(User.class, new Long(getUser2id()));

        String result = "";
        ResponseBean responseBean = new ResponseBean();
        if (getPowertype().equals(TOMATO_POWER) || getPowertype().equals(CUSTOM_POWER) || getPowertype().equals(STEP_POWER)) {    //如果偷的是番茄能量

            //先看该好友昨天有没有产生能量
            String queryStr = "from Power  where userid=:user2id and powerType=:powertype and canSteal=1 and date=:yesterday";
            Date yesterday = new Date(System.currentTimeMillis() - 86400000);
            Query<Power> query = session.createQuery(queryStr);
            query.setParameter("user2id", getUser2id());
            query.setParameter("powertype", Integer.parseInt(getPowertype()));
            query.setParameter("yesterday", yesterday);
            List<Power> list = query.getResultList();


            if (list.size() != 0) {
                Power power = list.get(0);

                int u2py = power.getPower();//剩余能量
                int u2ps = power.getPowerStolen();    //目前为止已经被偷的能量
                double sum = u2py + u2ps;    //昨天生成的能量
                if (u2py / sum <= 0.3) {//剩余太少，不能偷
                    power.setCanSteal(0);
                    session.update(u2);
                    session.update(power);
                    responseBean.setCode(ResponseBean.Steal_FAIL);//不能偷
                    responseBean.setMessage("非法操作");
                } else {
                    //从剩余的能量中减去总能量的30%之后再乘0.5，获取一个随机值
                    int randNum = (int) (Math.random() * ((u2py - sum * 0.3) * 0.5 + 1));
                    //已被偷的能量加上
                    power.setPowerStolen(power.getPowerStolen() + randNum);
                    //剩余能量减去
                    power.setPower(power.getPower() - randNum);
                    //u1的总能量加上
                    u1.setPower(u1.getPower() + randNum);

                    AlternateRecord alternateRecord = new AlternateRecord();
                    alternateRecord.setUser1id(getUser1id());
                    alternateRecord.setUser2id(getUser2id());
                    alternateRecord.setPower(randNum);
                    alternateRecord.setType(Integer.parseInt(getPowertype()));
                    alternateRecord.setTime(new Timestamp(System.currentTimeMillis()));


                    session.save(alternateRecord);
                    //如果偷完之后能量太少，就设置为不能偷
                    if (power.getPower() / sum < 0.3) {
                        power.setCanSteal(0);
                    }

                    responseBean.setCode(ResponseBean.Steal_SUCC);//偷成功了
                    responseBean.setMessage("收取成功！");
                    responseBean.setData(new Gson().toJson(alternateRecord));
                    session.update(u1);
                    session.update(power);
                }
                response.setStatus(200);

            } else {
                responseBean.setCode(ResponseBean.Steal_FAIL);//不能偷
                responseBean.setMessage("非法操作");
                response.setStatus(200);
            }
        } else if (getPowertype().equals(WATERING)) {//如果是浇水
            u2.setPower(u2.getPower() + 10);
            AlternateRecord alternateRecord = new AlternateRecord();
            alternateRecord.setUser1id(getUser1id());
            alternateRecord.setUser2id(getUser2id());
            alternateRecord.setPower(10);
            alternateRecord.setType(Integer.parseInt(WATERING));
            alternateRecord.setTime(new Timestamp(System.currentTimeMillis()));

            session.save(alternateRecord);

            responseBean.setCode(ResponseBean.Water_SUCC);//偷成功了
            responseBean.setMessage("浇水成功");
            responseBean.setData(new Gson().toJson(alternateRecord));
            session.update(u2);

            response.setStatus(200);
        }


        result = new Gson().toJson(responseBean);
        PrintWriter printWriter = response.getWriter();
        printWriter.write(result);
        printWriter.flush();
        printWriter.close();
        try {
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.close();
        return null;
    }

    /**
     * 获取排行榜
     * 在此一并把朋友的power信息获取了
     *
     * @return
     * @throws IOException
     */

    public String doGetRank() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        request.getAttribute("userid");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        String query_str = "select new User(userid,name,icon,power,tomatoN,uuid) from User  order by power desc ";
        Query<User> query = session.createQuery(query_str);
        List<User> userList = query.getResultList();
        //用一个map来暂存所有好友，可以保证插入的顺序
        Map<Long, User> userMap = new LinkedHashMap<Long, User>();
        for (User u : userList) {
            u.setCanBeStolen(0);
            userMap.put(u.getUserid(), u);
        }

        //然后查询power表，找找看有没有昨天生成power的记录
        String queryStr = "from Power  where date=:yesterday";
        Date yesterday = new Date(System.currentTimeMillis() - 86400000);
        Query<Power> query1 = session.createQuery(queryStr);
        query1.setParameter("yesterday", yesterday);
        List<Power> powerList = query1.getResultList();


        //获取到我今天所有的对别人的交互记录
        String query_str2 = "from AlternateRecord a where a.user1id=:user1id and a.time>:time";
        Query<AlternateRecord> query2 = session.createQuery(query_str2);
        query2.setParameter("user1id", getUserid());//我的id
        Timestamp today0clock = new Timestamp(System.currentTimeMillis());
        today0clock.setHours(0);
        today0clock.setMinutes(0);
        query2.setParameter("time", today0clock);
        List<AlternateRecord> alternateRecordList = query2.getResultList();

        //以user2id+powerType为key放在一个set中
        Set<String> alternateRecordSet = new HashSet<String>();
        for (int i = 0; i < alternateRecordList.size(); i++) {
            alternateRecordSet.add(
                    String.valueOf(alternateRecordList.get(i).getUser2id())
                            + String.valueOf(alternateRecordList.get(i).getType())
            );
        }
        //对于userList,如果其userid在map中存在，说明
        for (Power p : powerList) {
            //如果昨天的某一项能量可以被偷，而且我并没有偷过
            if (p.getCanSteal() == 1
                    && !alternateRecordSet.contains(String.valueOf(p.getUserid()) + String.valueOf(p.getPowerType()))) {
                userMap.get(p.getUserid()).setCanBeStolen(1);
            }
        }
        userList.clear();
        Iterator<Map.Entry<Long, User>> iterator = userMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            userList.add((User) entry.getValue());
        }
        Gson gson = new Gson();
        String result = gson.toJson(userList);
        response.setStatus(200);
        PrintWriter writer;
        writer = response.getWriter();
        writer.write(result);
        writer.flush();
        writer.close();
        session.getTransaction().commit();
        session.close();
        return null;
    }


    /**
     * 获取交互
     *
     * @return
     * @throws IOException
     */
    public String doGetRecord() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        request.getAttribute("userid");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String queryStr = "from OnesRecord  where user2id=:user2id";
        Query<OnesRecord> query = session.createQuery(queryStr);
        query.setParameter("user2id", getUserid());
        List<OnesRecord> list = query.getResultList();
        if (list.size() != 0) {
            PrintWriter printWriter = response.getWriter();
            printWriter.write(new Gson().toJson(list));
            printWriter.flush();
            printWriter.close();
        }
        session.getTransaction().commit();
        session.close();
        return null;
    }

    /**
     * 番茄时间到了,或者说收获了番茄能量之后自动上传番茄。
     *
     * @return
     * @throws IOException
     */
    public String doAddPower() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        request.getAttribute("userid");
        request.getAttribute("tomatoTime");
        request.getAttribute("powertype");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        User user = session.get(User.class, new Long(getUserid()));


        //先看该我自己今天有没有产生过能量
        String queryStr = "from Power  where userid=:userid and powerType=:powertype and date=:today";
        Date today = new Date(System.currentTimeMillis());
        Query<Power> query = session.createQuery(queryStr);
        query.setParameter("userid", getUserid());
        query.setParameter("powertype", Integer.parseInt(getPowertype()));
        query.setParameter("today", today);
        List<Power> list = query.getResultList();


        if (getPowertype().equals(TOMATO_POWER)) {

            int tomatoTime = getTomatoTime();  //总时长
            int tomatoNum = tomatoTime / 25;//25分钟一个番茄
            int power = 0;
            switch (tomatoNum) {
                case 1:
                    power = 50;
                    break;
                case 2:
                    power = 100;
                    break;
                case 3:
                    power = 150;
                    break;
                case 4:
                    power = 200;
                    break;
                case 5:
                    power = 254;
                    break;
                default:
                    power = 254;    //最多254g
                    break;
            }

            int i = 0;
            for (; i < list.size(); i++) {
                if (list.get(i).getPowerType().equals(TOMATO_POWER))
                    break;
            }
            //如果找到了，直接加
            if (i < list.size()) {
                Power p = list.get(i);
                p.setPower(p.getPower() + power);
                session.update(p);
            } else {
                Power p = new Power();
                p.setPower(power);
                p.setCanSteal(1);
                p.setPowerStolen(0);
                p.setHasGain(0);
                p.setUserid(getUserid());
                p.setPowerType(Integer.valueOf(getPowertype()));
                p.setDate(today);
                session.save(p);
            }
            user.setTomatoN(user.getTomatoN() + tomatoNum);    //番茄数立即修改
            session.update(user);
            response.setStatus(200);
        } else if (getPowertype().equals(CUSTOM_POWER)) {
            int i = 0;
            for (; i < list.size(); i++) {
                if (list.get(i).getPowerType().equals(CUSTOM_POWER))
                    break;
            }
            //如果找到了，直接加
            if (i < list.size()) {
                Power p = list.get(i);
                p.setPower(p.getPower() + 20);
                session.update(p);
            } else {
                Power p = new Power();
                p.setPower(20);
                p.setCanSteal(1);
                p.setPowerStolen(0);
                p.setHasGain(0);
                p.setUserid(getUserid());
                p.setPowerType(Integer.valueOf(getPowertype()));
                p.setDate(today);
                session.save(p);
            }

            response.setStatus(200);
        }

        session.getTransaction().commit();
        session.close();
        return null;
    }

    /**
     * 更新今日的步数和能量
     *
     * @return
     * @throws IOException
     */
    public String doUpdateStepPower() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        request.getAttribute("userid");
        request.getAttribute("currStep");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();


        //先看该我自己今天有没有产生过能量
        String queryStr = "from Power  where userid=:userid and powerType=4 and date=:today";
        Date today = new Date(System.currentTimeMillis());
        Query<Power> query = session.createQuery(queryStr);
        query.setParameter("userid", getUserid());
        query.setParameter("today", today);
        List<Power> list = query.getResultList();

        //看看今天有没有初始化过
        String queryStr1="from Step where userid=:userid and date=:today";
        Query<Step> query1 = session.createQuery(queryStr1);
        query1.setParameter("userid", getUserid());
        query1.setParameter("today", today);
        List<Step> list1 = query1.getResultList();
        if (list1.size()==0){

            Step step=new Step();
            step.setUserid(getUserid());
            step.setCurrentNumber(getCurrStep());
            step.setDate(today);
            step.setTodayStep(0);
            session.save(step);

            Power p = new Power();
            p.setPower(0);
            p.setCanSteal(1);
            p.setPowerStolen(0);
            p.setHasGain(0);
            p.setUserid(getUserid());
            p.setPowerType(Integer.valueOf(STEP_POWER));
            p.setDate(today);
            session.save(p);
        }else{
            Step step=list1.get(0);
            int lastNumber=step.getCurrentNumber();
            int power=0;
            if (getCurrStep()>lastNumber){
                power=(getCurrStep()-lastNumber)/100;
                step.setTodayStep(step.getTodayStep()+getCurrStep()-lastNumber);
                step.setCurrentNumber(getCurrStep());
            }else{
                power=getCurrStep()/100;
                step.setTodayStep(step.getTodayStep()+getCurrStep());
                step.setCurrentNumber(getCurrStep());
            }
            Power p = list.get(0);
            p.setPower(p.getPower() + power);
            session.update(step);
            session.update(p);
        }
        response.setStatus(200);
        session.getTransaction().commit();
        session.close();
        return null;
    }


    /**
     * 收获自己的能量,返回收到的能量数
     *
     * @return
     * @throws IOException
     */
    public String doGainPower() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        request.getAttribute("userid");
        request.getAttribute("powertype");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        User user = session.get(User.class, new Long(getUserid()));

        //先看该我自己昨天有没有产生能量
        String queryStr = "from Power  where userid=:userid and powerType=:powertype and hasGain=0 and date=:yesterday";
        Date yesterday = new Date(System.currentTimeMillis() - 86400000);
        Query<Power> query = session.createQuery(queryStr);
        query.setParameter("userid", getUserid());
        query.setParameter("powertype", Integer.parseInt(getPowertype()));
        query.setParameter("yesterday", yesterday);
        List<Power> list = query.getResultList();
        ResponseBean responseBean = new ResponseBean();
        String result = "";
        if (list.size() != 0) {
            Power power = list.get(0);

            int gainPower = power.getPower();
            user.setPower(user.getPower() + gainPower);
            power.setCanSteal(0);
            power.setHasGain(1);

            try {
                session.update(user);
                session.update(power);
                responseBean.setCode(ResponseBean.GainPower_SUCC);
                responseBean.setMessage("收取能量成功！");
                responseBean.setData(String.valueOf(gainPower));
            } catch (Exception e) {
                responseBean.setCode(ResponseBean.GainPower_FAIL);
                responseBean.setMessage("收取能量失败！请稍后重试");
            }
            response.setStatus(200);
            result = new Gson().toJson(responseBean);
        } else {
            response.setStatus(200);
            responseBean.setCode(ResponseBean.GainPower_FAIL);
            responseBean.setMessage("非法操作");
            result = new Gson().toJson(responseBean);
        }

        PrintWriter printWriter = response.getWriter();
        printWriter.write(result);
        printWriter.flush();
        printWriter.close();

        session.getTransaction().commit();
        session.close();
        return null;
    }

    /**
     * 检查某个好友的能量是否能够被偷
     * 需要好友user2id和我的user1id;
     *
     * @return
     * @throws IOException
     */
    public String doGetCanSteal() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        //先看该好友昨天有没有产生能量
        String queryStr = "from Power  where userid=:user2id and date=:yesterday";
        Date yesterday = new Date(System.currentTimeMillis() - 86400000);
        Query<Power> query = session.createQuery(queryStr);
        query.setParameter("user2id", getUser2id());
        query.setParameter("yesterday", yesterday);
        List<Power> list = query.getResultList();

        ResponseBean result = new ResponseBean();

        //获取到我今天所有的对别人的交互记录
        String query_str2 = "from AlternateRecord a where a.user1id=:user1id and a.user2id=:user2id and a.time>:time";
        Query<AlternateRecord> query2 = session.createQuery(query_str2);
        query2.setParameter("user1id", getUser1id());//我的id
        query2.setParameter("user2id", getUser2id());//这位朋友
        Timestamp today0clock = new Timestamp(System.currentTimeMillis());
        today0clock.setHours(0);
        today0clock.setMinutes(0);
        query2.setParameter("time", today0clock);
        List<AlternateRecord> alternateRecordList = query2.getResultList();

        for (int i = 0; i < list.size(); i++) {
            //对于每一种能量，检查一下我能不能偷
            for (int j = 0; j < alternateRecordList.size(); j++) {
                //虽然能偷，但是你偷过了，你就不能偷了，别人还可以偷
                if (list.get(i).getCanSteal() == 1 &&
                        list.get(i).getPowerType() == alternateRecordList.get(j).getType()) {
                    list.get(i).setCanSteal(0);
                    break;
                }
            }
        }


        PrintWriter printWriter = response.getWriter();
        if (list.size() != 0) {
            //如果该好友昨天产生了能量，在看昨天的能量是不是都还能偷
            int i = 0;
            while (i < list.size()) {
                if (list.get(i).getCanSteal() == 0) {
                    list.remove(i);
                } else {
                    i++;
                }
            }

            if (list.size() != 0) {
                result.setCode(ResponseBean.CanSteal);
                result.setMessage("该好友昨天有能量可偷");
                result.setData(new Gson().toJson(list));
            } else {
                result.setCode(ResponseBean.CanNotSteal);
                result.setMessage("该好友昨天没有能量可偷");
            }
        } else {
            result.setCode(ResponseBean.CanNotSteal);
            result.setMessage("该好友昨天没有能量可偷");
        }
        printWriter.write(new Gson().toJson(result));
        printWriter.flush();
        printWriter.close();
        session.getTransaction().commit();
        session.close();
        return null;
    }


    /**
     * 上传schedule，在服务端做备份。
     * 这是点击那个按钮的时候操作的，比较复杂
     * @return
     * @throws IOException
     */
    public String doUploadSchedule() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        BufferedReader br = request.getReader();
        String str;
        StringBuilder builder=new StringBuilder();
        String json;
        while((str = br.readLine()) != null){
           builder.append(str);
        }

        boolean init=false; //看看数据库是不是空的
        String queryStr="from Schedule where userid=:userid and isDelete=0";
        Query<Schedule> query=session.createQuery(queryStr);
        query.setParameter("userid",getUserid());
        List<Schedule> queryList=query.getResultList();


        Timestamp modifyTime=new Timestamp(System.currentTimeMillis());

        //用来存放IdInServer和list下标的对应关系
        Map<Long,Integer> queryMap=new HashMap<Long, Integer>();
        if (queryList.size()!=0){
            init=true;
            for (int i=0;i<queryList.size();i++){
                queryMap.put(queryList.get(i).getIdInServer(),i);
            }
        }

        ResponseBean responseBean=new ResponseBean();
        json=builder.toString();
        if (json!=null){
            System.out.println(json);
            //获取到传上来的东西
            List<Schedule> schedulesList=new Gson().fromJson(json,new TypeToken<List<Schedule>>(){}.getType());
            if (schedulesList!=null&&schedulesList.size()>0){
                Long userid=getUserid();


                for (Schedule schedule:schedulesList){
                    schedule.setUserid(userid);
                    //如果是初始化数据库，就所有的都存进来
                    if(init){
                        schedule.setIsDelete(0);
                        schedule.setIsDirty(0);
                        schedule.setLastModify(modifyTime);
                        try{
                            session.save(schedule);
                            responseBean.setCode(ResponseBean.UploadSchedule_SUCC);
                        }catch (Exception e){
                            responseBean.setCode(ResponseBean.UploadSchedule_Fail);
                        }

                    }
                    //如果是客户端新添加的，直接截断，不管修改了几次，直接新增
                    else if (schedule.getIsDelete()==2){
                        schedule.setIsDirty(0);
                        schedule.setIsDelete(0);
                        schedule.setLastModify(modifyTime);
                        session.save(schedule);
                        System.out.println(schedule.getIdInServer());
                    }
                    //如果已经上传过了，又在客户端修改过了
                    else if (schedule.getIsDirty()==1){
                        int index=queryMap.get(schedule.getIdInServer());
                        //就更新数据库中的就行了
                        Schedule scheduleInSever=queryList.get(index);
                        scheduleInSever.setIsDirty(0);
                        scheduleInSever.setIsDelete(0);
                        scheduleInSever.setLastModify(modifyTime);
                        session.save(scheduleInSever);
                    }
                    //如果是打算删除的
                    else if(schedule.getIsDelete()==1){
                        int index=queryMap.get(schedule.getIdInServer());
                        //就更新数据库中的就行了
                        Schedule scheduleInSever=queryList.get(index);
                        session.delete(scheduleInSever);
                    }
                }
            }

        }








        PrintWriter printWriter = response.getWriter();
        printWriter.write(new Gson().toJson(responseBean));
        printWriter.flush();
        printWriter.close();
        session.getTransaction().commit();
        session.close();
        return null;
    }

}
