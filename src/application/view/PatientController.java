package application.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import application.Main;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PatientController {
	@FXML
	private TextField lb_nameKS;
	@FXML
	private TextField lb_type;
	@FXML
	private TextField lb_moneyRequire;
	@FXML
	private TextField lb_moneyPay;
	@FXML
	private TextField lb_moneyReturn;
	@FXML
	private TextField lb_moneyYC;
	@FXML
	private TextField lb_nameHZ;
	@FXML
	private TextField lb_nameD;
	@FXML
	private TextField lb_code;
	@FXML
	private Button btn_register;
	@FXML
	private Button btn_clear;
	@FXML
	private Button btn_exit;
	@FXML
	private Button btn_regret;
	@FXML
	private Label lb_patientname;

	private AutoCompleteTextField autoNameKS, autoNameHZ, autoNameD, autoType;
	
	private Main main;
	
	int a;

	@FXML //整体鼠标检测
	private void suit() {
		changeNameD();
		changeType();
		changeNameHZ();
	}
	
	@FXML //整体键盘检测
	private void keysuit(KeyEvent event) {//键盘检测输入enter
		if(event.getCode() == KeyCode.ENTER  || event.getCode() == KeyCode.ALT) 
		{
			changeNameD();
			changeType();
			changeNameHZ();
		}
	}
	
	@FXML
	private void keyGH(KeyEvent event)
	{
		if(event.getCode() == KeyCode.ENTER) 
		{
			GH(null);
		}
	}
	
	@FXML
	private void keyclear(KeyEvent event)
	{
		if(event.getCode() == KeyCode.ENTER) 
		{
			clearAll(null);
		}
	}
	
	@FXML
	private void keyexit(KeyEvent event)
	{
		if(event.getCode() == KeyCode.ENTER) 
		{
			exitnow(null);
		}
	}
	@FXML
	private void keyRegret(KeyEvent event)
	{
		if(event.getCode() == KeyCode.ENTER) 
		{
			mouseRegret();
		}
	}
	

	/**
	 * 改变医生名称的候选项
	 */
	@FXML
	private void changeNameD() {
		try {
			if(lb_nameKS==null) return;
			if(lb_nameKS.getText()==null||lb_nameKS.getText().equals("")) {
				return;
			}
//			if(lb_nameD.getText()!=null) return;
			
			String codeKS = "";// 科室信息
			String sqlks = "select dbo.T_KSYS.YSMC nameD from dbo.T_KSXX, dbo.T_KSYS"
					+ " where dbo.T_KSXX.KSMC = ? AND"
					+ "  dbo.T_KSXX.KSBH = dbo.T_KSYS.KSBH";
			PreparedStatement stmt = Main.con.prepareStatement(sqlks);
			stmt.setString(1, lb_nameKS.getText());// 设置科室名称
			ResultSet rd = stmt.executeQuery();
			
			String nameD;
			List<String> nameDlist = new ArrayList<String>();
			while (rd.next()) {
				// 获取医生名称和科室
				nameD = rd.getString("nameD");
				nameDlist.add(nameD.trim());
			}
			
			autoNameD.setCacheDataList(nameDlist);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 改变号种名称的候选项
	 */
	@FXML
	private void changeNameHZ() {
		try {
			if(lb_type==null || lb_nameKS==null) return;
			if(lb_type.getText()==null||lb_type.getText().equals("")) {
				return;
			}
			if(lb_nameKS.getText()==null || lb_nameKS.getText().equals("")) {
				return;
			}
//			if(lb_nameHZ.getText()!=null) return;
			
			String nameHZ;
			List<String> nameHZlist = new ArrayList<String>();
			
			boolean isexpert = lb_type.getText().equals("专家号")? true: false;	
			int cost=0;
			
			String sqlks = "select HZMC, GHFY from dbo.T_KSXX, dbo.T_HZXX"
					+ " where dbo.T_KSXX.KSMC = ?"
					+ " AND dbo.T_HZXX.KSBH = dbo.T_KSXX.KSBH"
					+ " AND dbo.T_HZXX.SFZJ = ?";
			PreparedStatement stmt = Main.con.prepareStatement(sqlks);
			stmt.setString(1, lb_nameKS.getText());//设置科室名称
			stmt.setBoolean(2, isexpert);//设置科室名称
			
			ResultSet rhz = stmt.executeQuery();
	        
			while(rhz.next()) {
				nameHZ = rhz.getString("HZMC");
				nameHZlist.add(nameHZ.trim());
				cost = rhz.getInt("GHFY");
			}
			lb_moneyRequire.setText(cost + "");
			
			//预存金额足够
			if(Integer.parseInt(lb_moneyYC.getText())>cost)
			{
				lb_moneyPay.setEditable(false);
			}
			else {
				lb_moneyPay.setEditable(true);
			}
			autoNameHZ.setCacheDataList(nameHZlist);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 改变号种类别的候选项
	 */
	@FXML
	private void changeType() {
		if(lb_nameD==null) return;
		if(lb_nameD.getText()==null || lb_nameD.getText().equals("")) {
			return;
		}
//		if(lb_type.getText()!=null) return;
		List<String> typelist = new ArrayList<String>();
		typelist.clear();
		

		boolean isexpert = false;
		// 查询科室医生表
		try {
			String sqld = "select * from dbo.T_KSYS where YSMC = ?";
			PreparedStatement psmtd = Main.con.prepareStatement(sqld);
			psmtd.setString(1, lb_nameD.getText());// 设置医生名称
			ResultSet rd = psmtd.executeQuery();
			while (rd.next()) {
				isexpert = rd.getBoolean("SFZJ");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (isexpert) {
			typelist.add("专家号");
			typelist.add("普通号");
		} else {
			typelist.add("普通号");
		}

		autoType.setCacheDataList(typelist);

	}

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		lb_patientname.setText(Main.p.getName());
		btn_regret.setFocusTraversable(false);
		autoCompleteInitialize();
		lb_moneyYC.setText("0");
		// 先初始化预存金额
		try {
			String sql = "select * from dbo.T_BRXX where BRBH = ?";
			PreparedStatement psmt = Main.con.prepareStatement(sql);
			psmt.setString(1, Main.p.getAcount());
			ResultSet rs = psmt.executeQuery();

			while (rs.next()) {
				lb_moneyYC.setText(rs.getInt("YCJE") + "");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void autoCompleteInitialize() {
		autoNameKS = AutoCompleteTextFieldBuilder.build(lb_nameKS);
		autoNameHZ = AutoCompleteTextFieldBuilder.build(lb_nameHZ);
		autoNameD = AutoCompleteTextFieldBuilder.build(lb_nameD);
		autoType = AutoCompleteTextFieldBuilder.build(lb_type);
		try {
			String nameKS;// 科室信息
			List<String> nameKSlist = new ArrayList<String>();

			Statement stmt = Main.con.createStatement();
			// 查询科室信息表
			ResultSet rks = stmt.executeQuery("select * from dbo.T_KSXX");

			while (rks.next()) {
				// 获取医生名称和科室
				nameKS = rks.getString("KSMC");
				nameKSlist.add(nameKS.trim());
			}

			autoNameKS.setCacheDataList(nameKSlist);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Event Listener on Button[#btn_register].onMouseClicked
	/**
	 * 1.获取[病人编号] 2.用医生名称,查询科室医生表，获取[医生编号] 3.号种名称加号种类别，查询号种信息表，获取[号种编号],[挂号费用]
	 * 4.号种编号，查询挂号信息表，统计[挂号人次]
	 */
	/**
	 * @param event
	 */
	@FXML
	public void GH(MouseEvent event) {
		try {
			String codeP, codeD = "300001", codeHZ = "400001", codeKS="";
			String nameHZ = lb_nameHZ.getText(), nameKS="";
			int num_GH = 1, cost = 0, limit = 0;
			boolean typeHZ;
			if (lb_type.getText().equals("专家号"))
				typeHZ = true;
			else
				typeHZ = false;
			codeP = main.p.getAcount();// 获取病人编号

			String sql;
			PreparedStatement psmt;
			ResultSet rs;
			
			//获取号种信息
			sql = "select * from dbo.T_HZXX where HZMC = ? AND SFZJ = ?";
			psmt = Main.con.prepareStatement(sql);
			psmt.setString(1, nameHZ);// 设置号种名称
			psmt.setBoolean(2, typeHZ);// 设置号种类型
			rs = psmt.executeQuery();
			while (rs.next()) {
				// 获取[号种编号],[挂号费用]
				codeHZ = rs.getString("HZBH");
				cost = rs.getInt("GHFY");
				limit = rs.getInt("GHRS");//挂号人数限制
			}
			
			//余额不足处理
			boolean mYcChange = false;
			int mYC = Integer.parseInt(lb_moneyYC.getText());//肯定不为空
			int mPay = lb_moneyPay.getText().equals("")? 0: Integer.parseInt(lb_moneyPay.getText());
			int mRe = lb_moneyRequire.getText().equals("")? 0: Integer.parseInt(lb_moneyRequire.getText());
			int mReturn = -1;
			
			if(cost != mRe) {
				//金额变更提示
				lb_moneyRequire.setText(cost+"");
				JOptionPane.showMessageDialog(null, 
						"金额已经发生了变更\n" + mRe + "元->" + cost + "元\n 请重新确认.", 
						"Sorry!", 0);
				return;
			}
			
			//1.预存金额足够，无需支付
			//2.预存金额不足，需要支付
			//3.没有预存金额，完全支付
			if(mYC >= mRe) { //预存金额大于要求金额
				mYC -= mRe;
				mYcChange = true;//更新数据库
			}
			else if(mYC != 0 && (mYC + mPay >= mRe)) {//预存金额不足
				mYC = 0;
				mYcChange = true;//更新数据库
				mReturn = mYC + mPay - mRe;
			}
			else if((mYC == 0) && mPay >= mRe) {//预存金额没有，需要支付
				mReturn = mYC - mRe;
			}
			else { //余额不足
				JOptionPane.showMessageDialog(null, "金额不足，请重新支付", "Sorry!", 0);
				return;
			}
			
			//查询今日挂这个号的人次
			sql = "select count(*) nums from dbo.T_GHXX where HZBH = ?"
					+ " AND RQSJ >= cast(? as datetime)";
			psmt = Main.con.prepareStatement(sql);
			psmt.setString(1, codeHZ);// 设置号种编号
			psmt.setString(2, Main.dateFormat.format(new Date()));// 设置日期
			rs = psmt.executeQuery();
			while (rs.next()) {
				num_GH = rs.getInt("nums");
			}
			if(limit < num_GH) {//超过人数限制
				JOptionPane.showMessageDialog(null, 
						"该号种已经达到今日号数限制"+limit+"\n请明日再进行尝试!", "对不起", 0);
				return;
			}
			num_GH++;//现在挂号人次
			
			//查询医生科室信息
			sql = "select dbo.T_KSYS.YSBH codeD"
					+ " from dbo.T_KSYS, dbo.T_KSXX where YSMC = ?"
					+ " AND dbo.T_KSXX.KSBH = dbo.T_KSYS.KSBH";
			psmt = Main.con.prepareStatement(sql);
			psmt.setString(1, lb_nameD.getText());// 设置医生名称
			rs = psmt.executeQuery();
			while (rs.next()) {
				// 获取医生编号，科室名称
				codeD = rs.getString("codeD");
			}
			
			//总挂号编号
			sql = "select count(*) I from dbo.T_GHXX";
			psmt = Main.con.prepareStatement(sql);
			rs = psmt.executeQuery();
			int rows = 1;
			while (rs.next()) {
				rows = rs.getInt("I");
			}
			rows++;
			
			sql = "insert into dbo.T_GHXX" + "(GHBH, HZBH, YSBH, BRBH, GHRC, THBZ, GHFY, RQSJ) "
					+ "values(?,?,?,?,?,?,?,?)";// 参数用?表示，相当于占位符;
			psmt = Main.con.prepareStatement(sql);

			// 先对应SQL语句，给SQL语句传递参数
			String time = Main.dateFormat.format(new Date());
			psmt.setInt(1, rows);
			psmt.setString(2, codeHZ);
			psmt.setString(3, codeD);
			psmt.setString(4, codeP);
			psmt.setInt(5, num_GH);
			psmt.setBoolean(6, false);
			psmt.setInt(7, cost);
			psmt.setString(8, time);
			String s = "" + rows;
			int a = 6 - s.length();
			while ((a--) != 0) {
				s = "0" + s;
			}
			// 执行SQL语句
			psmt.execute();			
			lb_code.setText(s);//设置挂号编号
			btn_regret.setFocusTraversable(true);
			
			String information = "恭喜您，挂号成功!\n您的号码是 " + s + " 本次挂号费用为 " + cost + " 元";
			if(mReturn>=0)//需要找零
			{
				lb_moneyReturn.setText(mReturn + "");
				lb_moneyPay.setText("");
				information += ",\n找零 " + mReturn + " 元";
			}
			JOptionPane.showMessageDialog(null, information, "恭喜", 1);
			
			if(mYcChange)
			{
				//病人预存金额更新
				String sqlupdate="update dbo.T_BRXX set YCJE = ? where BRBH = ?";
		        PreparedStatement psmtupdate = Main.con.prepareStatement(sqlupdate);
		        psmtupdate.setInt(1, mYC);
		        psmtupdate.setString(2, main.p.getAcount());
		        psmtupdate.execute();
		        lb_moneyYC.setText(mYC + "");//病人钱变少
			}
			
			a = 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			if(a<=10){//允许10次冲突
				System.out.println(e.toString() + " 重复挂号");
				GH(null); //数据库重复数据,挂号失败，重新尝试挂号.
				a++;
			}
			else{
				e.printStackTrace();
			}
		} 
	}

	// Event Listener on Button[#btn_clear].onMouseClicked
	@FXML
	public void clearAll(MouseEvent event) {
		// TODO Autogenerated
		// 预存金额不需要清零
		lb_type.clear();
		lb_nameHZ.clear();
		lb_moneyRequire.clear();
		lb_moneyPay.clear();
		lb_moneyReturn.clear();
		lb_nameKS.clear();
		lb_nameD.clear();
		lb_code.clear();
		lb_nameKS.requestFocus();
		btn_regret.setFocusTraversable(false);
	}

	// Event Listener on Button[#btn_exit].onMouseClicked
	@FXML
	public void exitnow(MouseEvent event) {
		// TODO Autogenerated
		Main.primaryStage.close();
	}
	
	@FXML
	public void mouseRegret() {
		try {
			if(lb_code.getText() == null || lb_code.getText().equals("")) return;
			//退票
			String sql="update dbo.T_GHXX set THBZ = ?, GHFY = ? where GHBH = ?";//参数用?表示，相当于占位符
			PreparedStatement psmt = Main.con.prepareStatement(sql);
			psmt.setBoolean(1, true);
			psmt.setInt(2, 0); //费用清零
			int index = Integer.parseInt(lb_code.getText());
			psmt.setInt(3, index);
			psmt.execute();			
			
			//退还挂号钱
			JOptionPane.showMessageDialog(null, "退还挂号费用 " + lb_moneyRequire.getText() + " 元!", "退还挂号费用", 1);
			clearAll(null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setMain(Main main) {
		this.main = main;
	}
}

