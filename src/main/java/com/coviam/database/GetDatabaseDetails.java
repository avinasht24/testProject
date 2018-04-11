package com.coviam.database;


import com.coviam.qabot.controller.QaBotController;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
//import org.bson.Document;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import org.bson.conversions.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by avinash.t on 25/11/17.
 */
public class GetDatabaseDetails {

	public String dbResults="Starting DB Validation\n";

    public String getOrderInformation(String uatSource,String inputFromRouter)
    {
        GetMongoOrderInformation(uatSource,inputFromRouter);
        GetSqlOrderTable(uatSource,inputFromRouter);
        System.out.println( "dbresults value:"+dbResults );

        return dbResults;

    }

    public void GetSqlOrderTable(String uatSource,String inputFromRouter)
    {
        OrderInformation(uatSource,inputFromRouter);
        FinanceInformation(uatSource,inputFromRouter);



    }

    public void OrderInformation(String uatSource,String inputFromRouter)
    {
        String dbHost=null;
        String dbName=null;
        String dbUser=null;
        String dbPwd=null;

        if(uatSource.equalsIgnoreCase("qa1"))
        {
            dbHost= QaBotController.botProp.getProperty("OrderDBUatBHost");
            dbName=QaBotController.botProp.getProperty("OrderDBUatBName");
            dbUser=QaBotController.botProp.getProperty("OrderDBUatBUser");
            dbPwd=QaBotController.botProp.getProperty("OrderDBUatBPwd");
        }
        else if(uatSource.equalsIgnoreCase("qa2"))
        {
            dbHost= QaBotController.botProp.getProperty("OrderDBUatAHost");
            dbName=QaBotController.botProp.getProperty("OrderDBUatAName");
            dbUser=QaBotController.botProp.getProperty("OrderDBUatAUser");
            dbPwd=QaBotController.botProp.getProperty("OrderDBUatAPwd");
        }
        else if(uatSource.equalsIgnoreCase("dev1"))
        {
            dbHost= QaBotController.botProp.getProperty("OrderDBUat1Host");
            dbName=QaBotController.botProp.getProperty("OrderDBUat1Name");
            dbUser=QaBotController.botProp.getProperty("OrderDBUat1User");
            dbPwd=QaBotController.botProp.getProperty("OrderDBUat1Pwd");
        }
        else if(uatSource.equalsIgnoreCase("dev2"))
        {
            dbHost= QaBotController.botProp.getProperty("OrderDBUat5Host");
            dbName=QaBotController.botProp.getProperty("OrderDBUat5Name");
            dbUser=QaBotController.botProp.getProperty("OrderDBUat5User");
            dbPwd=QaBotController.botProp.getProperty("OrderDBUat5Pwd");
        }
        else if(uatSource.equalsIgnoreCase("preprod"))
        {
            dbHost= QaBotController.botProp.getProperty("OrderDBPreProdHost");
            dbName=QaBotController.botProp.getProperty("OrderDBPreProdName");
            dbUser=QaBotController.botProp.getProperty("OrderDBPreProdUser");
            dbPwd=QaBotController.botProp.getProperty("OrderDBPreProdPwd");
        }

        Connection con = null;
        Statement stmt = null;
        Statement stmt2 = null;

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://"+dbHost+":5432/"+dbName+"",
                    ""+dbUser+"", ""+dbPwd+"");
            con.setAutoCommit(false);
            //System.out.println("Connected to database!");

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT order_id, order_status FROM orders where order_id='"+inputFromRouter+"'" );
            while ( rs.next() ) {
                String  orderIdOrderDb = rs.getString("order_id");
                String  orderStatusOrderDb = rs.getString("order_status");
                System.out.println( "************************" );
                dbResults= dbResults.concat("\n<br />************************\n <br />");
                System.out.println( " DETAILS FROM ORDER TABLE" );
                System.out.println( "************************" );
                dbResults=  dbResults.concat(" <br />DETAILS FROM ORDER TABLE\n <br />************************");

                System.out.println( "ORDER ID = " + orderIdOrderDb );
                dbResults=    dbResults.concat(" <br />ORDER ID = " + orderIdOrderDb);
                System.out.println( "ORDER STATUS = " + orderStatusOrderDb );
                dbResults=   dbResults.concat(" <br />ORDER STATUS =  " + orderStatusOrderDb);
                System.out.println("\n***** END of ORDER TABLE ****\n");
                dbResults=  dbResults.concat("\n <br />***** END of ORDER TABLE ****\n <br />");


            }
            System.out.println( "************************" );
            dbResults= dbResults.concat( " <br />************************ <br />");

            System.out.println( " DETAILS FROM ORDER ITEM TABLE  " );
            dbResults= dbResults.concat("\n  <br />DETAILS FROM ORDER ITEM TABLE \n <br /> ");

            System.out.println( "************************" );
            dbResults=dbResults.concat( "\n <br />************************ <br />\n");

            stmt2 = con.createStatement();
            ResultSet rs2 = stmt2.executeQuery( "SELECT order_id, order_item_id,order_item_status FROM order_item where order_id='"+inputFromRouter+"'" );
            while ( rs2.next() ) {
                String  orderIdOrderDb = rs2.getString("order_id");
                String  orderItemIdOrderDb = rs2.getString("order_item_id");
                String  orderItemStatusOrderDb = rs2.getString("order_item_status");


                System.out.println( "ORDER ID = " + orderIdOrderDb );
                dbResults= dbResults.concat( "\n <br /> ORDER ID = " + orderIdOrderDb);

                System.out.println( "ORDER ITEM ID = " + orderItemIdOrderDb );
                dbResults=   dbResults.concat( "\n <br />ORDER ITEM ID = " + orderItemIdOrderDb);

                System.out.println( "ORDER ITEM STATUS = " + orderItemStatusOrderDb );
                dbResults=   dbResults.concat( "\n <br />ORDER ITEM STATUS =  " + orderItemStatusOrderDb);

            }
            System.out.println("\n***** END of ORDER ITEM TABLE ****\n");
            dbResults=  dbResults.concat( "\n <br />***** END of ORDER ITEM TABLE **** <br />\n");


            rs.close();
            stmt.close();
            con.close();





        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

    }

    public void FinanceInformation(String uatSource,String inputFromRouter)
    {
        String dbHost=null;
        String dbName=null;
        String dbUser=null;
        String dbPwd=null;

        if(uatSource.equalsIgnoreCase("qa2"))
        {
            dbHost= QaBotController.botProp.getProperty("FinanceDBUatBHost");
            dbName=QaBotController.botProp.getProperty("FinanceDBUatBName");
            dbUser=QaBotController.botProp.getProperty("FinanceDBUatBUser");
            dbPwd=QaBotController.botProp.getProperty("FinanceDBUatBPwd");
        }
        else if(uatSource.equalsIgnoreCase("qa1"))
        {
            dbHost= QaBotController.botProp.getProperty("FinanceDBUatAHost");
            dbName=QaBotController.botProp.getProperty("FinanceDBUatAName");
            dbUser=QaBotController.botProp.getProperty("FinanceDBUatAUser");
            dbPwd=QaBotController.botProp.getProperty("FinanceDBUatAPwd");
        }
        else if(uatSource.equalsIgnoreCase("dev1"))
        {
            dbHost= QaBotController.botProp.getProperty("FinanceDBUat1Host");
            dbName=QaBotController.botProp.getProperty("FinanceDBUat1Name");
            dbUser=QaBotController.botProp.getProperty("FinanceDBUat1User");
            dbPwd=QaBotController.botProp.getProperty("FinanceDBUat1Pwd");
        }
        else if(uatSource.equalsIgnoreCase("dev2"))
        {
            dbHost= QaBotController.botProp.getProperty("FinanceDBUat5Host");
            dbName=QaBotController.botProp.getProperty("FinanceDBUat5Name");
            dbUser=QaBotController.botProp.getProperty("FinanceDBUat5User");
            dbPwd=QaBotController.botProp.getProperty("FinanceDBUat5Pwd");
        }
        else if(uatSource.equalsIgnoreCase("preprod"))
        {
            dbHost= QaBotController.botProp.getProperty("FinanceDBPreProdHost");
            dbName=QaBotController.botProp.getProperty("FinanceDBPreProdName");
            dbUser=QaBotController.botProp.getProperty("FinanceDBPreProdUser");
            dbPwd=QaBotController.botProp.getProperty("FinanceDBPreProdPwd");
        }

        Connection con = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://"+dbHost+":5432/"+dbName+"",
                    ""+dbUser+"", ""+dbPwd+"");
            con.setAutoCommit(false);
            //System.out.println("Connected to database!");

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT order_id, order_status FROM orders where order_id='"+inputFromRouter+"'" );
            while ( rs.next() ) {
                String  orderIdOrderDb = rs.getString("order_id");
                String  orderStatusOrderDb = rs.getString("order_status");
                System.out.println( "************************" );
                dbResults= dbResults.concat( "\n <br />************************ <br />\n");

                System.out.println( " DETAILS FROM FINANCE DB" );
                dbResults=dbResults.concat( "\n <br />DETAILS FROM FINANCE DB <br />\n");

                System.out.println( "************************" );
                dbResults=  dbResults.concat( "\n <br />************************ <br />\n");


                System.out.println( "ORDER ID = " + orderIdOrderDb );
                dbResults=   dbResults.concat( "\n <br />ORDER ID = " + orderIdOrderDb);

                System.out.println( "ORDER STATUS = " + orderStatusOrderDb );
                dbResults=   dbResults.concat( "\n <br />ORDER STATUS = " + orderStatusOrderDb);

                System.out.println("\n***** END of FINANCE DB ****\n");
                dbResults=   dbResults.concat( "\n <br />***** END of FINANCE DB **** <br />\n");


            }
            rs.close();
            stmt.close();
            con.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

    }


    public void GetMongoOrderInformation(String uatSource,String inputFromRouter) {

        String dbHost=null;
        String dbName=null;
        String dbUser=null;
        String dbPwd=null;

        if(uatSource.equalsIgnoreCase("qa2"))
        {
            dbHost= QaBotController.botProp.getProperty("CartDBUatBHost");
            dbName=QaBotController.botProp.getProperty("CartDBUatBName");
            dbUser=QaBotController.botProp.getProperty("CartDBUatBUser");
            dbPwd=QaBotController.botProp.getProperty("CartDBUatBPwd");
        }
        else  if(uatSource.equalsIgnoreCase("qa1"))
        {
            dbHost= QaBotController.botProp.getProperty("CartDBUatAHost");
            dbName=QaBotController.botProp.getProperty("CartDBUatAName");
            dbUser=QaBotController.botProp.getProperty("CartDBUatAUser");
            dbPwd=QaBotController.botProp.getProperty("CartDBUatAPwd");
        }
        else  if(uatSource.equalsIgnoreCase("dev1"))
        {
            dbHost= QaBotController.botProp.getProperty("CartDBUat1Host");
            dbName=QaBotController.botProp.getProperty("CartDBUat1Name");
            dbUser=QaBotController.botProp.getProperty("CartDBUat1User");
            dbPwd=QaBotController.botProp.getProperty("CartDBUat1Pwd");
        }
        else  if(uatSource.equalsIgnoreCase("dev2"))
        {
            dbHost= QaBotController.botProp.getProperty("CartDBUat5Host");
            dbName=QaBotController.botProp.getProperty("CartDBUat5Name");
            dbUser=QaBotController.botProp.getProperty("CartDBUat5User");
            dbPwd=QaBotController.botProp.getProperty("CartDBUat5Pwd");
        }
        else  if(uatSource.equalsIgnoreCase("preprod"))
        {
            dbHost= QaBotController.botProp.getProperty("CartDBPreProdHost");
            dbName=QaBotController.botProp.getProperty("CartDBPreProdName");
            dbUser=QaBotController.botProp.getProperty("CartDBPreProdUser");
            dbPwd=QaBotController.botProp.getProperty("CartDBPreProdPwd");
        }
        MongoClientURI uri = new MongoClientURI("mongodb://"+dbUser+":"+dbPwd+"@"+dbHost+":27017/"+dbName+"");
        MongoClient mongoClient = new MongoClient(uri);

        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.WARNING);

        MongoClientOptions.Builder optionsBuilder = MongoClientOptions.builder();

        optionsBuilder.connectTimeout(30000);
        MongoDatabase db = mongoClient.getDatabase("cart");
        MongoCollection<Document> collection = db.getCollection("cart_order");
        //DBCursor cursor =collection.find();
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("orderId", inputFromRouter);

        BasicDBObject selectQuery = new BasicDBObject();

        //  selectQuery.put("orderId", "1");
        selectQuery.put("status", "1");
        selectQuery.put("cartItems.itemSku", "1");
        selectQuery.put("cartItems.orderItemId", "1");
        selectQuery.put("cartItems.quantity", "1");
        selectQuery.put("cartItems.adjustments.adjustmentName", "1");
        selectQuery.put("cartItems.logisticsInfo.logisticsProvider.logisticOptionCode", "1");
        selectQuery.put("cartItems.logisticsInfo.logisticsProvider.logisticsProviderCode", "1");

        //System.out.println(collection.find(whereQuery).projection(selectQuery));

        //   List<Document> customerAddress = (List<Document>) collection.find(whereQuery).projection(selectQuery).map(document -> document.get("customerAddress")).first();
        MongoIterable<Document> collections = collection.find(whereQuery).projection(selectQuery);

        for (Document collectionName : collections) {
            System.out.println("************************");
            dbResults= dbResults=  dbResults.concat( "\n <br />************************\n");

            System.out.println(" DETAILS FROM CART DB");
            dbResults= dbResults.concat( "\n <br />DETAILS FROM CART DB\n");

            System.out.println("************************");
            dbResults= dbResults.concat( "\n <br />************************\n");

           // System.out.println(collectionName.toJson());
            ObjectMapper mapper = new ObjectMapper();

            try {
                System.out.println("Output\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collectionName));
                dbResults=dbResults.concat( "\n <br />Output\n <br />" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collectionName));

            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("\n***** END of CART DB ****\n");
            dbResults=dbResults.concat( "\n <br />***** END of CART DB **** <br />\n");


        }

    }


}
