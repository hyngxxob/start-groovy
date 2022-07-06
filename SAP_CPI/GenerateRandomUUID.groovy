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

// 랜덤 UUID 생성
def ReqID  = UUID.randomUUID().toString();

// 현재 날짜와 시간 가져오기
def pattern = "yyyyMMdd";
def today = new Date();
Calendar cal = Calendar.getInstance();        
cal.setTime(today);
cal.add(Calendar.HOUR, +9);
def tempDate = cal.getTime().format(pattern); 