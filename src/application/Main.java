package application;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import application.model.Doctor;
import application.model.Patient;
import application.view.DoctorController;
import application.view.LoginController;
import application.view.PatientController;
import application.view.TableDoctorController;
import application.view.TableRegisterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author DYD
 *
 */
public class Main extends Application {

	public static java.sql.Connection con;// 数据库建立的连接
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
	public static Stage primaryStage; // 主界面stage
	public static Doctor d;
	public static Patient p;
	private BorderPane doctorLayout;

	/**
	 *  主函数，关闭数据库连接
	 */
	public static void main(String[] args) {
		launch(args);
		try {
			if(con==null) return;
			if (!con.isClosed()) {
				con.close();
				System.out.println("断开数据库连接!");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 显示医生主界面
	 */
	public void showDoctorview() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/doctor.fxml"));
			doctorLayout = (BorderPane) loader.load();

			// Show the scene containing the login layout.
			Scene scene = new Scene(doctorLayout);
			primaryStage.close();
			primaryStage.setScene(scene);
			primaryStage.show();
			
			DoctorController controller = loader.getController();
            controller.setMain(this);
            controller.registertable(null);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示登录主界面
	 */
	public void showLoginview() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/login.fxml"));
			AnchorPane loginLayout = (AnchorPane) loader.load();

			// Show the scene containing the login layout.
			Scene scene = new Scene(loginLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			LoginController controller = loader.getController();
            controller.setMain(this);
            
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示病人界面
	 */
	public void showPatientrview() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/patient.fxml"));
			SplitPane patientLayout = (SplitPane) loader.load();

			// Show the scene containing the login layout.
			Scene scene = new Scene(patientLayout);
			primaryStage.close();
			primaryStage.setScene(scene);
			primaryStage.show();
			
			PatientController controller = loader.getController();
			controller.setMain(this);
            
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示挂号列表界面
	 */
	public void tableRegisterLoad() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/tableregister.fxml"));
			AnchorPane RELayout = (AnchorPane) loader.load();
			
			TableRegisterController controller = loader.getController();
            controller.setMain(this);
            
			doctorLayout.setCenter(RELayout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 显示收入列表界面
	 */
	public TableDoctorController tableIncomeLoad() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/tabledoctor.fxml"));
			AnchorPane DocLayout = (AnchorPane) loader.load();
			
			TableDoctorController controller = loader.getController();
            
			doctorLayout.setCenter(DocLayout);
			return controller;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	/**
	 * 初始化数据库连接，应用关闭的时候释放
	 */
	public void sql_initial() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			String strCon = "jdbc:sqlserver://10.14.125.51:1433;databaseName=hospital";
			String strCon = "jdbc:sqlserver://localhost:1433;databaseName=hospital";
			String strUserName = "sa"; // 数据库的用户名称
			String strPWD = "808511"; // 数据库的密码
			con = java.sql.DriverManager.getConnection(strCon, strUserName, strPWD);
			if(con==null) {
				System.out.println("数据库连接失败!");
				primaryStage.close();
			}
			System.out.println("已顺利连接到数据库。");
		}
		// 捕获异常并进行处理
		catch (java.lang.ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (java.sql.SQLException se) {
			System.out.println(se.getMessage());
		}
	}

	@Override
	public void start(Stage primaryStage) {
		Main.primaryStage = primaryStage;
		Main.primaryStage.setTitle("医院挂号系统");
		sql_initial();
		showLoginview();
	}

}
