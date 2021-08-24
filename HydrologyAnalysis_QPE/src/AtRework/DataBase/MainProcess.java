package AtRework.DataBase;

import java.sql.Connection;
import java.sql.SQLException;

import AtRework.Global;
import AtRework.DataBase.Create.DataBaseCreateTables;
import AtRework.DataBase.Insert.InsertRainfallValues;
import database.AtDbBasicControl;
import database.AtDbBasicControl.DbDriver;

public class MainProcess {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		// STEP 1 create/link to sqlLite database
		AtDbBasicControl db = new AtDbBasicControl("localhost:3306", DbDriver.MYSQL);
		Connection con = db.getConnection();

//		// STEP 2 create tables
//		// create grid properties
//		DataBaseCreateTables.CreateRainfallGridTable(con);
//
		// create year values
		for (int year = Global.startYear; year <= Global.endYear; year++) {
			DataBaseCreateTables.CreateRainfallValuesTable(con, year);
		}

		// STEP 3 insert from ascii file
		// insert values
		new InsertRainfallValues(con);

	}

}
