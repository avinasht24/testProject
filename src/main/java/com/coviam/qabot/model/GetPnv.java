package com.coviam.qabot.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
/**
 * Created by avinash.t
 */
public class GetPnv {

	public String getPnv(String host,String inputFromController)
	{
		String line = null;
		String line2 = null;

	try{
		//String[] inputList = inputFromController.split(" ");
		String pnvUser=inputFromController;
		//String host="172.20.0.221";
		System.out.println("name"+pnvUser);

		String script = "/Users/avinash.t/Avinash/PNVCreation/getPnv.sh";
        String targetTemp = script.concat(" ").concat(pnvUser).concat(" ").concat(host);


        // String target = new String("src/linuxConnect.sh linux1-user 172.20.32.100 /opt/tomcat_app_x_cart/apache-tomcat-7.0.53/logs/x/cart/app.log");

        String target = new String(targetTemp);
		System.out.println("Target"+target);

        Runtime rt = Runtime.getRuntime();
//        Process proc = null;
        boolean proc = rt.exec(target).waitFor(5, TimeUnit.SECONDS);
		
				//proc.waitFor(10,TimeUnit.SECONDS);
			
				 String fileName = "/Users/avinash.t/Avinash/PNVCreation/pnv.txt";

			        // This will reference one line at a time
			         line2 = null;

			       
			            // FileReader reads text files in the default encoding.
			            FileReader fileReader = 
			                new FileReader(fileName);

			            // Always wrap FileReader in BufferedReader.
			            BufferedReader bufferedReader = 
			                new BufferedReader(fileReader);

			            while((line2 = bufferedReader.readLine()) != null) {
			                System.out.println("Inside File:"+line2);
			                line=line2;
			            }   

			            // Always close files.
			            bufferedReader.close();    
				
				
//            StringBuffer output = new StringBuffer();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//             line = "";
//				while ((line = reader.readLine())!= null) {
//				    output.append(line + "\n");
//				    System.out.println("output of line"+line);
//
//				}
			
	}
	 catch (Throwable t)
    {
        t.printStackTrace();
    }
    System.out.println("returning answer:"+line);

    return line;

	}
	
}
