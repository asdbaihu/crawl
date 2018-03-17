package com.langchao.bigdata.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.langchao.bigdata.hbase.HbaseUtil;
import com.langchao.bigdata.util.EsUtil;
import com.langchao.bigdata.util.MD5;
import com.sun.xml.internal.ws.api.model.ExceptionType;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * GXB 企业基本信息数据爬取
 * @author yuenbin
 *
 */
@TargetUrl("https://www.tianyancha.com/company/\\w+")
public class BsEntBasic  implements AfterExtractor {
	private String ID				;//主键	VARCHAR(36)	
	@Value(value="XXXX")
	private String ENT_ID			;//市场主体编号	VARCHAR(36)	
	private String USC_CODE		;//统一社会信用代码	VARCHAR(18)	统一社会信用代码
	@ExtractBy(value="//*[@id='_container_baseInfo']/div/div[3]/table/tbody/tr[1]/td[4]/text()")
	private String REG_CODE		;//组织机构代码          
	@ExtractBy(value="//*[@id='company_web_top']/div[2]/div[2]/div[1]/span/html()")
	private String ENT_NAME		;//企业名称         
	@ExtractBy(value="//*[@id='_container_baseInfo']/div/div[3]/table/tbody/tr[2]/td[4]/text()")
	private String MMB_TYPE	;//	市场主体类型	CHAR(6)	参考枚举
	@Value(value="经营者")
	private String ENT_TYPE	;//	企业性质	CHAR(6)	
	@ExtractBy(value="//*[@id='web-content']/div/div/div/div[1]/div[3]/div[3]/div[2]/div[2]/div[1]/div[3]/span/html()")
	private String EST_DATE ;//		成立日期	DATETIME	
	@ExtractBy(value="//*[@id='_container_baseInfo']/div/div[2]/table/tbody/tr/td[1]/div/div[1]/div[2]/div/a/html()")
	private String LEG_REP		;//		;//法定代表人/负责人/执行事务合伙人/首席代表
	@ExtractBy(value="//*[@id='web-content']/div/div/div/div[1]/div[3]/div[1]/div[2]/div[2]/div[1]/div[2]/span/html()")
	private String REG_CAP;//注册资本
	@ExtractBy(value="//*[@id='company_web_top']/div[2]/div[2]/div[2]/div[3]/div[2]/span[2]/html()")
	private String DOM					;//		住所/营业场所
	@ExtractBy(value="//*[@id='_container_baseInfo']/div/div[3]/table/tbody/tr[7]/td[2]/span/span/span[2]/html()")
	private String OP_SCOPE		;//经营范围	VARCHAR(2000)	
	
	private Date OP_FROM		;//	营业/经营/合伙/驻在期限自
	@Value(value="")
	private Date OP_TO			;//营业经营/合伙/驻在期限至	
	@ExtractBy(value="//*[@id='_container_baseInfo']/div/div[3]/table/tbody/tr[5]/td[2]/text()")
	private String REG_ORG			;//登记机关	VARCHAR(9)	工
	private Date APPR_DATE         ;//   核准日期      
	@Value(value="北京")
	private String PROC_LOC		;//生产经营地	VARCHAR(200)	
	@Value(value="110000")
	private String YIE_DIST		;//生产经营地所在行政区划	VARCHA
	@Value(value="xxxx")
	private String REG_CAP_CUR	;//	注册资本(金)币种代码	CH
	@Value(value="01")
	private String OP_STATE	;//	经营状态	CHAR              
	@Value(value="xxxx")
	private String FOR_ENTER_NAME;//	外国（地区）企业名称	VA
	@Value(value="xxxxx")
	private String FOR_DOM		;//	外国（地区）企业住所	VA
	@Value(value="")
	private String INDU_CODE;//		行业代码	VARCHAR(4)	国
	@Value(value="GXBXXX")
	private String GXBINDU_CODE;//	工信部行业代码	VARCHAR(4)	工
	@ExtractBy(value="100000")
	private String POST_CODE;//		邮编	VARCHAR           
	@ExtractBy(value="//*[@id='company_web_top']/div[2]/div[2]/div[2]/div[2]/div[1]/span[2]/html()")
	private String PHONE	;//		企业联系电话	VARCHAR(50
	@Value(value="A")
	private String CREDIT_RATING;//	信用等级	CHAR(1)	A、B、
	@Value(value="XX")
	private String CM_CODE	;//		核算方式	VARCHAR(1)	限
	@Value(value="1")
	private String IS_VALID	;//	有效性标识	CHAR(1)	0表示无效，
	
	private Date AN_CHE_DATE;
	
	@ExtractBy(value="//*[@id='web-content']/div/div/div[2]/div/div[2]/div[1]/div[2]/div[2]/div[10]/div[2]/div/a[1]/span[2]/text()")
	private String ANCHE_YEAR;

	private Date UPDATE_DATE;//		更新时间	DATETIME	数
	
	@ExtractBy(value="//*[@id='company_web_top']/div[2]/div[2]/div[2]/div[2]/div[2]/span[2]/html()")
	private String EMAIL;//邮箱
	
	public String getEMAIL() {
		return EMAIL;
	}
	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getENT_ID() {
		return ENT_ID;
	}
	public void setENT_ID(String eNT_ID) {
		ENT_ID = eNT_ID;
	}
	public String getUSC_CODE() {
		return USC_CODE;
	}
	public void setUSC_CODE(String uSC_CODE) {
		USC_CODE = uSC_CODE;
	}
	public String getREG_CODE() {
		return REG_CODE;
	}
	public void setREG_CODE(String rEG_CODE) {
		REG_CODE = rEG_CODE;
	}
	public String getENT_NAME() {
		return ENT_NAME;
	}
	public void setENT_NAME(String eNT_NAME) {
		ENT_NAME = eNT_NAME;
	}
	public String getMMB_TYPE() {
		return MMB_TYPE;
	}
	public void setMMB_TYPE(String mMB_TYPE) {
		MMB_TYPE = mMB_TYPE;
	}
	public String getENT_TYPE() {
		return ENT_TYPE;
	}
	public void setENT_TYPE(String eNT_TYPE) {
		ENT_TYPE = eNT_TYPE;
	}
	public String getEST_DATE() {
		return EST_DATE;
	}
	public void setEST_DATE(String eST_DATE) {
		EST_DATE = eST_DATE;
	}
	public String getLEG_REP() {
		return LEG_REP;
	}
	public void setLEG_REP(String lEG_REP) {
		LEG_REP = lEG_REP;
	}
	public String getDOM() {
		return DOM;
	}
	public void setDOM(String dOM) {
		DOM = dOM;
	}
	public String getOP_SCOPE() {
		return OP_SCOPE;
	}
	public void setOP_SCOPE(String oP_SCOPE) {
		OP_SCOPE = oP_SCOPE;
	}
	public Date getOP_FROM() {
		return OP_FROM;
	}
	public void setOP_FROM(Date oP_FROM) {
		OP_FROM = oP_FROM;
	}
	public Date getOP_TO() {
		return OP_TO;
	}
	public void setOP_TO(Date oP_TO) {
		OP_TO = oP_TO;
	}
	public String getREG_ORG() {
		return REG_ORG;
	}
	public void setREG_ORG(String rEG_ORG) {
		REG_ORG = rEG_ORG;
	}
	public Date getAPPR_DATE() {
		return APPR_DATE;
	}
	public void setAPPR_DATE(Date aPPR_DATE) {
		APPR_DATE = aPPR_DATE;
	}
	public String getPROC_LOC() {
		return PROC_LOC;
	}
	public void setPROC_LOC(String pROC_LOC) {
		PROC_LOC = pROC_LOC;
	}
	public String getYIE_DIST() {
		return YIE_DIST;
	}
	public void setYIE_DIST(String yIE_DIST) {
		YIE_DIST = yIE_DIST;
	}
	public String getREG_CAP_CUR() {
		return REG_CAP_CUR;
	}
	public void setREG_CAP_CUR(String rEG_CAP_CUR) {
		REG_CAP_CUR = rEG_CAP_CUR;
	}
	public String getOP_STATE() {
		return OP_STATE;
	}
	public void setOP_STATE(String oP_STATE) {
		OP_STATE = oP_STATE;
	}
	public String getFOR_ENTER_NAME() {
		return FOR_ENTER_NAME;
	}
	public void setFOR_ENTER_NAME(String fOR_ENTER_NAME) {
		FOR_ENTER_NAME = fOR_ENTER_NAME;
	}
	public String getFOR_DOM() {
		return FOR_DOM;
	}
	public void setFOR_DOM(String fOR_DOM) {
		FOR_DOM = fOR_DOM;
	}
	public String getINDU_CODE() {
		return INDU_CODE;
	}
	public void setINDU_CODE(String iNDU_CODE) {
		INDU_CODE = iNDU_CODE;
	}
	public String getGXBINDU_CODE() {
		return GXBINDU_CODE;
	}
	public void setGXBINDU_CODE(String gXBINDU_CODE) {
		GXBINDU_CODE = gXBINDU_CODE;
	}
	public String getPOST_CODE() {
		return POST_CODE;
	}
	public void setPOST_CODE(String pOST_CODE) {
		POST_CODE = pOST_CODE;
	}
	public String getPHONE() {
		return PHONE;
	}
	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}
	public String getCREDIT_RATING() {
		return CREDIT_RATING;
	}
	public void setCREDIT_RATING(String cREDIT_RATING) {
		CREDIT_RATING = cREDIT_RATING;
	}
	public String getCM_CODE() {
		return CM_CODE;
	}
	public void setCM_CODE(String cM_CODE) {
		CM_CODE = cM_CODE;
	}
	public String getIS_VALID() {
		return IS_VALID;
	}
	public void setIS_VALID(String iS_VALID) {
		IS_VALID = iS_VALID;
	}
	public Date getUPDATE_DATE() {
		return UPDATE_DATE;
	}
	public void setUPDATE_DATE(Date uPDATE_DATE) {
		UPDATE_DATE = uPDATE_DATE;
	}
	
	public String getREG_CAP() {
		return REG_CAP;
	}
	
	public void setREG_CAP(String rEG_CAP) {
		REG_CAP = rEG_CAP;
	}
	
	
	public String getANCHE_YEAR() {
		return ANCHE_YEAR;
	}
	public void setANCHE_YEAR(String aNCHE_YEAR) {
		ANCHE_YEAR = aNCHE_YEAR;
	}
	
	public Date getAN_CHE_DATE() {
		return AN_CHE_DATE;
	}
	public void setAN_CHE_DATE(Date aN_CHE_DATE) {
		AN_CHE_DATE = aN_CHE_DATE;
	}
	public void afterProcess(Page page) {
		Date date = Calendar.getInstance().getTime();
        String pageKey = MD5.MD5Encode(page.getUrl().toString());
 
//        // 添加到Hbase中
//        String tableName = "newsinfo";
//        List<Map> cl = new ArrayList();
//        Map<String, String> m = new HashMap<String, String>();
//        if ("[]".equals(title)){
//            title = page.getUrl().toString();
//        }
//        m.put("url", page.getUrl().toString());
//        m.put("title", title);
//        m.put("cTime", cTime);
//        m.put("sInfo", sInfo);
//        cl.add(m);
//        HbaseUtil.insertData(tableName, pageKey, cl);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		String uscCode = page.getHtml().xpath("//*[@id='_container_baseInfo']/div/div[3]/table/tbody/tr[2]/td[2]/html()").toString();
		this.setUSC_CODE(uscCode);
		String opFrom =page.getHtml().xpath("//*[@id='_container_baseInfo']/div/div[3]/table/tbody/tr[4]/td[2]/span/html()").toString();
		opFrom=opFrom==null?"2018-03-05":opFrom.substring(0,10);
		String appDate = page.getHtml().xpath("//*[@id='_container_baseInfo']/div/div[3]/table/tbody/tr[4]/td[4]/text/html()").toString();
		appDate=appDate==null?"2018-03-05":appDate;
		Date updateTime;
		try {
			this.setOP_FROM(sdf2.parse(opFrom));
			updateTime = sdf.parse(sdf.format(date));
			this.setUPDATE_DATE(updateTime);
			this.setAN_CHE_DATE(updateTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //sdf.format(date);
		
	      // 添加到ES索引中
//        EsUtil.insertDocFromWWW(page.getUrl().toString(), pageKey, this.getINDU_CODE(), this.getAPPR_DATE(), this.getENT_NAME());
	}
	

}
