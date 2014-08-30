package vcm_main;

public class MyTimer 
{
	void MyTimer(){}

	public static void tik()
	{
		m_Localtimer = System.nanoTime();
	}
	public static long tak()
	{
		return (System.nanoTime() - m_Localtimer)/100000;  //divide by 1000000 to get milliseconds. 
	}
	
	// Members 
	private static long m_Localtimer;
}
