package elevator;

public class Elevator {
    //电梯类，控制电梯的上行下行
    private int level;//所在的楼层
    private int des_level;//要去的楼层
    private int direction;//1UP/-1DOWN/0still
    private double runtime;//电梯运行的时间
    private double unit_time;//运行的单位时间为5毫秒
    public Elevator()
    {
        this.level=1;
        this.des_level=1;
        this.direction=0;
        this.runtime=0;
        this.unit_time=0.5F;
    }

    public double getUnit_time() {
        return unit_time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDes_level() {
        return des_level;
    }

    public void setDes_level(int des_level) {
        this.des_level = des_level;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public double getRuntime() {
        return runtime;
    }

    public void setRuntime(double runtime) {
        this.runtime = runtime;
    }

    public void ele_Up(int temp_level){
        direction=1;
        //runtime=runtime+unit_time*(temp_level-level)+unit_time*2;
        runtime=runtime+unit_time*(temp_level-level);
        level=temp_level;
        System.out.println("("+level+",UP,"+runtime+")");
        runtime += unit_time*2;
    }//电梯上行的操作

   public void ele_Down(int temp_level){
       direction=-1;
       //runtime=runtime+unit_time*(level-temp_level)+unit_time*2;
       runtime=runtime+unit_time*(level-temp_level);
       level=temp_level;
       System.out.println("("+level+",DOWN,"+runtime+")");
       runtime += unit_time*2;
    }//电梯下行的操作

   
    public void ele_open_close(){
        direction=0;
        runtime=runtime+unit_time*2;
        System.out.println("("+level+",STILL,"+runtime+")");
   }//电梯开门的操作

}
