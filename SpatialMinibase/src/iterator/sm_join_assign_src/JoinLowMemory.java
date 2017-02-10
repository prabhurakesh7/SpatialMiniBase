package iterator.sm_join_assign_src;

import chainexception.ChainException;

public class JoinLowMemory extends ChainException
{
	public JoinLowMemory(String s)
	{
		super(null, s);
	}
	
	public JoinLowMemory(Exception prev, String s)
	{
		super(prev, s);
	}
}
