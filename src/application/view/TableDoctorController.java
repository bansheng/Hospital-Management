package application.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import application.Main;
import application.model.DoctorIncome;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class TableDoctorController {
	@FXML
	private TableView<DoctorIncome> table_doctor;
	@FXML
	private TableColumn<DoctorIncome, String> nameKS_column;
	@FXML
	private TableColumn<DoctorIncome, String> code_column;
	@FXML
	private TableColumn<DoctorIncome, String> nameDoctor_column;
	@FXML
	private TableColumn<DoctorIncome, String> type_column;
	@FXML
	private TableColumn<DoctorIncome, String> num_column;
	@FXML
	private TableColumn<DoctorIncome, String> income_column;
	@FXML
	private Button btn_refresh;
	
	/**
     * The data as an observable list of GH.
     */
    private ObservableList<DoctorIncome> DoctorIncomeData = FXCollections.observableArrayList();
    
    public SimpleDateFormat  mydateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式
    
    private String checkDateTimeBegin="2018-01-01 00:00:00", checkDateTimeEnd = "2018-12-30 00:00:00";
	private Vector<String> codeHZ = new Vector<String>();
	private Vector<String> codeD = new Vector<String>();
	private Vector<String> nameKS = new Vector<String>();
	private Vector<String> nameD = new Vector<String>();
	private Vector<Boolean> isexpert = new Vector<Boolean>();
	private Vector<Integer> population = new Vector<Integer>();
	private Vector<Integer> Income = new Vector<Integer>();
	private Map<String, Integer> reflect = new HashMap();
    
    public void SetDateTime(String dateTime1, String dateTime2) {
    	checkDateTimeBegin = dateTime1;
    	checkDateTimeEnd = dateTime2;
    }
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
	@FXML
	private void initialize() {
		nameKS_column.setCellValueFactory(
        		cellData -> cellData.getValue().NameKS());
		code_column.setCellValueFactory(
        		cellData -> cellData.getValue().CodeDoctor());
		nameDoctor_column.setCellValueFactory(
        		cellData -> cellData.getValue().NameDoctor());
		type_column.setCellValueFactory(
        		cellData -> cellData.getValue().Type());
		num_column.setCellValueFactory(
        		cellData -> cellData.getValue().Num());
		income_column.setCellValueFactory(
        		cellData -> cellData.getValue().Income());
		checkDateTimeBegin = mydateFormat.format(new Date()) + " 0:00:00";
		checkDateTimeEnd = mydateFormat.format(new Date()) + " 0:00:00";
		code_column.setStyle("-fx-alignment: CENTER;");
		nameKS_column.setStyle("-fx-alignment: CENTER;");
		nameDoctor_column.setStyle("-fx-alignment: CENTER;");
		type_column.setStyle("-fx-alignment: CENTER;");
		num_column.setStyle("-fx-alignment: CENTER;");
		income_column.setStyle("-fx-alignment: CENTER;");
		
//		loadData();
		superloadData();
	}
	
	/**
	 * 科室信息表，科室名称
	 * 科室医生信息表，医生名称
	 * 挂号信息表，医生编号，挂号费用,时间日期，注意这个部分要筛选时间日期
	 * 科室号种信息表，号种类别
	 * O(n)
	 */
	public void superloadData() {
		DoctorIncomeData.clear();
		try {
			int row = 0;
			String SFZJ = null;
			int cost;
			codeD.clear();
			codeHZ.clear();
			nameKS.clear();
			nameD.clear();
			isexpert.clear();
			population.clear();
			Income.clear();
			
			reflect.clear();//用map获得字符串对应下标
			String sql = "SELECT   dbo.T_KSYS.YSMC AS nameD, dbo.T_KSYS.YSBH AS codeD, dbo.T_HZXX.SFZJ AS isexpert, \r\n" + 
					"                dbo.T_HZXX.GHFY AS singlecost, dbo.T_KSXX.KSMC AS nameKS, dbo.T_HZXX.HZBH AS codeHZ\r\n" + 
					"FROM      dbo.T_KSXX INNER JOIN\r\n" + 
					"                dbo.T_KSYS ON dbo.T_KSXX.KSBH = dbo.T_KSYS.KSBH INNER JOIN\r\n" + 
					"                dbo.T_HZXX ON dbo.T_KSYS.KSBH = dbo.T_HZXX.KSBH\r\n" + 
					"WHERE   (dbo.T_HZXX.SFZJ = 'FALSE') OR\r\n" + 
					"                (dbo.T_KSYS.SFZJ = 'TRUE')";
					
			Statement stmt = Main.con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				reflect.put(rs.getString("codeD").trim() + rs.getString("codeHZ").trim(), row++);//建立字符串映射
				codeD.addElement(rs.getString("codeD"));
				codeHZ.addElement(rs.getString("codeHZ"));
				nameKS.addElement(rs.getString("nameKS"));
				nameD.addElement(rs.getString("nameD"));
				isexpert.addElement(rs.getBoolean("isexpert"));
				population.addElement(0);
				Income.addElement(0);
			}
			String sql2= "select * from dbo.T_GHXX where"
//					+ " THBZ = false"//退号标志
					+ " RQSJ"
					+ " between cast(? as datetime) and cast(? as datetime) ";
			PreparedStatement psmt = Main.con.prepareStatement(sql2);
			psmt.setString(1, checkDateTimeBegin);
			psmt.setString(2, checkDateTimeEnd);
			ResultSet rsgh = psmt.executeQuery();
			
			while (rsgh.next()) {
				int i = reflect.get(rsgh.getString("YSBH").trim() + rsgh.getString("HZBH").trim());//获得下标			
				
				cost = rsgh.getInt("GHFY");
				Income.setElementAt(Income.elementAt(i)+cost, i);
				population.setElementAt(population.elementAt(i)+1, i);
			}
			
			for(int i=0; i<nameD.size(); i++) {
				SFZJ = isexpert.elementAt(i)? "专家号": "普通号";
				DoctorIncome d = new DoctorIncome(nameKS.elementAt(i), codeD.elementAt(i),
						nameD.elementAt(i), SFZJ, population.elementAt(i), Income.elementAt(i));
				DoctorIncomeData.add(d);
			}
			
			table_doctor.setItems(DoctorIncomeData);
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
