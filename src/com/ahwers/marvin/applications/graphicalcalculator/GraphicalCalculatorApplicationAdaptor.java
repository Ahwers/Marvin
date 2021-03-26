package com.ahwers.marvin.applications.graphicalcalculator;

import java.util.Map;

import com.ahwers.marvin.CommandExecutionOutcome;
import com.ahwers.marvin.CommandStatus;
import com.ahwers.marvin.applications.Application;
import com.ahwers.marvin.applications.ApplicationAdaptor;
import com.ahwers.marvin.applications.CommandMatch;
import com.ahwers.marvin.applications.IntegratesApplication;

// TODO: If we go centralised, we could add a javascriptToRun field to the MarvinResponse/CommandExecutionOutcome class that clients can then run on their own brower managers.
//		 But it's probably best to be stateless and always return something that can work without relying on a previously sent resource. 
@IntegratesApplication("Graphical Calculator")
public class GraphicalCalculatorApplicationAdaptor extends ApplicationAdaptor {
	
	@Override
	protected Application instantiateApplication() {
		return new LocalGraphicalCalculatorApplication();
	}

	@CommandMatch("^plot (?<expression>.+)$")
	public CommandExecutionOutcome plotNewAlgebraicExpression(Map<String, String> arguments) {
		String expression = arguments.get("expression");
		
		GraphicalCalculator graphicalCalculator = (GraphicalCalculator) getApplication();
		graphicalCalculator.plotNewExpression(expression);
		
		return new CommandExecutionOutcome(CommandStatus.SUCCESS);
	}
	
	// TODO: Should I make it so that ^ and $ are not required for any matches? Yes. Actually nah, let's stay flexible until we see that the flexibility is not required.
	@CommandMatch("^remove (?:graph|expression) (?<graphIndex>.+)$")
	public CommandExecutionOutcome removeAlgebraicExpression(Map<String, String> arguments) {
		String expressionIndex = arguments.get("graphIndex");
		
		GraphicalCalculator graphicalCalculator = (GraphicalCalculator) getApplication();
		
		CommandExecutionOutcome outcome = new CommandExecutionOutcome(CommandStatus.FAILED);
		try {
			// TODO: Why does this keep asking for Exception to be handled????
			graphicalCalculator.removeExpression(Integer.valueOf(expressionIndex));
		} catch (NumberFormatException e) {
			outcome.setResponseMessage("\"Graphical Calculator\" graphs are indexed by integers.");
			outcome.setFailException(e);
		} catch (IndexOutOfBoundsException e) {
			outcome.setResponseMessage("Graph " + expressionIndex + "doesn't exist.");
			outcome.setFailException(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new CommandExecutionOutcome(CommandStatus.SUCCESS);
	}
	
}
