package iterator.sm_join_assign_src;

import chainexception.ChainException;

public class WrongPermat extends ChainException
{
	public WrongPermat(String s)
	{
		super(null, s);
	}
	
	public WrongPermat(Exception prev, String s)
	{
		super(prev, s);
	}
}
