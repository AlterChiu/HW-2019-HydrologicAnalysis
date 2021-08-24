package AtRework.Rainfall.Original.ToAscii;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.nodes.Element;

import AtRework.Global;
import AtRework.GridModel.Grid;
import AtRework.Rainfall.Original.Adaptor.RainfallAdaptor.RainfallModel;
import asciiFunction.AsciiBasicControl;
import asciiFunction.XYZToAscii;
import usualTool.AtFileWriter;
import usualTool.AtXmlReader;
import usualTool.TimeTranslate;

public class PiXmlToAscii {
	private static int piXmlTimeZone = 0;
	private static String nullValue = "-999";

	public PiXmlToAscii(String piXmlFolder) throws IOException {
		String piXmlFiles[] = new File(piXmlFolder).list();

		AsciiBasicControl emptyAscii = getAsciiTemplate(piXmlFolder + piXmlFiles[0]);

		for (int index = 1; index < piXmlFiles.length; index++) {
			System.out.println(piXmlFiles[index]);

			// get timeSeries rainfall data
			// timeString , xyzList
			Map<String, List<Double[]>> timeSeries = new HashMap<>();

			AtXmlReader temptPiXml = new AtXmlReader(piXmlFolder + piXmlFiles[index], "UTF-8");
			temptPiXml.getNodeByTag("series").forEach(seriesElement -> {

				// get properties
				Element headerElement = seriesElement.getElementsByTag("header").get(0);
				double x = Double.parseDouble(headerElement.getElementsByTag("x").get(0).text());
				double y = Double.parseDouble(headerElement.getElementsByTag("y").get(0).text());

				// get event(rainfall) value
				seriesElement.getElementsByTag("event").forEach(eventElement -> {
					String date = eventElement.attr("date");
					String time = eventElement.attr("time");
					double value = Double.parseDouble(eventElement.attr("value"));

					// get value without null value
					if (value >= 0) {
						String twDate;
						try {
							twDate = TimeTranslate.addHour(date + " " + time, "yyyy-MM-dd HH:mm:ss", 8 - piXmlTimeZone);
							List<Double[]> temptValues = Optional.ofNullable(timeSeries.get(twDate))
									.orElse(new ArrayList<Double[]>());
							temptValues.add(new Double[] { x, y, value });
							timeSeries.put(twDate, temptValues);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			});

			// convert timeSeries map to asciiFiles
			timeSeries.forEach((time, xyzList) -> {
				AsciiBasicControl temptAscii = emptyAscii.clone();
				xyzList.forEach(xyz -> {
					temptAscii.setValue(xyz[0], xyz[1], String.valueOf(xyz[2]));
				});
				try {
					String asciiName = TimeTranslate.getDateStringTranslte(time, "yyyy-MM-dd HH:mm:ss", "yyyyMMddHH");
					new AtFileWriter(temptAscii.getAsciiFile(), Global.asciiFolder + asciiName + ".asc")
							.textWriter(" ");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

		}

	}

	public AsciiBasicControl getAsciiTemplate(String piXmlFileAdd) throws IOException {
		List<Double[]> xyzList = new ArrayList<>();
		AtXmlReader temptPiXml = new AtXmlReader(piXmlFileAdd, "UTF-8");
		temptPiXml.getNodeByTag("series").forEach(seriesElement -> {

			// get properties
			Element headerElement = seriesElement.getElementsByTag("header").get(0);
			String x = headerElement.getElementsByTag("x").get(0).text();
			String y = headerElement.getElementsByTag("y").get(0).text();

			xyzList.add(new Double[] { Double.parseDouble(x), Double.parseDouble(y), 0.0 });
		});

		XYZToAscii xyz = new XYZToAscii(xyzList);
		xyz.setCellSize(0.0125);
		xyz.start();

		return xyz.getEmptyAscii();
	}

}
