
package ProcessAndThreads;


public class WaitNotifyExample
{
    
    public static void test() {
        escriuHolaAdeu eha = new escriuHolaAdeu();
        E_Adeu e_Adeu = new E_Adeu(eha);
        E_Hola e_Hola = new E_Hola(eha);
    }
}
 
class E_Hola implements Runnable {
    escriuHolaAdeu eh;
    E_Hola (escriuHolaAdeu eh)
    {
        this.eh = eh;
        new Thread(this, "Hola").start();
    }
	@Override
    public void run() {
        try{
            for(int x=0;x<5;x++) {
                Thread.sleep(1000);
                eh.eHola();
            }
        }catch (InterruptedException e){}
    }
}
 
class E_Adeu implements Runnable {
    escriuHolaAdeu eh;
    E_Adeu (escriuHolaAdeu eh) {
        this.eh = eh;
        new Thread(this, "Adéu").start();
    }
	
	@Override
    public void run() {
        for(int x=0;x<5;x++) {
            eh.eAdeu();
        }
    }
}
 
class escriuHolaAdeu {

    boolean escritHola = false;

    public synchronized void eAdeu() {
        if (escritHola == false) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        escritHola = false;
        System.out.println(" Adéu ");
    }

    public synchronized void eHola() {
        System.out.println(" Hola ");
        escritHola = true;
        notify();
    }
    
}