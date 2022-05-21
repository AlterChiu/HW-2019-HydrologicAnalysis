package AtRework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.gdal.ogr.Geometry;

import AtRework.GridModel.Grid;
import Hydro.Rainfall.ReturnPeriod.RetrunPeriod;
import asciiFunction.AsciiBasicControl;
import asciiFunction.XYZToAscii;
import geo.gdal.GdalGlobal;
import geo.gdal.SpatialReader;
import usualTool.AtCommonMath;
import usualTool.AtFileWriter;

public class AtTesting {

	public static void main(String[] args) throws NumberFormatException, Exception {
		// TODO Auto-generated method stub

		String asciiFolder = "W:\\OneDrive - IISIGroup\\IISI\\國土二組文件\\110 -研討會論文\\數據資料\\各年份最大值\\";

		Double[] values = new Double[] { 154.96, 564.51, 737.64, 382.68, 165.64, 181.42, 643.98, 398.03, 154.62, 484.83,
				442.35, 255.33, 194.74 };

		for (Global.rainfallDistribute distribution : Global.rainfallDistribute.values()) {
			RetrunPeriod returnPeriodDis = distribution.getDistribute(Arrays.asList(values));

			System.out.println(distribution.name());

			for (int year : Global.rainfallReturnYear) {
				System.out.println(year + "\t" + returnPeriodDis.getPeriodRainfall(year));
			}

		}

//		List<String[]> values = new ArrayList<>();
//		values.add(new String[] { "24_min", "24_mean", "24_max", "24_std", "48_min", "48_mean", "48_max", "48_std" });
//		for (int year = Global.startYear; year <= Global.endYear; year++) {
//			List<String> temptList = new ArrayList<>();
//
//			AsciiBasicControl ascii24 = new AsciiBasicControl(asciiFolder + year + "_24.asc");
//			List<Double> values24 = getValues(ascii24);
//			AtCommonMath math24 = new AtCommonMath(values24);
//			System.out.println(values24.size());
//			temptList.add(String.valueOf(math24.getMin()));
//			temptList.add(String.valueOf(math24.getMean()));
//			temptList.add(String.valueOf(math24.getMax()));
//			temptList.add(String.valueOf(math24.getStd()));
//
//			AsciiBasicControl ascii48 = new AsciiBasicControl(asciiFolder + year + "_48.asc");
//			List<Double> values48 = getValues(ascii48);
//			AtCommonMath math48 = new AtCommonMath(values48);
//			temptList.add(String.valueOf(math48.getMin()));
//			temptList.add(String.valueOf(math48.getMean()));
//			temptList.add(String.valueOf(math48.getMax()));
//			temptList.add(String.valueOf(math48.getStd()));
//
//			values.add(temptList.parallelStream().toArray(String[]::new));
//		}
//
//		new AtFileWriter(values.parallelStream().toArray(String[][]::new),
//				"W:\\OneDrive - IISIGroup\\IISI\\國土二組文件\\110 -研討會論文\\數據資料\\各年份最大值\\石門水庫雨量統計.csv").csvWriter();

//		AsciiBasicControl ascii = new AsciiBasicControl(
//				"W:\\OneDrive\\工作用\\計劃案\\110 - 北科\\格網水文\\110-架構\\asciiTemptlapte.asc");
//
//		Geometry boundary = new SpatialReader(
//				"W:\\OneDrive - IISIGroup\\IISI\\國土二組文件\\110 -研討會論文\\數據資料\\出圖\\SM_Bounday_WGS84.geojson")
//						.getGeometryList().get(0);
//
//		// get gird which inside boundary
//		List<Grid> gridList = new ArrayList<>();
//
//		for (int row = 0; row < ascii.getRow(); row++) {
//			for (int column = 0; column < ascii.getColumn(); column++) {
//				if (!ascii.isNull(column, row)) {
//					double cor[] = ascii.getCoordinate(column, row);
//					if (boundary.Contains(GdalGlobal.CreatePoint(cor[0], cor[1]))) {
//						gridList.add(new Grid(cor[0], cor[1]));
//					}
//				}
//			}
//		}
//		System.out.println("parseComplete");
//
//		for (int year = Global.startYear; year <= Global.endYear; year++) {
//			System.out.println(year + "year start");
//			List<Double[]> xyz48List = new ArrayList<>();
//			List<Double[]> xyz24List = new ArrayList<>();
//
//			for (Grid grid : gridList) {
//
//				double yearMax48 = Double
//						.parseDouble(Optional.ofNullable(grid.getYearMaxRainfall().get(year).get(48)).orElse("0.0"));
//				xyz48List.add(new Double[] { grid.getX(), grid.getY(), yearMax48 });
//
//				double yearMax24 = Double
//						.parseDouble(Optional.ofNullable(grid.getYearMaxRainfall().get(year).get(24)).orElse("0.0"));
//				xyz24List.add(new Double[] { grid.getX(), grid.getY(), yearMax24 });
//			}
//
//			XYZToAscii to24Ascii = new XYZToAscii(xyz24List);
//			to24Ascii.setCellSize(0.0125);
//			to24Ascii.start();
//			to24Ascii.saveAscii("W:\\OneDrive - IISIGroup\\IISI\\國土二組文件\\110 -研討會論文\\數據資料\\各年份最大值\\" + year + "_24.asc");
//
//			XYZToAscii to48Ascii = new XYZToAscii(xyz48List);
//			to48Ascii.setCellSize(0.0125);
//			to48Ascii.start();
//			to48Ascii.saveAscii("W:\\OneDrive - IISIGroup\\IISI\\國土二組文件\\110 -研討會論文\\數據資料\\各年份最大值\\" + year + "_48.asc");

	}

	public static List<Double> getValues(AsciiBasicControl ascii) {
		List<Double> valueList = new ArrayList<>();
		for (int row = 0; row < ascii.getRow(); row++) {
			for (int column = 0; column < ascii.getColumn(); column++) {
				if (!ascii.isNull(column, row)) {
					valueList.add(Double.parseDouble(ascii.getValue(column, row)));
				}
			}
		}
		return valueList;
	}

}
