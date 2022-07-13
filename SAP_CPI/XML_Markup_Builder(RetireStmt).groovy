import com.sap.gateway.ip.core.customdev.util.Message;
import java.io.*; 
import java.util.*;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.text.DecimalFormat
import groovy.json.JsonSlurper;
import groovy.json.JsonOutput;
import groovy.json.StringEscapeUtils;
import groovy.json.JsonBuilder;
import groovy.util.XmlParser;
import groovy.xml.XmlUtil;
import groovy.xml.MarkupBuilder;
import org.apache.commons.lang3.*;

def Message processData(Message message) {
    def mapHeader = message.getHeaders();
    def map = message.getProperties();
    def body = message.getBody(java.lang.String) as String; // PRD
    // JsonSlurper slurper = new JsonSlurper();
    // def parsedJson = slurper.parseText(body);

    
   
    def props = body.split(',')
    
    def CATEGORY = props[0]
    def MATNR = props[1]
    
    
    def ReqID  = UUID.randomUUID().toString();
    
    message.setProperty("MATNR", MATNR);
    message.setProperty("CATEGORY", CATEGORY);
    message.setProperty("ReqID", ReqID); // random
    
    
    // def reqDataBody =""
    // reqDataBody =              '{'
    // reqDataBody = reqDataBody+  '"ReqID" : "'+ReqUID+'",'
    // reqDataBody = reqDataBody+  '"ReqUserID" : "'+ReqID+'", '
    // reqDataBody = reqDataBody+  '"ReqDateTime" : "'+Date+'",'
    // reqDataBody = reqDataBody+  '"ReqBody" : "" }'

    // println reqDataBody

    // message.setBody(reqDataBody)
    
    return message;
}



def Message setData(Message message) {
    def jsonSlurper = new JsonSlurper();
    def body = message.getBody(java.lang.String) as String;
    def result = jsonSlurper.parseText(body);
 
    // Set :: Properties
    def MATNR = message.getProperty("MATNR")
    def CATEGORY = message.getProperty("CATEGORY")
    def ReqID = message.getProperty("ReqID")

    // Set :: XML
    def ExternalRequestRootCollection = "<ExternalRequestRootCollection><ExternalRequestRoot></ExternalRequestRoot></ExternalRequestRootCollection>";
    def ExternalRequestRootCollectionXml = new XmlParser().parseText(ExternalRequestRootCollection) // XML DATA 

    // Set :: Final Result
    def setPPP = ""
    def setPPO = ""
    def setPPA = ""
    def DescBox = ""
    def blank = " "
    
    def T_LIST = result.'rfc:ZSD_SEND_PRICE_PAGE_TO_C4C.Response'.T_LIST
    if(T_LIST.size() > 0) { T_LIST = result.'rfc:ZSD_SEND_PRICE_PAGE_TO_C4C.Response'.T_LIST.item }

    def T_TEXT = result.'rfc:ZSD_SEND_PRICE_PAGE_TO_C4C.Response'.T_TEXT
    if(T_TEXT.size() > 0) { T_TEXT = result.'rfc:ZSD_SEND_PRICE_PAGE_TO_C4C.Response'.T_TEXT.item }

    def T_VALUE = result.'rfc:ZSD_SEND_PRICE_PAGE_TO_C4C.Response'.T_VALUE
    if(T_VALUE.size() > 0) { T_VALUE = result.'rfc:ZSD_SEND_PRICE_PAGE_TO_C4C.Response'.T_VALUE.item }

    def T_ATTACH = result.'rfc:ZSD_SEND_PRICE_PAGE_TO_C4C.Response'.T_ATTACH
    if(T_ATTACH.size() > 0) { T_ATTACH = result.'rfc:ZSD_SEND_PRICE_PAGE_TO_C4C.Response'.T_ATTACH.item }
    

    // def T_LIST = result.'rfc:ZSD_SEND_PRICE_PAGE_TO_C4C.Response'.T_LIST.item
    // def T_TEXT = result.'rfc:ZSD_SEND_PRICE_PAGE_TO_C4C.Response'.T_TEXT.item
    // def T_VALUE = result.'rfc:ZSD_SEND_PRICE_PAGE_TO_C4C.Response'.T_VALUE.item
    // def T_ATTACH = result.'rfc:ZSD_SEND_PRICE_PAGE_TO_C4C.Response'.T_ATTACH.item
    

// PricePage_PackageArragementString
    for(int i=0; i < T_TEXT.size(); i++) {
        DescBox += T_TEXT[i].TEXTX + "<br/>"
    }

    def T_LIST_RST = T_LIST
    def T_LIST_Chk = T_LIST_RST instanceof ArrayList

    if(!T_LIST_Chk && T_LIST_RST !="" ) {
            setPPP += T_LIST_RST.MALNR + "%col%" + DescBox + "%col%" + numberChk(T_LIST_RST.BRGEW) + blank + T_LIST_RST.GEWEI + "%col%"
            setPPP += numberChk(T_LIST_RST.KBETR) + blank + T_LIST_RST.KONWA + "%col%" + CATEGORY + "%col%"
            DescBox = ""
    } else {
        for(int i=0; i < T_LIST_RST.size(); i++) {
            if(i > 0) {
                setPPP += "%row%"
            }
            setPPP += T_LIST_RST[i].MALNR + "%col%" + DescBox + "%col%" + numberChk(T_LIST_RST[i].BRGEW) + blank + T_LIST_RST[i].GEWEI + "%col%"
            setPPP += numberChk(T_LIST_RST[i].KBETR) + blank + T_LIST_RST[i].KONWA + "%col%" + CATEGORY+ "%col%"
        }
        DescBox = ""
    }


// PricePage_OptionEquipString
    def T_VALUE_RST = T_VALUE
    def T_VALUE_Chk = T_VALUE_RST instanceof ArrayList

    if(!T_VALUE_Chk && T_VALUE_RST !="")  {
        setPPO += T_VALUE_RST.ATBEZ + "%col%" + T_VALUE_RST.ATWRT + "%col%" + T_VALUE_RST.ATWTB + "%col%" + numberChk(T_VALUE_RST.KBETR) + blank + T_VALUE_RST.KONWA + "%col%"
    } else {
        for(int i=0; i < T_VALUE_RST.size(); i++) {
            if(T_VALUE_RST[i].KBETR != "0.00") {
                if(setPPO!="") {
                    setPPO += "%row%"
                }
            //if(T_VALUE_RST[i].KBETR == "0.00") {
            //    i++
            //}
            
                setPPO += T_VALUE_RST[i].ATBEZ + "%col%" + T_VALUE_RST[i].ATWRT   + "%col%" + T_VALUE_RST[i].ATWTB  + "%col%"
                setPPO += numberChk(T_VALUE_RST[i].KBETR) + blank + T_VALUE_RST[i].KONWA + "%col%"
            }
        }
    }

// PricePage_AttachmentsString
    def T_ATTACH_RST = T_ATTACH
    def T_ATTACH_Chk = T_ATTACH_RST instanceof ArrayList
    
    if(!T_ATTACH_Chk && T_ATTACH_RST !="") {
        setPPA += T_ATTACH_RST.CHILD + "%col%" + T_ATTACH_RST.CHILD_TX + "%col%" + numberChk(T_ATTACH_RST.BRGEW) + "%col%"
        setPPA += numberChk(T_ATTACH_RST.KBETR) + blank + T_ATTACH_RST.KONWA + "%col%"
    } else {
        for(int i=0; i < T_ATTACH_RST.size(); i++) {
            if(i > 0) {
                setPPA += "%row%"
            }
            setPPA += T_ATTACH_RST[i].CHILD + "%col%" + T_ATTACH_RST[i].CHILD_TX + "%col%" + numberChk(T_ATTACH_RST[i].BRGEW) + "%col%"
            setPPA += numberChk(T_ATTACH_RST[i].KBETR) + blank + T_ATTACH_RST[i].KONWA + "%col%" 
        }
    }
    


    assert new Node(ExternalRequestRootCollectionXml.ExternalRequestRoot[0],"ReqID" , null , ReqID );
    assert new Node(ExternalRequestRootCollectionXml.ExternalRequestRoot[0],"PricePage_PackageArragementString" , null , setPPP); 
    assert new Node(ExternalRequestRootCollectionXml.ExternalRequestRoot[0],"PricePage_OptionEquipString" , null , setPPO);
    assert new Node(ExternalRequestRootCollectionXml.ExternalRequestRoot[0],"PricePage_AttachmentsString" , null , setPPA);

    def nodeAsText = XmlUtil.serialize(ExternalRequestRootCollectionXml).toString();
    nodeAsText = nodeAsText.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","");
    message.setBody(nodeAsText);

    
    // println nodeAsText

    return message
}

def numberChk(String str) {
    DecimalFormat fmt = new DecimalFormat("###,###,###,###,###,###,###,###,###,##0.00")
    def inputNum = fmt.parse(str).doubleValue()
    def result = fmt.format(inputNum)
    return result;
}