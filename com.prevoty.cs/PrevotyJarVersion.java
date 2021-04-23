import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

public class PrevotyJarVersion {

	// Agent string
	private static final String AGENT_STRING = "agent";
	
	// Agent string
	private static final String RASP_STRING = "rasp";

	// Complete path to the Prevoty JAR file
	String prevotyJarPath = "";
	
	// The JAR file name
	String jarFileName = "";
	
	// Version from the JAR file
	String jarFileVersion = "";
	
	// Version from the Boot-Class-Path
	String bootClassPathVersion = "";

	// Version from the X-Prevoty-Version
	String xPrevotyVersion = "";
	
	// Component from JAR file (agent │ rasp)
	String jarFileComponent = "";
	
	// Component from Premain-Class (agent only)
	String premainClassComponent = "";
	
    // Display how to use the program
    void displayHelp(String msg) {
    	System.out.println("╔══════════════════════════════════════════════════════");
        System.out.println("║ PrevotyJarVersion, Delivers and checks the version of a Prevoty JAR file.");
        System.out.println("║ Version: 1.0 - Sat Sep 22, 2018 - PWR Created");
        System.out.println("║ Usage:   java -jar PrevotyJarVersion.jar <Prevoty JAR file path>");
        System.out.println("║ Example: java -jar PrevotyJarVersion.jar /opt/Apache/Tomcat-8.5.5/Prevoty/prevoty-agent.jar");
        System.out.println("║ Notes:   - Written by PWR on his own accord; Prevoty cannot be held liable for any errors, mistakes, omissions, etc.");
        System.out.println("║          - USE AT YOUR OWN RISK!");
        if (( msg != null ) && ( msg.length() > 0 )) {
            System.out.println("║ Error:   " + msg ); 
        }
    	System.out.println("╚══════════════════════════════════════════════════════");
    }

    // Constructor
	PrevotyJarVersion( String[] arguments ) {
        // Do we have a file name?
        if (( arguments != null ) && ( arguments.length > 0 )){
            // Get the Prevoty results log file
        	for (int i = 0; i < arguments.length; i++ ) {
	    		// Is there a next argument?
	    		if ( prevotyJarPath.length() > 0 ) {
	    			prevotyJarPath += " ";
	    		}
	    		prevotyJarPath += arguments[i];
	    	}
        }
        else {
        	// No JAR file name provided
        	displayHelp("Please specify the complete path of the Prevoty JAR file name.");
        }
	}
	
	// Get the component from a String (rasp │ agent)
	String getComponentString(String theAttribute, String theString) {
		// Set the return value
		String retVal = null;
		// Do we have a string?
		if ((theString != null) && (theString.length() > 0)) {
			// Does it contain the word agent?
			if ((theString.indexOf("-"+AGENT_STRING+"-") != -1) || (theString.indexOf("-"+AGENT_STRING+".") != -1) || (theString.indexOf("."+AGENT_STRING+".") != -1)){
				// Return agent
				retVal = AGENT_STRING;
			}
			else {
				// Does it contain the word agent?
				if ((theString.indexOf("-"+RASP_STRING+"-") != -1) || (theString.indexOf("-"+RASP_STRING+".") != -1) || (theString.indexOf("."+RASP_STRING+".") != -1)){
					// Return rasp
					retVal = RASP_STRING;
				}
			}
		}
		// Print the result
		// System.out.println( theAttribute + " (component): " + retVal + "");
		// return the results
		return retVal;
	}
	
	// Get a version number from a string
	String getVersionString(String theAttribute, String theString) {
		// Set the return value
		String retVal = null;
		// Do we have a string?
		if ((theString != null) && (theString.length() > 0)) {
			// Does it contain ".jar"
			if(theString.endsWith(".jar")) {
				// Strip the ".jar"
				theString = theString.substring(0, theString.indexOf(".jar"));
			}
			// Not in version
			boolean inVersion = false;
			// Version string
			String versionString = null;
			// Character at the index
			char theKey = ' ';
			// Loop through the string and pick-up x.y.z
			for(int i = 0; i < theString.length(); i++) {
				// Get the key at the index
				theKey = theString.charAt(i);
				// Is it a dot and are we in the version?
				if ((theKey == '.') && (inVersion)) {
					// Add the dot to the version
					versionString += theKey;
				}
				// Is it a number?
				if ((theKey >= '0') && (theKey <= '9')) {
					// Are we in the version?
					if (inVersion) {
						// Add the number to the version
						versionString += theKey;
					}
					else {
						// We are now in the version
						inVersion = true;
						// Start the version
						versionString = "" + theKey;
					}
				}
			}
			// Copy the version string
			retVal = versionString;
		}
		// Print the result
		// System.out.println( theAttribute + " (version): " + retVal + "");
		// return the results
		return retVal;
	}
	
	// Parse the JAR file and get the versions
	boolean getVersions() {
		// Set the return value
		boolean retVal = false;
		// Do we have a Prevoty JAR file path?
		if ((prevotyJarPath != null) && (prevotyJarPath.length() > 0)) {
			// Get the path to the JAR file
			Path path = Paths.get(prevotyJarPath);
			try {
				// Create the buffered input stream from the path
				BufferedInputStream stream = new BufferedInputStream(Files.newInputStream(path));
				// Create the JAR stream from the buffered input stream
				JarInputStream jarStream = new JarInputStream(stream);
				// Read the manifest from the JAR stream
				Manifest mf = jarStream.getManifest();
				// Close the JAR stream
				jarStream.close();
				// Did we got the manifest?
				if (mf != null) {
					// Get the main attributes
					Attributes mainAttributes = mf.getMainAttributes();
					// Get the JAR file name
					jarFileName = path.getFileName().toString();
					// Get the JAR file component string
					jarFileComponent = getComponentString("JAR file name", jarFileName);
					// Get the JAR file version number
					jarFileVersion = getVersionString("JAR file name", jarFileName);
					// Get the Premain-Class component string
					premainClassComponent = getComponentString("Manifest Premain-Class", mainAttributes.getValue("Premain-Class"));
					// Get the Boot-Class-Path version number
					bootClassPathVersion = getVersionString("Manifest Boot-Class-Path", mainAttributes.getValue("Boot-Class-Path"));
					// Get the X-Prevoty-Version version number
					xPrevotyVersion = getVersionString("Manifest X-Prevoty-Version", mainAttributes.getValue("X-Prevoty-Version"));
					// All is well
					retVal = true;
				}
				else {
					// Could not read the manifest from the JAR stream
					displayHelp("Could not read the manifest from the Prevoty JAR file \"" + prevotyJarPath + "\"");
				}
			}
			catch (IOException e) {
				// Could not read the provide Prevot JAR file
				displayHelp("Could not read the Prevoty JAR file \"" + prevotyJarPath + "\"");
			}
		}
		// Return the result
		return retVal;
	}

	/* Check the versions according to the following table:
	 *  
	 * ┌────────────────────┬─────────────────┬───────────────────────────────────────────────┐
	 * │                    │JAR file name    │Manifest                                       │
	 * │Prevoty JAR file    ├─────────┬───────┼─────────────┬───────────────┬─────────────────┤
	 * │                    │Component│Version│Premain-Class│Boot-Class-Path│X-Prevoty-Version│
	 * ├────────────────────┼─────────┼───────┼─────────────┼───────────────┼─────────────────┤
	 * │Agent - version     │agent    │x.y.z  │agent        │x.y.z          │null             │
	 * │Agent - version-less│agent    │null   │agent        │null           │x.y.z            │
	 * │RASP  - version     │rasp     │x.y.z  │null         │null           │x.y.z            │
	 * │RASP  - version-less│rasp     │null   │null         │null           │x.y.z            │
	 * └────────────────────┴─────────┴───────┴─────────────┴───────────────┴─────────────────┘
	 */
	void checkVersions() {
		// Print the file name
		System.out.println("Filename:  " + jarFileName);
		// Do we at least have a component?
		if(jarFileComponent != null) {
			// Is it an agent?
			if(jarFileComponent.equalsIgnoreCase(AGENT_STRING)) {
				// Print the component
				System.out.println("Component: " + jarFileComponent);
				// Is it a version agent JAR?
				if ((jarFileVersion != null) && (jarFileVersion.length() > 0) &&
					(premainClassComponent != null) && (premainClassComponent.length() > 0 ) && (premainClassComponent.equalsIgnoreCase(AGENT_STRING)) &&
					(bootClassPathVersion != null) && (bootClassPathVersion.length() > 0 ) &&
					(xPrevotyVersion == null) &&
					(jarFileVersion.equalsIgnoreCase(bootClassPathVersion))) {
					// Print the type
					System.out.println("Type:      with version");
					// Print the version
					System.out.println("Version:   " + bootClassPathVersion);
				}
				else {
					// Is it a version-less agent JAR?
					if ((jarFileVersion == null) &&
						(premainClassComponent != null) && (premainClassComponent.length() > 0 ) && (premainClassComponent.equalsIgnoreCase(AGENT_STRING)) &&
						(bootClassPathVersion == null) &&
					    (xPrevotyVersion != null) && (xPrevotyVersion.length() > 0)) {
						// Print the type
						System.out.println("Type:      version-less");
						// Print the version
						System.out.println("Version:   " + xPrevotyVersion);
					}
					else {
						// JAR file has been tempered with and needs to be replaced
						System.out.println("ERROR:     JAR has been tempered with; both Prevoty JAR files need to be replaced");
					}
				}
			}
			else {
				// Is it rasp?
				if(jarFileComponent.equalsIgnoreCase(RASP_STRING)) {
					// Print the component
					System.out.println("Component: " + jarFileComponent);
					// Is it a version RASP JAR?
					if ((jarFileVersion != null) && (jarFileVersion.length() > 0) &&
						(premainClassComponent == null) &&
						(bootClassPathVersion == null) &&
					    (xPrevotyVersion != null) && (xPrevotyVersion.length() > 0) &&
					    (jarFileVersion.equalsIgnoreCase(xPrevotyVersion))) {
						// Print the type
						System.out.println("Type:      with version");
						// Print the version
						System.out.println("Version:   " + xPrevotyVersion);
					}
					else {
						// Is it a version-less RASP jar?
						if ((jarFileVersion == null) &&
							(premainClassComponent == null) &&
							(bootClassPathVersion == null) &&
						    (xPrevotyVersion != null) && (xPrevotyVersion.length() > 0)) {
							// Print the type
							System.out.println("Type:      version-less");
							// Print the version
							System.out.println("Version:   " + xPrevotyVersion);
						}
						else {
							// JAR file has been tempered with and needs to be replaced
							System.out.println("ERROR:     JAR has been tempered with; both Prevoty JAR files need to be replaced");
						}
					}
				}
				else {
					// Component was not found
					System.out.println("ERROR:     Not a Prevoty JAR file ("+jarFileComponent+")");
				}
			}
		}
		else {
			// Component was not found
			System.out.println("ERROR:     Not a Prevoty JAR file");
		}
	}
	
	// Check the version of a Prevoty JAR file
	public static void main(String[] args) {
		// Create the object
		PrevotyJarVersion prevotyJarVersion = new PrevotyJarVersion( args );
		// Can we get the versions from the the JAR file?
		if(prevotyJarVersion.getVersions()) {
			// Check the versions
			prevotyJarVersion.checkVersions();
		}
	}
}