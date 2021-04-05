// Exemple of custom thread. Only sleeps half second
package ProcessAndThreads;

public class ThreadClass extends Thread
{
	String name;
	public ThreadClass(String name)
	{
		super();
		this.name = name;
	}
	
	@Override
	public void run()
	{
		for(int i = 0; i < 5; i++)
		{
			System.out.println(name + ": i = " + i);
			try {
				sleep(500);
			} catch (InterruptedException ex) {
				System.out.println("Interromput");
			}
		}
	}
}