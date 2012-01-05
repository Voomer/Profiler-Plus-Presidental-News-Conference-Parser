/*
 * abstract state that has some built in functionality to check for enclosings to remove.
 * the go method is the method that that algorithm should go in, it returns false if there is
 * no more lines to process.
 */
package parsers;



import org.apache.commons.lang.StringUtils;

public abstract class State {
	
	protected Context context;
	public State(Context context) {
		this.context = context;
	}
	
	protected String removeEnclosings(String currentline)
	{
		currentline = StringUtils.replace(currentline, "[", "<ignore>[");		
		currentline = StringUtils.replace(currentline, "]", "]</ignore>");
		currentline = StringUtils.replace(currentline, "(", "<ignore>(");		
		currentline = StringUtils.replace(currentline, ")", ")</ignore>");
		//System.out.println(currentline);
		return currentline;
	}
	
	abstract public boolean go();

}
