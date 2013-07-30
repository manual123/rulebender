package rulebender.simulate;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.console.MessageConsoleStream;

import rulebender.core.utility.Console;
import rulebender.logging.Logger;
import rulebender.logging.Logger.LOG_LEVELS;
import rulebender.preferences.PreferencesClerk;

public class BioNetGenConsole {

	private static Process bngConsoleProcess = null;
	private static OutputStreamWriter writer = null;
	private static ConsoleReader out = null;
	public static long creationTimeOut = 5000;
	public static long check = 1000;
	
	private static void invokeBNGConsole() 
	{
	  String bngPath = PreferencesClerk.getFullBNGPath();
    // String bngPath = bng.toString();
    if (BioNetGenUtility.checkPreReq() && validateBNGPath(bngPath)) 
    {
      List<String> commands = new ArrayList<String>();
      commands.add("perl");
      commands.add(bngPath);
      commands.add("-console");
      ProcessBuilder builder = new ProcessBuilder(commands);
      try 
      {
        bngConsoleProcess = builder.start();
      } 
      catch (IOException e) 
      {
        e.printStackTrace();
      }
      writer = new OutputStreamWriter(bngConsoleProcess.getOutputStream());
      out = new ConsoleReader(bngConsoleProcess.getInputStream());
      out.start();
    }
    else
    {
      //FIXME If this condition is hit, then there will be npe's.  Need
      // inform user of why it failed because this breaks the contact map.
    }

	}

	private static boolean validateBNGPath(String path) 
	{
		if ((new File(path)).exists()) {
			return true;
		}
		return false;
	}

	// not working yet !!

	public static File generateXML(File bngModel, MessageConsoleStream errorStream) {
		String fileName = bngModel.getParentFile().toString() + "/"
		    + bngModel.getName().substring(0, bngModel.getName().indexOf(".bngl"));
		File xmlFile = new File(fileName + ".xml");
		if (xmlFile.exists()) {
			xmlFile.delete();
		}
		String writeXML = "writeXML({prefix=>\"" + fileName.replace("\\", "/")
		    + "\"})";
		// String net = xmlFile.toString();
		// net = net.substring(0, net.length() - 3) + "net";
		xmlFile.deleteOnExit();
		clearModel();
		readModel(bngModel);
		// String networkGen = "generate_network({" + "overwrite=>1,file=>\"" + net
		// + "\"})";
		// executeAction(networkGen);
		executeAction(writeXML);
		long waitTime = 0;
		while (!xmlFile.exists()) {
			try {
				waitTime += check;
				Thread.sleep(check);
				if (out.hadError()) {
					String err = out.getError();
					out.reportError();
					errorStream.println(err);
					// throw new Error("An error occurred while processing the file: " +
					// err);
					xmlFile = null;
					break;
				} else if (waitTime > creationTimeOut) {
					Logger.log(LOG_LEVELS.ERROR, BioNetGenConsole.class,
					    "Wasn't able to create the xml-file for the model!!!");
					Console.getMessageConsoleStream(bngModel.toString()).print(
					    "Wasn't able to create the xml-file for the model located at: "
					        + bngModel.toString() + "\n");
					// throw new Error(
					// "Wasn't able to create the xml-file for the model located at: "
					// + bngModel.toString());
					xmlFile = null;
					break;
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String warning = out.getWarnings();
		out.reportWarnings();
		errorStream.println(warning);
		return xmlFile;
	}

	public static void clearModel() {
		if (prepareConsole()) {
			write("clear");
		}
		
		out.reportWarnings();
	}

	public static void readModel(File bngModel) {
		if (prepareConsole()) {
			write("load " + bngModel.toString());
		}
	}

	public static void executeAction(String action) {
		if (prepareConsole()) {
			write("action " + action);
		}
	}

	private static boolean prepareConsole() {
		if (bngConsoleProcess == null) {
			invokeBNGConsole();
			if (bngConsoleProcess == null) {
				return false;
			}
		}
		return true;
	}

	public static void write(String s) {
		try {
			writer.write(s + "\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// generating an ast out of an xml is not working yet

	// public static prog_return generateASTFromXML(File xmlFile) {
	// prog_return ast = new prog_return();
	// BufferedReader reader = null;
	// try {
	// reader = new BufferedReader(new FileReader(xmlFile));
	// String line;
	// while ((line = reader.readLine()) != null) {
	// StringTemplate temp = new StringTemplate();
	// temp.setTemplate(line);
	// XML4JDOMAdapter ad = new XML4JDOMAdapter();
	// try {
	// ad.getDocument(xmlFile, true);
	// ad.createDocument();
	// } catch (JDOMException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// ast.st = temp;
	// }
	//
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } finally {
	// try {
	// if (reader != null) {
	// reader.close();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// return ast;
	// }
}
