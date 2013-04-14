package parsers;
/* 
 * This state is for recording where the president the president's speech.
 * The signal to switch states is detecting a signal in the ignore set of the 
 * context. Sometimes there are paragraph headers above the signal, therefore the
 * algorithm checks to see if they line above the signal has no punctuation at the end.
 */
import org.apache.commons.lang3.StringUtils;

public class PresRecordState extends State {

	public PresRecordState(Context context) {
		super(context);
	}

	@Override
	public boolean go() {
		String result;
		while (!context.EOF()) {
			result = context.containsAny(context.lines[context.cursor], context.ignore);
			if (result != null) {
				if (Character.isLetter(context.lines[context.cursor - 1].charAt(context.lines[context.cursor - 1].length()-1)))
					context.lines[context.cursor - 1] = "<ignore>" + context.lines[context.cursor - 1];
				else 
					context.lines[context.cursor] = StringUtils.replace(context.lines[context.cursor], result, "<ignore>" + result);
				context.cursor++;
				context.state = new PresidentSeekState(context);
				return true;
			}
			context.lines[context.cursor] = removeEnclosings(context.lines[context.cursor]);
			context.cursor++;						
		}
		context.state = null;
		return false;
	}

}
