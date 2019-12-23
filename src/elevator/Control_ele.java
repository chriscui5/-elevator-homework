package elevator;

import java.util.*;

public class Control_ele {
    private Elevator ele;//电梯类

    private Queue<String> ins;//暂时存入输入指令的指令集
    private ArrayList<ArrayList<Ele_Instruction>> level_ins;//根据楼层对指令集进行存取
    private int[] level_time;//保存每层楼的指令最短的时间

    public Control_ele() {
        //构造类
        ele=new Elevator();
        ins=new LinkedList<>();
        level_time=new int[11];
        level_ins=new ArrayList<>(11);
        for (int i=0;i<11;i++){
            level_ins.add(i,new ArrayList<>());
        }
    }

    public void welcome() {
        //处理指令的输入与输出
        Scanner scanner = new Scanner(System.in); // Get user input
        String input;
        System.out.println("请输入你的指令，指令以RUN结束:");
        
        boolean flag = false; //首行是否被添加
        while (true) {
            input=scanner.nextLine(); //改为nextLine防止因空格出现断行
            //处理第一条不等于(FR,1,UP,0)的情况
            input = input.replace(" ", ""); //去除空格,有bug
            input = input.replace("，", ","); //去除中文逗号
        	input = input.replace("（", "("); //去除中文括号
        	input = input.replace("）", ")"); //去除中文括号
//            if(!flag && !input.equals("(FR,1,UP,0)")) {
//            	String t = "(FR,1,UP,0)";
//            	ins.offer(t);
//            	//System.out.println("第一条必须为(FR,1,UP,0)！");
//            	//continue;
//            }            
            ////if(!input.equals("(FR,1,UP,0)")){ }
            ////并没有处理第一条不等于(FR,1,UP,0)的情况
            if(input.equals("RUN") ||input.equals("run")) {
                break;//并没有考虑不是RUN或者不是标准字符串出现的情况
            }

            ins.offer(input);
            if(!flag) //添加了首行
            	flag = true;
        }
        float last=0;
        //后发出指令的时间一定比先发出指令的时间长 如果不是 处理为无效指令
        //并且把存在Queue<String> ins放在 ArrayList<ArrayList <Ele_Instruction>> level_ins;
        for(String item:ins) {
            Ele_Instruction ele_ins=new Ele_Instruction();//指令集

            if(ele_ins.parseInstruction2(item)){
                int tmp_level = ele_ins.getLevel(); 
                if(last<=ele_ins.getRequest_time()) {
                    level_ins.get(tmp_level).add(ele_ins);
                    last=ele_ins.getRequest_time();
                }
                else {
                    System.out.println("#无效指令："+item);//处理指令时间错误的指令
                }
            }
        }
        //处理指令，命令电梯运行

        control();
    }


    public void control() {
        //处理指令，命令电梯运行
        while (!jude_empty()){//判断指令是否为空
            do_recent_request_time_of_level();//处理int[] level_time为每层楼的指令的最短的时间
            ele.setDes_level(get_recent_request_time_of_level());//让电梯的目的楼层==得到发出时间最早的指令要去的楼层
            float tmp_time=level_ins.get(ele.getDes_level()).get(0).getRequest_time();
            if(ele.getRuntime()<tmp_time){
                ele.setRuntime(tmp_time);
            }//设置电梯运行的时间
            //double pre_time = 
            if(ele.getLevel()==ele.getDes_level()){
                //电梯不行动
                ele.setDirection(0);
                ele.ele_open_close();
                level_ins.get(ele.getDes_level()).remove(0);
                //处理电梯为still状态的指令的重复指令
                handle_repeat(ele.getDes_level());
            }
            else if(ele.getLevel()<ele.getDes_level())
            {
                //电梯上行
                ele.setDirection(1);
                shaodai_up();//上行的捎带
                ele.ele_Up(ele.getDes_level());
                level_ins.get(ele.getDes_level()).remove(0);
                handle_repeat(ele.getDes_level());
            }
            else if(ele.getLevel()>ele.getDes_level())
            {
                //电梯下行
                ele.setDirection(-1);
                shaodai_down();//下行的捎带
                ele.ele_Down(ele.getDes_level());
                level_ins.get(ele.getDes_level()).remove(0);
                handle_repeat(ele.getDes_level());
            }
        }
    }


    public void do_recent_request_time_of_level() {
        //处理int[] level_time为每层楼的指令的最短的时间
        level_time[0]=Integer.MAX_VALUE;
        for (int i=1;i<=10;i++){
            if (level_ins.get(i).size()==0){
                level_time[i]=Integer.MAX_VALUE;
            }
            else {
                level_time[i]=level_ins.get(i).get(0).getRequest_time();
            }
        }
    }
    public int get_recent_request_time_of_level(){
        //得到发出时间最早的指令要去的楼层
        int level=1;
        for (int i=2;i<=10;i++){
            if (level_time[i]<level_time[level]){
                level=i;
            }
            else if(level_time[i]==level_time[level]){
                if(Math.abs(i-ele.getLevel())<Math.abs(level-ele.getLevel())){
                    level=i;
                }
            }
        }
        return level;
    }

    public void shaodai_up(){
        //处理上行的捎带
        Ele_Instruction j=new Ele_Instruction();
        for (int i=ele.getLevel()+1;i<=ele.getDes_level()-1;i++){
            int rank=0;
            boolean flag=false;
            while (!level_ins.get(i).isEmpty()&&level_ins.get(i).size()>=rank+1) {
                j=level_ins.get(i).get(rank);
                if(((j.getIn_out()==0)||
                        (j.getIn_out()==1&&j.getDirect()==1))&&
                                (ele.getRuntime()+(i-ele.getLevel())*ele.getUnit_time()>=j.getRequest_time())) {
                    ele.ele_Up(i);
                    level_ins.get(i).remove(rank);

                    flag=true;
                    break;
                }
                rank++;
            }
            rank=0;
            while (flag&&!level_ins.get(i).isEmpty()&&level_ins.get(i).size()>=rank+1) {
                j=level_ins.get(i).get(rank);
                if(ele.getRuntime()>=j.getRequest_time()) {
                    System.out.println("#无效指令："+level_ins.get(i).get(rank).toString());
                    level_ins.get(i).remove(rank);
                    rank--;
                }//处理重复指令
                rank++;
            }
        }
    }

    public void shaodai_down(){
        //处理上行的
        Ele_Instruction j=new Ele_Instruction();
        for (int i=ele.getLevel()-1;i>=ele.getDes_level()+1;i--){
            boolean flag=false;
            int rank=0;
            while (!level_ins.get(i).isEmpty()&&level_ins.get(i).size()>=rank+1) {
                j=level_ins.get(i).get(rank);
                if(((j.getIn_out()==0)||
                        (j.getIn_out()==1&&j.getDirect()==-1))&&
                                (ele.getRuntime()+(ele.getLevel()-i)*ele.getUnit_time()>=j.getRequest_time())) {
                    ele.ele_Down(i);
                    level_ins.get(i).remove(rank);
                    flag=true;
                    break;
                }
                rank++;
            }
            rank=0;
            while (flag&&!level_ins.get(i).isEmpty()&&level_ins.get(i).size()>=rank+1) {
                j=level_ins.get(i).get(rank);
                if(ele.getRuntime()>=j.getRequest_time()) {
                    System.out.println("#无效指令："+level_ins.get(i).get(rank).toString());
                    level_ins.get(i).remove(rank);
                    rank--;
                }//处理重复指令
                rank++;
            }
        }
    }
     public Boolean jude_empty(){
        //判断ArrayList<ArrayList<Ele_Instruction>> level_ins是否为空
        int empty_level=0;
        for (int i=1;i<=10;i++){
            if (level_ins.get(i).isEmpty()){
                empty_level++;
            }
        }
        if (empty_level==10){
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }
    public void handle_repeat(int level){
        int rank=0;
        while (!level_ins.get(level).isEmpty()&&level_ins.get(level).size()>=rank+1) {
            if(level_ins.get(level).get(rank).getRequest_time()<=ele.getRuntime()){
//                if(level_ins.get(level).get(rank).getIn_out() == 1) { //本层外楼请求跳过
//                	break;
//                }
            	//相同时间不同指令错误去重
            	System.out.println("#无效指令（ERROR）："+level_ins.get(ele.getDes_level()).get(rank).toString());
                level_ins.get(level).remove(rank);
                rank--;
            }
            else{
                break;
            }
            rank++;
        }
    }//处理重复指令

    public static void main(String[] args) {
        Control_ele e=new Control_ele();
        e.welcome();
    }
}
