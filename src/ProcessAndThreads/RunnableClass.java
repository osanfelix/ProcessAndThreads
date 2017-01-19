package ProcessAndThreads;

import static java.lang.Thread.sleep;
public class RunnableClass implements Runnable
{
	String name;
	
	public RunnableClass(String name)
	{
		this.name = name;
	}

	@Override
	public void run()
	{
		for(int i = 0; i < 5; i++)
		{
			System.out.println("Fil " + name + ": i = " + i);
			try {
				sleep(500);
			} catch (InterruptedException ex) {
				System.out.println("Interrumpit");
			}
		}
	}
}