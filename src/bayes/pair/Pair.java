package bayes.pair;

public class Pair<E, F>
{
	private E _category;
	private F _attribute;
	
	public Pair(E category, F attribute)
	{
		_category=category;
		_attribute=attribute;
	}
	
	public String toString()
	{
		return _category+" "+_attribute;
	}
	
	public E get_category() {
		return _category;
	}

	public void set_category(E _category) {
		this._category = _category;
	}

	public F get_attribute() {
		return _attribute;
	}

	public void set_attribute(F _attribute) {
		this._attribute = _attribute;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean equals(Object o)
	{
		Pair<?, ?> p=(Pair<?, ?>) o;
		return _category.equals(p.get_category())&&_attribute.equals(p.get_attribute());		
	}
	
	@Override
	public int hashCode()
	{
		return (_category.hashCode()+_attribute.hashCode())/2;		
	}
	

}
