package artifacts;

import java.io.File;

import org.xframium.artifact.AbstractArtifact;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.reporting.ExecutionContextStep;

public class ReportArtifact extends AbstractArtifact 
{

	        
	        public ReportArtifact()
	        {
	            //
	            // Starting with TAB_ adds a new tab to the report. Neither creates the artifact but does not link it 
	            //
	            setArtifactType( "EXECUTION_REPORT_HTML" );
	        }
	        
	        private void addStep( ExecutionContextStep step, StringBuilder stepMap, String indentAppend )
	        {
	            stepMap.append( indentAppend ).append( step.getStep().getName() ).append( "(" ).append(  step.getStep().getKw() ).append( ")" ).append( "\r\n" );
	            if ( step.getStepList() != null && !step.getStepList().isEmpty() )
	            {
	                String newAppend = indentAppend + "   ";
	                for ( ExecutionContextStep s : step.getStepList() )
	                {
	                    addStep( s, stepMap, newAppend );
	                }
	            }
	        }
	
	

	    	@Override
	    	protected File _generateArtifact(File rootFolder, DeviceWebDriver webDriver, String xFID) throws Exception {
	    		StringBuilder stepMap = new StringBuilder();
	            
	            for ( ExecutionContextStep s : webDriver.getExecutionContext().getStepList() )
	            {
	                addStep( s, stepMap, "" );
	            }

	            return writeToDisk( rootFolder, "webHomeArtifact.txt", stepMap.toString().getBytes() );

	        }
	    	
	    	 
}
