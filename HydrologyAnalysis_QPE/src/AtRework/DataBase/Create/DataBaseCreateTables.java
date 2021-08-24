package AtRework.DataBase.Create;

import java.sql.Connection;
import java.sql.SQLException;

import AtRework.Global;

public class DataBaseCreateTables {

	public static void CreateRainfallValuesTable(Connection con, int year) throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append("create table qpe." + Global.valueTable + year + "(");
		sql.append("name CHAR(20) not null,");
		sql.append("date timestamp not null,");
		sql.append("R1H real,");
		sql.append("R2H real,");
		sql.append("R3H real,");
		sql.append("R6H real,");
		sql.append("R9H real,");
		sql.append("R12H real,");
		sql.append("R18H real,");
		sql.append("R24H real,");
		sql.append("R48H real,");
		sql.append("R72H real,");
		sql.append("PRIMARY KEY (name, date)");
		sql.append(")");

		try {
			con.prepareStatement(sql.toString()).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void CreateRainfallGridTable(Connection con) throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append("create table +" + Global.gridTable + "(");
		sql.append("name CHAR(20) not null,");
		sql.append("WGS84_X real,");
		sql.append("WGS84_Y real,");
		sql.append("TWD97_X real,");
		sql.append("TWD97_Y real,");
		sql.append("PRIMARY KEY (name)");
		sql.append(")");

		try {
			con.prepareStatement(sql.toString()).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
