/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcessAndThreads;
/**
 *
 * @author Oscar
 */


public class MainApp
{
	public static void main(String[] args){
		
		// Ejecutar comandos mediante clase "Runtime":
		// Prova "notepad", "cmd /c dir", "comanda que no existeix" 
		ProcessTest.ExecuteCommand("cmd /c dir");
		
		// Ejecutar comandos mediante clase "ProcessBuilder":
		ProcessTest.ExecuteProcess("cmd", "/c", "dir");
		//~ProcessTest.ExecuteProcessOutputToFile("C:\\Users\\Oscar\\Desktop\\out.txt","C:\\Users\\Oscar\\Desktop\\err.txt", "cmd", "/c", "dir");
	}
}