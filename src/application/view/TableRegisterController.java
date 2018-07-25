package application.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.Main;
import application.model.GH;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class TableRegisterController {
	@FXML
	private TableView<GH> table_register;
	@FXML
	private TableColumn<GH, String> code_column;
	@FXML
	private TableColumn<GH, String> name_column;
	@FXML
	private TableColumn<GH, String> date_column;
	@FXML
	private TableColumn<GH, String> expert_column;
	
	private Main main;
	
	/**
     * The data as an observable list of GH.
     */
    private ObservableList<GH> GHData = FXCollections.observableArrayList();
    
	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
	@FXML
	private void initialize() {
		code_column.setCellValueFactory(
        		cellData -> cellData.getValue().Index());
		name_column.setCellValueFactory(
        		cellData -> cellData.getValue().Name());
		date_column.setCellValueFactory(
        		cellData -> cellData.getValue().Date());
		expert_column.setCellValueFactory(
        		cellData -> cellData.getValue().Type());
		code_column.setStyle("-fx-alignment: CENTER;");
		name_column.setStyle("-fx-alignment: CENTER;");
		date_column.setStyle("-fx-alignment: CENTER;");
		expert_column.setStyle("-fx-alignment: CENTER;");
		loadData();
	}
	
	/**
	 * 用于刷新表格内容
	 */
	@FXML
	private void loadData()
	{
		GHData.clear();
		try {
			int index;
			boolean isexpert=false;
			String name;//查询病人名称
			String date;
			// 执行SQL语句
			String sql = "select dbo.T_GHXX.GHBH codeGH, dbo.T_GHXX.RQSJ time, BRMC nameP, dbo.T_HZXX.SFZJ isexpert"
					+ " from dbo.T_GHXX, dbo.T_HZXX, dbo.T_BRXX"
					+ " where dbo.T_GHXX.THBZ = ?" //未被退号
					+ " AND dbo.T_GHXX.YSBH = ? AND dbo.T_GHXX.BRBH = dbo.T_BRXX.BRBH"
					+ " AND dbo.T_GHXX.HZBH = dbo.T_HZXX.HZBH";
			PreparedStatement psmt = Main.con.prepareStatement(sql);
			psmt.setBoolean(1, false); //设置未被退号
			psmt.setString(2, Main.d.getAcount()); //设置医生编号
			ResultSet rs = psmt.executeQuery();
			
			while (rs.next()) {
				index = rs.getInt("codeGH");
				name = rs.getString("nameP");
				date = rs.getString("time");
				isexpert = rs.getBoolean("isexpert");
		        
		        GH g = new GH(index, name, date, isexpert);
		        GHData.add(g);
			}
			
			table_register.setItems(GHData);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
}
