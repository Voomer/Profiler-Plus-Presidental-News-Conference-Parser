/*
 * looks for a signal the president is speaking to switch to record state.
 */
package parsers;

import org.apache.commons.lang3.StringUtils;

public class PresidentSeekState extends State {

	public PresidentSeekState(Context context) {
		super(context);
	}

	@Override
	public boolean go() {
		String result;
		while (!context.EOF()) {
			result = context.containsAny(context.lines[context.cursor], context.record);
			if (result != null) {
				context.lines[context.cursor] = 
						StringUtils.replace(context.lines[context.cursor], result, result + "</ignore>");
				context.lines[context.cursor] = removeEnclosings(context.lines[context.cursor]);
				context.cursor++;
				context.state = new PresRecordState(context);
				return true;
			}
			
			context.cursor++;						
		}
		context.lines[context.cursor - 1] = context.lines[context.cursor - 1] + ("</ignore>");
		context.state = null;
		return false;	
	}
}
