package Test;

import java.util.concurrent.Semaphore;  
import java.util.concurrent.ThreadLocalRandom;  

public class DiningPhilosopher
{
	static int philosopher = 5;
	
	static Philosopher philosophers[] = new Philosopher[philosopher];
	
	static Chopstick chopsticks[] = new Chopstick[philosopher];

	static class Chopstick
	{
		Semaphore mutex = new Semaphore(1);
		void grab()
		{
			try
			{
				mutex.acquire();
			}
			catch(Exception e)
			{
				e.printStackTrace(System.out);
			}
		}
		
		void release()
		{
			mutex.release();
		}
		boolean isFree()
		{
			return mutex.availablePermits()>0;
		}
	}
	
	static class Philosopher extends Thread
	{
		int number;
		Chopstick leftChopstick;
		Chopstick rightChopstick;
		
		Philosopher(int n,Chopstick left, Chopstick right)
		{
			number = n;
			leftChopstick = left;
			rightChopstick = right;
		}
		
		public void run()
		{
			while(true)
			{
				leftChopstick.grab();
				System.out.println("Philosopher "+(number+1)+"grabs left chopstick");
				rightChopstick.grab();
				System.out.println("Philosopher "+(number+1)+"grabs right chopstick");
				
				eat();
				
				leftChopstick.release();
				System.out.println("Philosopher "+(number+1)+"releases left chopstick");
				rightChopstick.release();
				System.out.println("Philosopher "+(number+1)+"releases right chopstick");
			}
		}
		void eat()
		{
			
			try
			{
				int sleepTime = ThreadLocalRandom.current().nextInt(0,1000);
				System.out.println("Philosopher "+(number+1)+"eats for "+sleepTime+"ms");
				Thread.sleep(sleepTime);
			}
			catch(Exception e)
			{
				e.printStackTrace(System.out);
			}
		}
	}
	
	public static void main(String args[])
	{
		for(int i=0;i<philosopher;i++)
		{
			chopsticks[i] = new Chopstick();
		}
		for(int i=0;i<philosopher;i++)
		{
			philosophers[i] = new Philosopher(i,chopsticks[i],chopsticks[(i+1)%philosopher]);
			philosophers[i].start();
		}
		
		while(true)
		{
			try
			{
				Thread.sleep(2000);
				
				boolean deadlock = true;
				
				for(Chopstick cs:chopsticks)
				{
					if(cs.isFree())
					{
						deadlock = false;
						break;
					}
				}
				if(deadlock)
				{
					Thread.sleep(2000);
					System.out.println("Everyone eats");
					break;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace(System.out);
				break;
			}
		}
		System.out.println("Everyone Eats");
		System.exit(0);
	}
}