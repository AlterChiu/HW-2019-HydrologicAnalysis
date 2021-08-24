package AtRework.DataBase.Insert;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import AtRework.Global;
import AtRework.DataBase.Grid;
import asciiFunction.AsciiBasicControl;
import usualTool.AtCommonMath;
import usualTool.TimeTranslate;
import usualTool.AtCommonMath.StaticsModel;

public class InsertRainfallValues {
	// key for row_column
	private Map<String, Grid> gridMap = new HashMap<>();

	public InsertRainfallValues(Connection con) throws Exception {
		String asciiRootAdd = Global.asciiFolder;
		for (String asciiName : new File(asciiRootAdd).list()) {
			System.out.print(asciiName + "\tstart");

			// get ascii properties
			long dateLong = TimeTranslate.getDateLong(asciiName.split(".asc")[0], "yyyyMMddHH");
			String year = TimeTranslate.getDateString(dateLong, "yyyy");
			PreparedStatement pre = con.prepareStatement(getPreparedSql(year));

			AsciiBasicControl ascii = new AsciiBasicControl(asciiRootAdd + asciiName);
			System.out.print("\tgetValues");
			for (int row = 0; row < ascii.getRow(); row++) {
				System.out.println(row);
				for (int column = 0; column < ascii.getColumn(); column++) {
					if (!ascii.isNull(column, row)) {

						// get grid properties
						String key = row + "_" + column;
						double[] coordinate = ascii.getCoordinate(column, row);
						Grid grid = Optional.ofNullable(gridMap.get(key))
								.orElse(new Grid(coordinate[0], coordinate[1]));

						// add values and update
						grid.addValues(Double.parseDouble(ascii.getValue(column, row)));
						updateStatement(pre, grid, dateLong);
						this.gridMap.put(key, grid);
					}
				}

				if (row % 3 == 0) {
					pre.executeBatch();
					pre.clearBatch();
				}
			}

			System.out.print("\tupdate");
			pre.executeBatch();
			pre.clearBatch();
			con.commit();
			System.out.println("\tend");
		}
	}

	private String getPreparedSql(String year) {
		StringBuilder sql = new StringBuilder();
		sql.append("replace into qpe." + Global.valueTable + year + "(");
		sql.append("name , date , R1H ,R2H ,  R3H , R6H , R9H , R12H ,R18H , R24H , R48H , R72H)");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?)");
		return sql.toString();
	}

	private void updateStatement(PreparedStatement pre, Grid grid, long dateLong) throws Exception {
		pre.setString(1, grid.name);
		pre.setTimestamp(2, new Timestamp(dateLong));
		pre.setDouble(3, grid.getValuesSum(1));
		pre.setDouble(4, grid.getValuesSum(2));
		pre.setDouble(5, grid.getValuesSum(3));
		pre.setDouble(6, grid.getValuesSum(6));
		pre.setDouble(7, grid.getValuesSum(9));
		pre.setDouble(8, grid.getValuesSum(12));
		pre.setDouble(9, grid.getValuesSum(18));
		pre.setDouble(10, grid.getValuesSum(24));
		pre.setDouble(11, grid.getValuesSum(48));
		pre.setDouble(12, grid.getValuesSum(72));
		pre.addBatch();
	}

}
