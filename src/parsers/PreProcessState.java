/* one of the requirements of the project is to skip the first statement the president makes
 * because its usually just him updating the status of various things in his administration.
 * this class seeks the first ignore signal, and if it doesnt find one, then the whole document
 * itself is just an update status.
 */

package parsers;

import org.apache.commons.lang3.StringUtils;


public class PreProcessState extends State{
	private String ignoreHeader;

	public PreProcessState(Context context, String skipFirst) {
		super(context);
		this.ignoreHeader = skipFirst;
	}

	@Override
	public boolean go() {
		//look for the first
				if (ignoreHeader.equals("true")) 
					context.state = new PresidentSeekState(context);
				else {
					context.state = new PresRecordState(context);
					String result = context.containsAny(context.lines[context.cursor], context.record);
					context.lines[context.cursor] = 
							StringUtils.replace(context.lines[context.cursor], result, result + "</ignore>");
				}
				context.lines[0] = "<ignore>" + context.lines[0];
				return true;		
	}
}
