import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import java.text.SimpleDateFormat;
import java.util.Calendar; 
import java.util.Date;
import java.util.Arrays;

def Message processData(Message message) {

    def mapHeader = message.getHeaders();
    def map = message.getProperties();
    def body = message.getBody(java.lang.String) as String;


    // JsonSlurper 생성
    JsonSlurper slurper = new JsonSlurper();
    def parsedJson = slurper.parseText(body);

    def emp = parsedJson.EmployeeCollection.Employee

    def dataList = []

    for(int i = 0; i < emp.size(); i++){
        // Map(Key:Value)
        def temp = [:]
        temp.put("objID",emp[i].ObjectID)
        temp.put("LastName",emp[i].LastName)
        temp.put("EmployeeID",emp[i].EmployeeID)
        // dataList 배열에 추가
        dataList.add(temp)
    }
    // dataList를 JSON 형태로 setProperty
    // JsonOutput.toJson() 안 해주면 String 형태로 넘어감
    message.setProperty("empList", JsonOutput.toJson(dataList))
    message.setProperty("cnt" , dataList.size())
    return message
}
