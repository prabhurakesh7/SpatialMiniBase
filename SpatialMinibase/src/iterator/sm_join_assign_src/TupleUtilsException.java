package iterator.sm_join_assign_src;

import chainexception.ChainException;

public class TupleUtilsException extends ChainException
{
	public TupleUtilsException(String s)
	{
		super(null, s);
	}
	
	public TupleUtilsException(Exception prev, String s)
	{
		super(prev, s);
	}
}