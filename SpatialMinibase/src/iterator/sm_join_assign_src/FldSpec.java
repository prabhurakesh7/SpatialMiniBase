package iterator.sm_join_assign_src;

public class FldSpec
{
	public RelSpec relation;
	public int     offset;
	
	/**
	 * contrctor
	 *
	 * @param _relation
	 *            the relation is outer or inner
	 * @param _offset
	 *            the offset of the field
	 */
	public FldSpec(RelSpec _relation, int _offset)
	{
		relation = _relation;
		offset = _offset;
	}
}
