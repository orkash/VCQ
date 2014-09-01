package vcm_main;

public class MySignal 
{
	private Object m_monitorObject;
	private boolean m_wasSignalled;
	private boolean m_autoReset;
	private boolean m_notifyReady;

	public MySignal(boolean initState,boolean autoReset)
	{
		m_wasSignalled = initState;
		m_autoReset = autoReset;
		m_monitorObject = new Object();
		m_notifyReady = false;
	}
	
	public void WaitForSignal()
	{
		synchronized(m_monitorObject)
		{
			if(!m_wasSignalled)
			{
				try {
					m_notifyReady = true;
					m_monitorObject.wait();
					m_notifyReady = false;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		if(m_autoReset)
		{
			m_wasSignalled = false;
		}
	}

	public void setSignal()
	{
		synchronized(m_monitorObject)
		{
			if(!m_wasSignalled)
			{
				m_wasSignalled = true;
				if(m_notifyReady)
				{
					m_monitorObject.notify();
				}
			}
		}
	}
	
	public void resetSignal()
	{
		m_wasSignalled = false;
	}
}
