package Test;

public class DiningTest {
public static void main(String[] args){
        Chopstick CH = new Chopstick();
            new Philosopher(CH).start();
            new Philosopher(CH).start();
            new Philosopher(CH).start();
            new Philosopher(CH).start();
            new Philosopher(CH).start();
    }
}
class Chopstick{
    private boolean[] taking={false,false,false,false,false};
    public synchronized void release(){
	Philosopher phi=(Philosopher) Thread.currentThread();
	int Num=phi.Num;
	System.out.format("Philosopher\t%d\treleases Chopstick\n", Num);
	taking[Num]=false;
	taking[((Num+1)%5)]=false;
	notifyAll();
    }
    public synchronized void take(){
	Philosopher phi=(Philosopher) Thread.currentThread();
	int Num=phi.Num;
	while(taking[((Num+1)%5)] || taking[Num]){
            try {wait();} catch (InterruptedException e){}
        }
        System.out.format("Philosopher\t%d\ttakes Chopstick\n", Num);
        taking[Num]=true;
        taking[((Num+1)%5)]=true;
    }
}

class Philosopher extends Thread{
    int Num;
    static int Number=0;
    private Chopstick Chop;
    public Philosopher(Chopstick Chop){
	super();
	this.Chop=Chop;
        Num=Number;
	Number++;
    }
    
    private void eating(){
        System.out.format("Philosopher\t%d\tis Eating\n", Num);
        try {Thread.sleep(500);
        } catch (InterruptedException e) {}
    }
    private void thinking(){
        System.out.format("Philosopher\t%d\tis Thinking\n", Num);
        try { Thread.sleep(500);
        } catch (InterruptedException e) {}
    }
    public void run(){
        while(true){
            thinking();
            Chop.take();
            eating();
            Chop.release();
        }
    }
}
