/* ex) numberChk(15000)*/
numberChk(T_ATTACH_RST.BRGEW)

def numberChk(String str) {
    DecimalFormat fmt = new DecimalFormat("###,###,###,###,###,###,###,###,###,##0.00")
    def inputNum = fmt.parse(str).doubleValue()
    def result = fmt.format(inputNum)
    return result;
}