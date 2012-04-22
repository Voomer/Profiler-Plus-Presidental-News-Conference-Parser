/* one of the requirements of the project is to skip the first statement the president makes
 * because its usually just him updating the status of various things in his administration.
 * this class seeks the first ignore signal, and if it doesnt find one, then the whole document
 * itself is just an update status.
 */

package parsers;


public class PreProcessState extends State{
	

	public PreProcessState(Context context) {
		super(context);
	}

	@Override
	public boolean go() {
		//look for the first
		while (!context.EOF()) {
			if (context.containsAny(context.lines[context.cursor], context.ignore) != null) {
				context.state = new PresidentSeekState(context);
				context.lines[0] = "<ignore>" + context.lines[0];
				return true;
			}
			context.cursor = context.cursor + 1;						
		}
		for (int i = 0; i < context.length; i++) {
			context.lines[i] = removeEnclosings(context.lines[i]);
		}
		return false;		
	}
}
