package elevator;

public class Ele_Instruction {
    //根据输入的字符串转换为指令类
    private int In_out;//in=0;out=1;
    private int level;//ER中请求到达的楼层(电梯内的请求)；FR中发出请求的楼层（楼层的请求）
    private int direct;//up=1;still=0;down=-1;
    private int maxlevel=10;
    private int minlevel=1;
    private int request_time;//请求发出时刻


    public Ele_Instruction(){
        In_out=1;
        level=1;
        direct=1;
        request_time=0;
    }

    public int getIn_out() {
        return In_out;
    }

    public int getLevel() { return level; }

    public int getDirect() {
        return direct;
    }

    public int getRequest_time() {
        return request_time;
    }

    public Boolean parseInstruction2(String instruction)//解析指令有错误
    {
    	if(instruction.charAt(0) != '(' || instruction.charAt(instruction.length()-1) != ')'){
            System.out.println("#无效指令："+instruction);
            return false;
        }//指令不以（开头）结尾，输出无效指令

        //指令不带括号
        String instructionWithoutParentheses = instruction.substring(1,instruction.length()-1);
        String[] splitIns = instructionWithoutParentheses.split(",");
        //并没有出来不以逗号，分别的字符串

        if (splitIns[0].equals("FR")) {//指令开头是FR
            if(splitIns.length!=4){
                System.out.println("#无效指令："+instruction);
                return false;
            }
            this.In_out=1;
        }
        else if (splitIns[0].equals("ER")) {//指令开头是ER
            if(splitIns.length!=3) {
                System.out.println("#无效指令："+instruction);
                return false;
            }
            this.In_out=0;
        }
        else {//指令开头不是ER或者FR
            System.out.println("#无效指令："+instruction);
            return false;
        }

        try {
	        if (Integer.parseInt(splitIns[1])>maxlevel||Integer.parseInt(splitIns[1])<minlevel) {
	            //假如楼层大于10小于1为无效指令
	            System.out.println("#无效指令："+instruction);
	            return false;
	        }
	        else {
	            this.level=Integer.parseInt(splitIns[1]); //发出指令的楼层
	        }
        }catch(NumberFormatException e) { //不是整数
        	System.out.println("#无效指令："+instruction);
            return false;
        }
        if (splitIns.length==4) {
            //处理FR指令
            if(splitIns[2].equals("UP")) {
                if (level>=maxlevel) {
                    //10楼发出向上指令为无效指令
                    System.out.println("#无效指令："+instruction);
                    return false;
                }
                this.direct=1;
            }
            else if(splitIns[2].equals("DOWN")) {
                if(level<=minlevel){
                    //1楼发出向下指令为无效指令
                    System.out.println("#无效指令："+instruction);
                    return false;
                }
                this.direct=-1;
            }
            else {
                //并没有发出up或者down指令
                System.out.println("#无效指令："+instruction);
                return false;
            }
            try{
            	this.request_time=Integer.parseInt(splitIns[3]);
            }catch(NumberFormatException e) { //不是整数
            	System.out.println("#无效指令："+instruction);
                return false;
            }
        }
        else if(splitIns.length==3) {	
            //处理ER指令
            if (Integer.parseInt(splitIns[1])>maxlevel||Integer.parseInt(splitIns[1])<minlevel) {
                System.out.println("#无效指令："+instruction);
                return false;
            }
            else {
                this.level=Integer.parseInt(splitIns[1]);
            }
            try{
            	this.request_time=Integer.parseInt(splitIns[2]);
            }catch(NumberFormatException e) {
            	System.out.println("#无效指令："+instruction);
                return false;
            }
            this.direct=0;
        }
        else { //括号内容多了
        	System.out.println("#无效指令："+instruction);
            return false;
        }
        return true;
    }

    public String toString(){
        //将处理好的指令重新打印出来
        if (In_out==0){
            return "(ER,"+level+","+request_time+")";
        }
        else {
            if(direct==1) {
                return "(FR,"+level+","+"UP,"+request_time+")";
            }
            else{
                return "(FR,"+level+","+"DOWN,"+request_time+")";
            }
        }
    }

}
