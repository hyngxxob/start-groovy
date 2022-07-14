import com.sap.gateway.ip.core.customdev.util.Message;
import java.io.*; 
import java.util.*;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import groovy.json.JsonSlurper;
import groovy.json.JsonOutput;
import groovy.json.StringEscapeUtils;
import groovy.json.JsonBuilder;
import groovy.xml.XmlUtil;
import groovy.xml.MarkupBuilder;
import groovy.json.JsonBuilder;
import groovy.util.*
import java.util.TimeZone;

def Message processData(Message message) {
	def body = message.getBody(java.lang.String) as String;     
    def slurper = new JsonSlurper()
    def parsedJson = slurper.parseText(body);
    
    def list = parsedJson.ServiceRequestCollection.ServiceRequest;
    // def resultList = [];

    // if(list instanceof ArrayList){  // 불러온 데이터가 List Type이면
    //     resultList = list;          // 리스트에 담아주고
    // }else{
    //     resultList.add(list);       // 아니면 추가 해 준다.
    // }
    
    // message.setProperty("resultList",resultList);   // (변수)Property 명, 넣어줄 데이터
    // message.setProperty("resultListSize",resultList.size());
    // def BuyerPartyID = [];
    def BuyerPartyID = "";
    def BuyerPartyID_SIZE;
    for( obj in list ) {
        if(obj.BuyerPartyID == "" || obj.BuyerPartyID == null) {
            continue;
        } else {
            if(list.size() == 1 ) {
                BuyerPartyID += obj.BuyerPartyID;
            } else {
                BuyerPartyID += obj.BuyerPartyID + "%col%" + obj.ID + "%div%";
            }
            println obj.BuyerPartyID;
        }
        // BuyerPartyID.add(obj.BuyerPartyID);
        //BuyerPartyID += obj.BuyerPartyID;
    }
    BuyerPartyID_SIZE = list.size()
    message.setProperty("BPID", BuyerPartyID);
    message.setProperty("BPID_SIZE", BuyerPartyID_SIZE);
	//message.setBody(list);
	return message;
}

def Message Loop(Message message) {
    def properties = message.getProperty("BPID").minus("[").minus("]");
    def BuyerPartyID = []
    def BuyerPartyID_SIZE;
    def tempBuyerPartyID = "";
    if(properties.indexOf("%div%") != -1) {
        def properties_split = properties.split("%div%");
        BuyerPartyID.addAll(properties_split)
        println BuyerPartyID
        def obj = BuyerPartyID[0];
        def obj_split = obj.split("%col%");
        def cus_id = obj_split[0]
        def id = obj_split[1]
        message.setProperty("CUSID", cus_id);
        message.setProperty("TEMP_ID", id)

        BuyerPartyID.remove(0)
        BuyerPartyID_SIZE = BuyerPartyID.size()
        for( lst in BuyerPartyID) {
            if( BuyerPartyID.size() == 1) {
                tempBuyerPartyID += lst;
            } else {
                tempBuyerPartyID += lst + "%div%";
            }
        }
        message.setProperty("BPID_SIZE", BuyerPartyID_SIZE)
        message.setProperty("BPID", tempBuyerPartyID)

    } 
    // else {
    //     BuyerPartyID.addAll(properties)
    //     def obj = BuyerPartyID[0];
        // println obj
        // message.setProperty("CUSID", obj);
    // }
    // for( obj in test) {
    //     message.setProperty("CUSID",obj);
    // }
    return message;
}

def Message Detect(Message message) {
	def body = message.getBody(java.lang.String) as String;     
    def slurper = new JsonSlurper()
    def parsedJson = slurper.parseText(body);
    
    def collection_size = parsedJson.IndividualCustomerCollection.size()
    def OID_BAGS = [];
    def TICKETID_BAGS = [];
    // println collection_size

    // def test = message.getProperty("OID_BAGS")
    // println test


    if(collection_size > 0) {
        def IndividualCustomer = parsedJson.IndividualCustomerCollection.IndividualCustomer;
        def OID = parsedJson.IndividualCustomerCollection.IndividualCustomer.ObjectID;
        def OID_BAGS_prop = message.getProperty("OID_BAGS");
        def TICKET_LIST_prop = message.getProperty("TICKET_LIST")

        if(OID_BAGS_prop != null) {
            def properties = message.getProperty("OID_BAGS").minus("[").minus("]");
            OID_BAGS.addAll(properties)
        } else {
            message.setProperty("OID_BAGS", OID_BAGS)
            message.setProperty("OID_cnt", OID_BAGS.size())
        }
        
        if(TICKET_LIST_prop != null) {
            def ticket_list = message.getProperty("TICKET_LIST").minus("[").minus("]")
            TICKETID_BAGS.addAll(ticket_list)
        } else {
            message.setProperty("TICKET_LIST","")
        }

        if(!initial_chk(OID)) {
            def ticket_id = message.getProperty("TEMP_ID")
            OID_BAGS.add(OID)
            TICKETID_BAGS.add(ticket_id)
            message.setProperty("OID_BAGS", OID_BAGS)
            message.setProperty("OID_cnt", OID_BAGS.size())
            message.setProperty("TICKET_LIST",TICKETID_BAGS)
            message.setProperty("TEMP_ID","");
        }
    } else {
        message.setProperty("TEMP_ID","");
    }
    // def chk = test instanceof ArrayList;


    // def OID = parsedJson.IndividualCustomerCollection.IndividualCustomer.ObjectID;
    // println parsedJson.IndividualCustomerCollection
    // println OID
    return message;
}

def initial_chk(String str) {
    if(str == null) {
        return true
    } else return false
}