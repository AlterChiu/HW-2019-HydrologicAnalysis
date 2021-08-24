package AtRework.DataBase;

import java.util.ArrayList;
import java.util.List;

import AtRework.Global;
import geo.gdal.GdalGlobal;
import usualTool.AtCommonMath;
import usualTool.AtCommonMath.StaticsModel;

public class Grid {
	public String name;
	private List<Double> values = new ArrayList<>();
	public double wgs84X;
	public double wgs84Y;
	public double twd97X;
	public double twd97Y;

	public Grid(double wgs84x, double wgs84y) {
		String xString = AtCommonMath.getDecimal_String(wgs84x, Global.coordinateDecimal);
		String yString = AtCommonMath.getDecimal_String(wgs84y, Global.coordinateDecimal);
		this.name = xString + "_" + yString;

		this.wgs84X = wgs84x;
		this.wgs84Y = wgs84y;

		double[] twd97 = GdalGlobal.CoordinateTranslator(this.wgs84X, this.wgs84Y, GdalGlobal.WGS84_prj4,
				GdalGlobal.TWD97_121_prj4);
		this.twd97X = twd97[0];
		this.twd97Y = twd97[1];

	}

	public void addValues(double value) {
		this.values.add(value);
		if (this.values.size() > Global.maxDuration)
			this.values.remove(0);
	}

	public double getValuesSum(int previousHours) throws Exception {
		if (previousHours > Global.maxDuration) {
			new Exception("*ERROR* previousHours over max rainfallduration").printStackTrace();
			return 0;
		} else {
			if (this.values.size() <= previousHours) {
				return AtCommonMath.getListStatistic(this.values, StaticsModel.getSum);
			} else {
				List<Double> temptValues = new ArrayList<>();
				int lastIndex = this.values.size() - 1;
				for (int index = 0; index < previousHours; index++) {
					temptValues.add(this.values.get(lastIndex - index));
				}
				return AtCommonMath.getListStatistic(temptValues, StaticsModel.getSum);
			}
		}
	}
}