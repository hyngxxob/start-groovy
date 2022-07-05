import groovy.util.XmlParser;
import groovy.xml.XmlUtil;

// Set :: XML
def ExternalRequestRootCollection = "<ExternalRequestRootCollection><ExternalRequestRoot></ExternalRequestRoot></ExternalRequestRootCollection>";
def ExternalRequestRootCollectionXml = new XmlParser().parseText(ExternalRequestRootCollection) // XML DATA 

// Set :: Final Result
def retSM = "";

retSM += "" + OrginReqDateTime + "%col%"
retSM += "" + OrginReqUserID + "%col%"

assert new Node(ExternalRequestRootCollectionXml.ExternalRequestRoot[0],"ReqID" , null , ReqID );
assert new Node(ExternalRequestRootCollectionXml.ExternalRequestRoot[0],"RetrieveStatement_PartsString" , null , retPA); 
assert new Node(ExternalRequestRootCollectionXml.ExternalRequestRoot[0],"RetrieveStatement_WholsString" , null , retGA);
assert new Node(ExternalRequestRootCollectionXml.ExternalRequestRoot[0],"RetrieveStatement_CreditString" , null , retCM);
assert new Node(ExternalRequestRootCollectionXml.ExternalRequestRoot[0],"RetrieveStatement_UnappliedString" , null , retUF);
assert new Node(ExternalRequestRootCollectionXml.ExternalRequestRoot[0],"RetrieveStatement_DownString" , null , retDP);
assert new Node(ExternalRequestRootCollectionXml.ExternalRequestRoot[0],"RetrieveStatement_SummaryString" , null , retSM);

def nodeAsText = XmlUtil.serialize(ExternalRequestRootCollectionXml).toString();
nodeAsText = nodeAsText.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","");
message.setBody(nodeAsText);

// println nodeAsText
return message