package vcm_main;

public class MyTimer 
{
	public MyTimer(){}

	public static void tik()
	{
		m_Localtimer = System.nanoTime();
	}
	public static long tak()
	{
		return (System.nanoTime() - m_Localtimer)/100000;  //divide by 1000000 to get milliseconds. 
	}
	public static void Go2Sleep(long sleepTime_millsec)
	{
		try
		{
			Thread.sleep(sleepTime_millsec);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}	
	}
	// Members 
	private static long m_Localtimer;
}
