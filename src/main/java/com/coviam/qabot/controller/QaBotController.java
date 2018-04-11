package com.coviam.qabot.controller;

import java.awt.List;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.coviam.database.BotConfiguration;
import com.coviam.database.BugCreationJira;
import com.coviam.database.GetBotInformation;
import com.coviam.database.GetDatabaseDetails;
import com.coviam.qabot.model.GetPnv;
import com.coviam.qabot.model.QaBotBugCreationModel;
import com.coviam.qabot.model.QaBotConfigModel;
//import com.coviam.blibli.quiz.model.UserCredentials;
import com.coviam.qabot.model.QaBotModel;

import org.springframework.web.bind.annotation.RestController;
/**
 * Created by avinash.t
 */

@Controller
public class QaBotController {
//	@RequestMapping(value="/" , method=RequestMethod.POST)
//	public void method(String inputFromWeb)
//	{
//		
//		System.out.println("test");
//		
//	}

//	@Autowired
//	pu Properties botProp;

	String inputFromBot;

   public static Properties botProp=null;

    public static InputStream botPropFile=null;

    @RequestMapping("/test")
	public String test(){
    	return "I am alive";
	}

	@RequestMapping(value="/qabot", method=RequestMethod.GET)
	public ModelAndView showForm()
	{
		System.out.println("Testing");
	    botProp= new Properties();


    String file = "/Users/avinash.t/Downloads/QaBotWebSpringBoot/TechBot.properties";
	            botPropFile = this.getClass().getClassLoader().getResourceAsStream(file);
try {
	            botProp.load(new FileInputStream("/Users/avinash.t/Downloads/QaBotWebSpringBoot/TechBot.properties"));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

		System.out.println("JSPNOTGETTING");

	//return "index";
		return new ModelAndView("index");
	}
	
	@RequestMapping(value="botconfig", method=RequestMethod.GET)
	public ModelAndView showConfigForm()
	{
		System.out.println("Calling Config Controller");
	 
		
		return new ModelAndView("botconfig");
	}
	
	@RequestMapping(value="bugcreation", method=RequestMethod.GET)
	public ModelAndView showBugForm()
	{
		System.out.println("Calling Bug Controller");
	 
		
		return new ModelAndView("bugcreation");
	}
	
	
	
	@RequestMapping(value="index", method=RequestMethod.GET)
	public ModelAndView showMainForm()
	{
		System.out.println("Calling Main Controller");
	 
		
		return new ModelAndView("/");
	}
	
	@RequestMapping(value="newbugcreation", method=RequestMethod.POST)
	public ModelAndView newBugCreation(@ModelAttribute("bugDetails")QaBotBugCreationModel createBug)
	{
		System.out.println("Getting Bug details");
		System.out.println("Jira Username  :"+createBug.getJira_username());
		System.out.println("Project Added is  :"+createBug.getProject_code());
		System.out.println("Project Title is  :"+createBug.getTitle_summary());
		System.out.println("Bug Description is  :"+createBug.getBug_description());

		
		String jiraStatus="false";
		BugCreationJira bc=new BugCreationJira();
		
		
		try {
			jiraStatus=bc.createNewJiraTicket(createBug);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createBug.setInsertFlag(jiraStatus);
		return new ModelAndView("bugcreation");
	}
	
	
	@RequestMapping(value="botconfiginsert", method=RequestMethod.POST)
	public ModelAndView getBotConfig(@ModelAttribute("configDetails")QaBotConfigModel config)
	{
		System.out.println("Checking configuration details");
		System.out.println("Environement selected is  :"+config.getEnvironment());
		System.out.println("Source selected is  :"+config.getSource_type());
		System.out.println("Keyword entered is  :"+config.getBot_keyword());
		
		if(config.getSource_type().equals("PROMOTIONCODE"))
		{
			
		}
		
		System.out.println("Url Entered  is  :"+config.getUrl());
		String insertFlag="true";
		BotConfiguration bc=new BotConfiguration();

		if(config.getSource_type().equals("PROMOTIONCODE"))
		{
			insertFlag=bc.insertBotConfiguration(config.getEnvironment(), config.getSource_type(), "promotioncode", config.getUrl());

		}
		else
		{
			insertFlag=bc.insertBotConfiguration(config.getEnvironment(), config.getSource_type(), config.getBot_keyword().toLowerCase(), config.getUrl());

		}
		config.setInsertFlag(insertFlag);
		return new ModelAndView("botconfig");
	}
	
	@RequestMapping(value="botconfigresults", method=RequestMethod.POST)
	public ModelAndView getBotConfigResults() 
	{

		BotConfiguration bc=new BotConfiguration(); 
		java.util.List<QaBotConfigModel> qaBotConfigs = bc.getAllConfigData();
		ModelAndView model= new ModelAndView("botconfig");
		model.addObject("qaBotConfigs",qaBotConfigs);
		System.out.println("qqbotconfig"+qaBotConfigs);
		
		return model;
		
		
	}
	
	@RequestMapping(value="/getInputAPI", method=RequestMethod.GET,produces={MediaType.APPLICATION_JSON_VALUE})
	public QaBotModel getInputAPI(@RequestParam(value="inputValue", defaultValue="Hello") String inputValue)
	{
		System.out.println("from input api: "+inputValue);
		 inputFromBot=inputValue.toLowerCase();
//		 QaBotModel model=new QaBotModel();
		//Start calling the routers
		if (inputFromBot.contains("db") || inputFromBot.contains("database"))
        {
             if(inputFromBot.contains("qa 2") || inputFromBot.contains("qa2"))
             {
            	 String dbResults= dbParser("qa2");
	    			return new QaBotModel(dbResults);

             }
             else  if(inputFromBot.contains("qa 1") || inputFromBot.contains("qa1"))
             {
            	 String dbResults=dbParser("qa1");
	    			return new QaBotModel(dbResults);

             }
             else  if(inputFromBot.contains("dev 1") || inputFromBot.contains("dev1"))
             {
            	 String dbResults= dbParser("dev1");
	    			return new QaBotModel(dbResults);

             }
             else   if(inputFromBot.contains("dev2") || inputFromBot.contains("dev2"))
             {
                 String dbResults=dbParser("dev2");
	    			return new QaBotModel(dbResults);

             }
             else   if(inputFromBot.contains("preprod") || inputFromBot.contains("pre-prod"))
             {
                 String dbResults=dbParser("preprod");
	    			return new QaBotModel(dbResults);

             }
             else
             {
                 System.out.println("Mention which uat, the order details are required (qa1/qa2/dev1/dev2/pre-prod) and try again  ");
	    			return new QaBotModel("Mention which uat, the order details are required (qa1/qa2/dev1/dev2/pre-prod) and try again  ");
                
             }
             
        }
		 else if (inputFromBot.contains("pnv") && !inputFromBot.contains("user"))
	        {

	            System.out.println("\nMake sure redis is installed in local machine (To install type brew install redis)\n");
	            if(inputFromBot.contains("qa2") || inputFromBot.contains("qa 2"))
	            {
	            	String pnvputput=pnvParser("xOtpQa2");
	    			return new QaBotModel(pnvputput);

	            }
	            else if(inputFromBot.contains("qa1") || inputFromBot.contains("qa 1"))
	            {
	            	String pnvputput=pnvParser("xOtpQa1");
	    			return new QaBotModel(pnvputput);

	            }
	            else if(inputFromBot.contains("dev1") || inputFromBot.contains("dev 1"))
	            {
	            	String pnvputput= pnvParser("xOtpDev1");
	    			return new QaBotModel(pnvputput);

	            }
	            else if(inputFromBot.contains("dev 2") || inputFromBot.contains("dev2"))
	            {
	                String pnvputput=pnvParser("xOtpDev2");
	    			return new QaBotModel(pnvputput);

	            }
	            else if(inputFromBot.contains("pre-prod") || inputFromBot.contains("preprod"))
	            {
	                String pnvputput=pnvParser("xOtpPreProd");
	    			return new QaBotModel(pnvputput);

	            }
	            else
	            {
	                System.out.println("Mention which uat, the pnv details are required (qa1/qa2/dev1/dev2) : ");
	                return new  QaBotModel("Mention which uat, the pnv details are required (qa1/qa2/dev1/dev2) : ");
	            }
	        }
	            else if(inputFromBot.contains("api"))
	   		 {
	   			 if(inputFromBot.contains("qa 2") || inputFromBot.contains("qa2"))
	   	            {
	   	                String apiOutput=apiParser(inputFromBot,"QA-2");
		    			return new QaBotModel(apiOutput);

	   	            }
	   	            else if(inputFromBot.contains("qa 1") || inputFromBot.contains("qa1"))
	   	            {
	   	            	String apiOutput= apiParser(inputFromBot,"QA-1");
		    			return new QaBotModel(apiOutput);

	   	            }
	   	            else if(inputFromBot.contains("dev 1") || inputFromBot.contains("dev1"))
	   	            {
	   	            	String apiOutput=apiParser(inputFromBot,"DEV-1");
		    			return new QaBotModel(apiOutput);

	   	            }
	   	            else  if(inputFromBot.contains("dev 2") || inputFromBot.contains("dev2"))
	   	            {
	   	            	String apiOutput= apiParser(inputFromBot,"DEV-2");
		    			return new QaBotModel(apiOutput);

	   	            }
	   	            else  if(inputFromBot.contains("pre-prod") || inputFromBot.contains("pre prod"))
	   	            {
	   	            	String apiOutput=apiParser(inputFromBot,"PRE-PROD");
		    			return new QaBotModel(apiOutput);

	   	            }
	   	            else
	   	            {
	   	                System.out.println("Mention which uat, the api url details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
		    			return new QaBotModel("Mention which uat, the api url details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	   	            } 
	   		 }
	            else if(inputFromBot.contains("center"))
	   		 {
	   			 if(inputFromBot.contains("qa 2") || inputFromBot.contains("qa2"))
	   	            {
	   	                String centerOutput=centerParser(inputFromBot,"QA-2");
		    			return new QaBotModel(centerOutput);
	   	            }
	   	            else if(inputFromBot.contains("qa 1") || inputFromBot.contains("qa1"))
	   	            {
	   	            	String centerOutput= centerParser(inputFromBot,"QA-1");
		    			return new QaBotModel(centerOutput);

	   	            }
	   	            else if(inputFromBot.contains("dev 1") || inputFromBot.contains("dev1"))
	   	            {
	   	            	String centerOutput=centerParser(inputFromBot,"DEV-1");
		    			return new QaBotModel(centerOutput);

	   	            }
	   	            else  if(inputFromBot.contains("dev 2") || inputFromBot.contains("dev2"))
	   	            {
	   	            	String centerOutput= centerParser(inputFromBot,"DEV-2");
		    			return new QaBotModel(centerOutput);

	   	            }
	   	            else  if(inputFromBot.contains("pre-prod") || inputFromBot.contains("pre prod"))
	   	            {
	   	            	String centerOutput=centerParser(inputFromBot,"PRE-PROD");
		    			return new QaBotModel(centerOutput);

	   	            }
	   	            else
	   	            {
	   	                System.out.println("Mention which uat, the center url details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
		    			return new QaBotModel("Mention which uat, the center url details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	   	            } 
	   		 }
	            else if(inputFromBot.contains("product"))
	   		 {
	   			 if(inputFromBot.contains("qa 2") || inputFromBot.contains("qa2"))
	   	            {
	   	                String productOutput=productParser(inputFromBot,"QA-2");
		    			return new QaBotModel(productOutput);

	   	            }
	   	            else if(inputFromBot.contains("qa 1") || inputFromBot.contains("qa1"))
	   	            {
	   	            	String productOutput= productParser(inputFromBot,"QA-1");
		    			return new QaBotModel(productOutput);

	   	            }
	   	            else if(inputFromBot.contains("dev 1") || inputFromBot.contains("dev1"))
	   	            {
	   	            	String productOutput=productParser(inputFromBot,"DEV-1");
		    			return new QaBotModel(productOutput);

	   	            }
	   	            else  if(inputFromBot.contains("dev 2") || inputFromBot.contains("dev2"))
	   	            {
	   	            	String productOutput= productParser(inputFromBot,"DEV-2");
		    			return new QaBotModel(productOutput);

	   	            }
	   	            else  if(inputFromBot.contains("pre-prod") || inputFromBot.contains("pre prod"))
	   	            {
	   	            	String productOutput=productParser(inputFromBot,"PRE-PROD");
		    			return new QaBotModel(productOutput);

	   	            }
	   	            else
	   	            {
	   	                System.out.println("Mention which uat, the product url details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	   	             return new QaBotModel("Mention which uat, the product url details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	   	            } 
	   		 }
	   		 else if(inputFromBot.contains("user"))
	   		 {
	   			 if(inputFromBot.contains("qa 2") || inputFromBot.contains("qa2"))
	   	            {
	   	                String userOutput=userParser(inputFromBot,"QA-2");
		    			return new QaBotModel(userOutput);

	   	            }
	   	            else if(inputFromBot.contains("qa 1") || inputFromBot.contains("qa1"))
	   	            {
	   	            	String userOutput= userParser(inputFromBot,"QA-1");
		    			return new QaBotModel(userOutput);

	   	            }
	   	            else if(inputFromBot.contains("dev 1") || inputFromBot.contains("dev1"))
	   	            {
	   	            	String userOutput=userParser(inputFromBot,"DEV-1");
		    			return new QaBotModel(userOutput);

	   	            }
	   	            else  if(inputFromBot.contains("dev 2") || inputFromBot.contains("dev2"))
	   	            {
	   	            	String userOutput= userParser(inputFromBot,"DEV-2");
		    			return new QaBotModel(userOutput);

	   	            }
	   	            else  if(inputFromBot.contains("pre-prod") || inputFromBot.contains("pre prod"))
	   	            {
	   	            	String userOutput=userParser(inputFromBot,"PRE-PROD");
		    			return new QaBotModel(userOutput);

	   	            }
	   	            else
	   	            {
	   	                System.out.println("Mention which uat, the user details details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	   	             return new QaBotModel("Mention which uat, the user details details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	   	            } 
	   		 }
	   		 else if(inputFromBot.contains("promotioncode") || inputFromBot.contains("promotion code") || inputFromBot.contains("promo code"))
	   		 {
	   			 if(inputFromBot.contains("qa 2") || inputFromBot.contains("qa2"))
	   	            {
	   	                String promoOutput=promoParser(inputFromBot,"QA-2");
		    			return new QaBotModel(promoOutput);

	   	            }
	   	            else if(inputFromBot.contains("qa 1") || inputFromBot.contains("qa1"))
	   	            {
	   	            	String promoOutput= promoParser(inputFromBot,"QA-1");
		    			return new QaBotModel(promoOutput);

	   	            }
	   	            else if(inputFromBot.contains("dev 1") || inputFromBot.contains("dev1"))
	   	            {
	   	            	String promoOutput=promoParser(inputFromBot,"DEV-1");
		    			return new QaBotModel(promoOutput);

	   	            }
	   	            else  if(inputFromBot.contains("dev 2") || inputFromBot.contains("dev2"))
	   	            {
	   	            	String promoOutput= promoParser(inputFromBot,"DEV-2");
		    			return new QaBotModel(promoOutput);

	   	            }
	   	            else  if(inputFromBot.contains("pre-prod") || inputFromBot.contains("pre prod"))
	   	            {
	   	            	String promoOutput=promoParser(inputFromBot,"PRE-PROD");
		    			return new QaBotModel(promoOutput);

	   	            }
	   	            else
	   	            {
	   	                System.out.println("Mention which uat, the promotion code details details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	   	             return new QaBotModel("Mention which uat, the promotion code details details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	   	            } 
	   		 } 
	            
		//end of code
	
//		GetPnv pnv= new GetPnv();
//		String line=pnv.getPnv(inputValue);
			//return new QaBotModel(line);
       
        return new  QaBotModel(" Mention the input correctly. ex: pnv, order etc");
	}

	@RequestMapping(value="/getInput", method=RequestMethod.GET,produces={MediaType.APPLICATION_JSON_VALUE})
	public ModelAndView getInput(@RequestParam(value="inputValue", defaultValue="Hello") String inputValue)
	{
		System.out.println("from input api: "+inputValue);
		
		ModelAndView model= new ModelAndView("index");

		
		System.out.println("from input api: "+inputValue);
		 inputFromBot=inputValue;
		//Start calling the routers
		if (inputFromBot.contains("db") || inputFromBot.contains("database"))
       {
            if(inputFromBot.contains("qa 2") || inputFromBot.contains("qa2"))
            {
                String dbResults=dbParser("qa2");
                model.addObject("input",dbResults);
            }
            else  if(inputFromBot.contains("qa 1") || inputFromBot.contains("qa1"))
            {
            	String dbResults=dbParser("qa1");
                model.addObject("input",dbResults);
            }
            else  if(inputFromBot.contains("dev1") || inputFromBot.contains("dev 1"))
            {
            	String dbResults=dbParser("dev1");
                model.addObject("input",dbResults);
            }
            else   if(inputFromBot.contains("dev 2") || inputFromBot.contains("dev2"))
            {
            	String dbResults=dbParser("dev2");
                model.addObject("input",dbResults);

            }
            else   if(inputFromBot.contains("preprod") || inputFromBot.contains("pre-prod"))
            {
            	String dbResults=dbParser("preprod");
                model.addObject("input",dbResults);

            }
            else
            {
                System.out.println("Mention which uat, the order details are required (qa1/qa2/dev1/dev2) and try again  ");
                model.addObject("input","Mention which uat, the order details are required (qa1/qa2/dev1/dev2) and try again  ");
            }
       }
		 else if (inputFromBot.contains("pnv") && !inputFromBot.contains("user"))
	        {

	            System.out.println("\nMake sure redis is installed in local machine (To install type brew install redis)\n");
	            if(inputFromBot.contains("qa2") || inputFromBot.contains("qa 2"))
	            {
	            	String pnvputput=pnvParser("xOtpQa2");
	            	 model.addObject("input",pnvputput);
	            }
	            else if(inputFromBot.contains("qa 1") || inputFromBot.contains("qa1"))
	            {
	            	String pnvputput=pnvParser("xOtpQa1");
	            	 model.addObject("input",pnvputput);
	            }
	            else if(inputFromBot.contains("Dev1") || inputFromBot.contains("Dev 1"))
	            {
	            	String pnvputput= pnvParser("xOtpDev1");
	            	 model.addObject("input",pnvputput);
	            }
	            else if(inputFromBot.contains("dev 2") || inputFromBot.contains("dev2"))
	            {
	                String pnvputput=pnvParser("xOtpDev2");
	                model.addObject("input",pnvputput);
	            }
	            else if(inputFromBot.contains("preprod") || inputFromBot.contains("pre-prod"))
	            {
	                String pnvputput=pnvParser("xOtpPreProd");
	                model.addObject("input",pnvputput);
	            }
	            else
	            {
	                System.out.println("Mention which uat, the pnv details are required (qa1/qa2/dev1/dev2) : ");
	                model.addObject("input","Mention which uat, the pnv details are required (qa1/qa2/dev1/dev2) : ");
	            }
	        }
		 else if(inputFromBot.contains("api"))
		 {
			 if(inputFromBot.contains("qa 2") || inputFromBot.contains("qa2"))
	            {
	                String apiOutput=apiParser(inputFromBot,"QA-2");
	                model.addObject("input",apiOutput);

	            }
	            else if(inputFromBot.contains("qa 1") || inputFromBot.contains("qa1"))
	            {
	            	String apiOutput= apiParser(inputFromBot,"QA-1");
	                model.addObject("input",apiOutput);

	            }
	            else if(inputFromBot.contains("dev 1") || inputFromBot.contains("dev1"))
	            {
	            	String apiOutput=apiParser(inputFromBot,"DEV-1");
	                model.addObject("input",apiOutput);

	            }
	            else  if(inputFromBot.contains("dev 2") || inputFromBot.contains("dev2"))
	            {
	            	String apiOutput= apiParser(inputFromBot,"DEV-2");
	                model.addObject("input",apiOutput);

	            }
	            else  if(inputFromBot.contains("pre-prod") || inputFromBot.contains("pre prod"))
	            {
	            	String apiOutput=apiParser(inputFromBot,"PRE-PROD");
	                model.addObject("input",apiOutput);

	            }
	            else
	            {
	                System.out.println("Mention which uat, the api url details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	                model.addObject("input","Mention which uat, the api url details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	            } 
		 }
		 else if(inputFromBot.contains("center"))
		 {
			 if(inputFromBot.contains("qa 2") || inputFromBot.contains("qa2"))
	            {
	                String centerOutput=centerParser(inputFromBot,"QA-2");
	                model.addObject("input",centerOutput);

	            }
	            else if(inputFromBot.contains("qa 1") || inputFromBot.contains("qa1"))
	            {
	            	String centerOutput= centerParser(inputFromBot,"QA-1");
	                model.addObject("input",centerOutput);

	            }
	            else if(inputFromBot.contains("dev 1") || inputFromBot.contains("dev1"))
	            {
	            	String centerOutput=centerParser(inputFromBot,"DEV-1");
	                model.addObject("input",centerOutput);

	            }
	            else  if(inputFromBot.contains("dev 2") || inputFromBot.contains("dev2"))
	            {
	            	String centerOutput= centerParser(inputFromBot,"DEV-2");
	                model.addObject("input",centerOutput);

	            }
	            else  if(inputFromBot.contains("pre-prod") || inputFromBot.contains("pre prod"))
	            {
	            	String centerOutput=centerParser(inputFromBot,"PRE-PROD");
	                model.addObject("input",centerOutput);

	            }
	            else
	            {
	                System.out.println("Mention which uat, the center url details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	                model.addObject("input","Mention which uat, the center url details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	            } 
		 }
		 else if(inputFromBot.contains("product"))
		 {
			 if(inputFromBot.contains("qa 2") || inputFromBot.contains("qa2"))
	            {
	                String productOutput=productParser(inputFromBot,"QA-2");
	                model.addObject("input",productOutput);

	            }
	            else if(inputFromBot.contains("qa 1") || inputFromBot.contains("qa1"))
	            {
	            	String productOutput= productParser(inputFromBot,"QA-1");
	                model.addObject("input",productOutput);

	            }
	            else if(inputFromBot.contains("dev 1") || inputFromBot.contains("dev1"))
	            {
	            	String productOutput=productParser(inputFromBot,"DEV-1");
	                model.addObject("input",productOutput);

	            }
	            else  if(inputFromBot.contains("dev 2") || inputFromBot.contains("dev2"))
	            {
	            	String productOutput= productParser(inputFromBot,"DEV-2");
	                model.addObject("input",productOutput);

	            }
	            else  if(inputFromBot.contains("pre-prod") || inputFromBot.contains("pre prod"))
	            {
	            	String productOutput=productParser(inputFromBot,"PRE-PROD");
	                model.addObject("input",productOutput);

	            }
	            else
	            {
	                System.out.println("Mention which uat, the product url details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	                model.addObject("input","Mention which uat, the product url details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	            } 
		 }
		 else if(inputFromBot.contains("user"))
		 {
			 if(inputFromBot.contains("qa 2") || inputFromBot.contains("qa2"))
	            {
	                String userOutput=userParser(inputFromBot,"QA-2");
	                model.addObject("input",userOutput);

	            }
	            else if(inputFromBot.contains("qa 1") || inputFromBot.contains("qa1"))
	            {
	            	String userOutput= userParser(inputFromBot,"QA-1");
	                model.addObject("input",userOutput);

	            }
	            else if(inputFromBot.contains("dev 1") || inputFromBot.contains("dev1"))
	            {
	            	String userOutput=userParser(inputFromBot,"DEV-1");
	                model.addObject("input",userOutput);

	            }
	            else  if(inputFromBot.contains("dev 2") || inputFromBot.contains("dev2"))
	            {
	            	String userOutput= userParser(inputFromBot,"DEV-2");
	                model.addObject("input",userOutput);

	            }
	            else  if(inputFromBot.contains("pre-prod") || inputFromBot.contains("pre prod"))
	            {
	            	String userOutput=userParser(inputFromBot,"PRE-PROD");
	                model.addObject("input",userOutput);

	            }
	            else
	            {
	                System.out.println("Mention which uat, the user details details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	                model.addObject("input","Mention which uat, the user details details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	            } 
		 }
		 else if(inputFromBot.contains("promotioncode") || inputFromBot.contains("promotion code") || inputFromBot.contains("promo code"))
		 {
			 if(inputFromBot.contains("qa 2") || inputFromBot.contains("qa2"))
	            {
	                String promoOutput=promoParser(inputFromBot,"QA-2");
	                model.addObject("input",promoOutput);

	            }
	            else if(inputFromBot.contains("qa 1") || inputFromBot.contains("qa1"))
	            {
	            	String promoOutput= promoParser(inputFromBot,"QA-1");
	                model.addObject("input",promoOutput);

	            }
	            else if(inputFromBot.contains("dev 1") || inputFromBot.contains("dev1"))
	            {
	            	String promoOutput=promoParser(inputFromBot,"DEV-1");
	                model.addObject("input",promoOutput);

	            }
	            else  if(inputFromBot.contains("dev 2") || inputFromBot.contains("dev2"))
	            {
	            	String promoOutput= promoParser(inputFromBot,"DEV-2");
	                model.addObject("input",promoOutput);

	            }
	            else  if(inputFromBot.contains("pre-prod") || inputFromBot.contains("pre prod"))
	            {
	            	String promoOutput=promoParser(inputFromBot,"PRE-PROD");
	                model.addObject("input",promoOutput);

	            }
	            else
	            {
	                System.out.println("Mention which uat, the promotion code details details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	                model.addObject("input","Mention which uat, the promotion code details details are required (qa1/qa2/dev1/dev2/pre-prod) : ");
	            } 
		 }
		 else
		 {
             model.addObject("Mention the input correctly, else check the configuration details.");
		 }
		
		//return new ModelAndView("index");

//			model.addObject("input",line);
			return model;
			
            }          
            
    public String dbParser(String uatSource)
    {
        System.out.println("Enter the order id : ");
        
        String[] inputFromParser=inputFromBot.split(" ");
    	int correctIndex=0;
    	
    	for(int i=0;i<inputFromParser.length;i++)
    	{
    		if(!inputFromParser[i].equalsIgnoreCase("db") && !inputFromParser[i].equalsIgnoreCase("database") && !inputFromParser[i].equalsIgnoreCase(uatSource.replaceAll("-", "")))
    		{
    			correctIndex=i;
    		}
    	}
        
        String inputOrderId =  inputFromParser[correctIndex];
       // String inputOrderId =  inputFromBot.replaceAll("[A-Za-z-\\s]", "");
        
        
        System.out.println("DB Order id extraceted:"+inputOrderId);
		GetDatabaseDetails db=new GetDatabaseDetails();
		String dbResults=db.getOrderInformation(uatSource,inputOrderId);
		return dbResults;



    }
    
    public String pnvParser(String source)
    {
       String[] inputEmailArray = inputFromBot.split(" ");
		String inputEmail=inputEmailArray[2];
         String pnvHost=botProp.getProperty(source);
		GetPnv gl1=new GetPnv();
		String pnvOutput=gl1.getPnv(pnvHost,inputEmail);
		return pnvOutput;

    }
    public String apiParser(String inputFromRouter,String source)
    {
    	String[] inputFromParser=inputFromRouter.split(" ");
    	int correctIndex=0;
    	
    	for(int i=0;i<inputFromParser.length;i++)
    	{
    		if(!inputFromParser[i].equalsIgnoreCase("api") && !inputFromParser[i].equalsIgnoreCase(source.replaceAll("-", "")))
    		{
    			correctIndex=i;
    		}
    	}
    	
    	GetBotInformation apiget=new GetBotInformation();
    	String outputUrl=null;
    	 outputUrl=apiget.getApiInformation(source, "API", inputFromParser[correctIndex]);
    	if(outputUrl == null){
    		outputUrl="No Results found for the given API details.<br /> Please check if configuration details are added.";
    	}
    	return outputUrl;
    }
    public String centerParser(String inputFromRouter,String source)
    {
    	String[] inputFromParser=inputFromRouter.split(" ");
    	int correctIndex=0;
    	
    	for(int i=0;i<inputFromParser.length;i++)
    	{
    		if(!inputFromParser[i].equalsIgnoreCase("center") && !inputFromParser[i].equalsIgnoreCase(source.replaceAll("-", "")))
    		{
    			correctIndex=i;
    		}
    	}
    	
    	GetBotInformation centerGet=new GetBotInformation();
    	String outputUrl=null;
    	 outputUrl=centerGet.getCenterInformation(source, "CENTER", inputFromParser[correctIndex]);
    	if(outputUrl == null){
    		outputUrl="No Results found for the given Center details.<br /> Please check if configuration details are added.";
    	}
    	return outputUrl;
    }
    public String productParser(String inputFromRouter,String source)
    {
    	String[] inputFromParser=inputFromRouter.split(" ");
    	int correctIndex=0;
    	
    	for(int i=0;i<inputFromParser.length;i++)
    	{
    		if(!inputFromParser[i].equalsIgnoreCase("product") && !inputFromParser[i].equalsIgnoreCase(source.replaceAll("-", "")))
    		{
    			correctIndex=i;
    		}
    	}
    	
    	GetBotInformation productGet=new GetBotInformation();
    	String outputUrl=null;
    	 outputUrl=productGet.getProductInformation(source, "PRODUCT", inputFromParser[correctIndex]);
    	if(outputUrl == null){
    		outputUrl="No Results found for the given Product details.<br /> Please check if configuration details are added.";
    	}
    	return outputUrl;
    }
    public String userParser(String inputFromRouter,String source)
    {
    	String[] inputFromParser=inputFromRouter.split(" ");
    	int correctIndex=0;
    	
    	for(int i=0;i<inputFromParser.length;i++)
    	{
    		if(!inputFromParser[i].equalsIgnoreCase("user") && !inputFromParser[i].equalsIgnoreCase(source.replaceAll("-", "")))
    		{
    			correctIndex=i;
    		}
    	}
    	
    	GetBotInformation productGet=new GetBotInformation();
    	String outputUrl=null;
    	 outputUrl=productGet.getUserInformation(source, "USER", inputFromParser[correctIndex]);
    	if(outputUrl == null){
    		outputUrl="No Results found for the given User details.<br /> Please check if configuration details are added.";
    	}
    	return outputUrl;
    }
    
    public String promoParser(String inputFromRouter,String source)
    {
    	
    	
    	GetBotInformation productGet=new GetBotInformation();
    	String outputUrl=null;
    	 outputUrl=productGet.getPromoInformation(source, "PROMOTIONCODE", "promotioncode");
    	if(outputUrl == null){
    		outputUrl="No Results found for the given Promotion Code details.<br /> Please check if configuration details are added.";
    	}
    	return outputUrl;
    }
}
