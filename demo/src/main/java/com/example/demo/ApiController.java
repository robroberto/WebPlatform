package com.example.demo;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ApiController {
	@Autowired
	private StockRepository stockRepository;

	/*
	 * THIN CLIENT REST-APIs
	 */

	/*
	 * UC-0: Simple Average for a Stock (20 Years)
	 */

	@CrossOrigin(origins = "*")
	@GetMapping("/movingAverage/{n}/{name}")
	public AverageDTO getAvg(@PathVariable int n, @PathVariable String name) {
		double[] data = stockRepository.getData(name);
		return new AverageDTO(stockRepository.getDates(name), data, getSMA(data, n));
	}

	/*
	 * UC-1: Best Simple Average for a Stock (20 Years)
	 */

	@CrossOrigin(origins = "*")
	@GetMapping("/getBestValue")
	public ResultDTO getBestValue(@PathParam("name") String name) {
		double[] data = stockRepository.getData(name);
		final int MIN = 2;
		final int MAX = 250;
		double total = 0.0;
		double best = 0.0;
		int value = 0;
		boolean firstTime = true;
		double startValue = 0.0;
		double lastPrice = 0.0;
		for (int n = MIN; n <= MAX; n++) {
			double[] result = getSMA(data, n);
			startValue = data[0];
			lastPrice = startValue;
			for (int i = 0; i < result.length - 1; i++) {
				if (data[i + n - 1] < result[i] && data[i + n] > result[i + 1]) {
					// buy
					lastPrice = data[i + n];

				} else if (data[i + n - 1] > result[i] && data[i + n] < result[i + 1]) {
					// sell
					total += (data[i + n] - lastPrice);
					lastPrice = data[i + n];
				}

				if (firstTime) {
					best = total;
					firstTime = false;

				} else if (total > best) {
					value = n;
					best = total;
				}

			}
		}

		return new ResultDTO(value, best, (best / startValue));
	}

	/*
	 * UC-2: Best Simple Average for a Stock (1 Year)
	 */

	@CrossOrigin(origins = "*")
	@GetMapping("/getBestValueOneYear")
	public ResultDTO getBestValueOneYear(@PathParam("name") String name) {
		double[] data = stockRepository.getOneYearData(name);
		final int MIN = 2;
		final int MAX = 250;
		double total = 0.0;
		double best = 0.0;
		int value = 0;
		boolean firstTime = true;
		double startValue = 0.0;
		double lastPrice = 0.0;
		for (int n = MIN; n <= MAX; n++) {
			double[] result = getSMA(data, n);
			startValue = data[0];
			lastPrice = startValue;
			for (int i = 0; i < result.length - 1; i++) {
				if (data[i + n - 1] < result[i] && data[i + n] > result[i + 1]) {
					// buy
					lastPrice = data[i + n];

				} else if (data[i + n - 1] > result[i] && data[i + n] < result[i + 1]) {
					// sell
					total += (data[i + n] - lastPrice);
					lastPrice = data[i + n];
				}

				if (firstTime) {
					best = total;
					firstTime = false;

				} else if (total > best) {
					value = n;
					best = total;
				}

			}
		}

		return new ResultDTO(value, best, (best / startValue));
	}

	/*
	 * UC-3: Apply Strategy on Portfolio: Best Simple Average
	 */

	@CrossOrigin(origins = "*")
	@GetMapping("/bestSimpleAveragePortfolio")
	public Double bestSimpleAveragePortfolio() {
		Double totalResult = 0.0;
		String[] stocks = { "abbn", "cfr", "csgn", "gebn", "givn", "logn", "nesn", "novn", "rog", "scmn", "sgs", "sika",
				"slhn", "sren", "ubsg", "zurn" };
		for (String stock : stocks) {
			totalResult += getBestValue(stock).getProgression();
		}
		return totalResult;
	}

	/*
	 * UC-4: Apply Strategy on Portfolio: Rebalancing
	 */

	@CrossOrigin(origins = "*")
	@GetMapping("/rebalancingStrategyPortfolio")
	public Double rebalancingStrategyPortfolio() throws ParseException {
		Double totalResult = 0.0;
		String[] stocks = { "abbn", "cfr", "csgn", "gebn", "givn", "logn", "nesn", "novn", "rog", "scmn", "sgs", "sika",
				"slhn", "sren", "ubsg", "zurn" };
		for (String stock : stocks) {
			totalResult += simulateBonds(stock);
		}
		return totalResult;
	}

	/*
	 * UC-5: Apply Strategy on Portfolio: Buy & Hold
	 */

	@CrossOrigin(origins = "*")
	@GetMapping("/buyHoldPortfolio")
	public Double buyHoldPortfolio() {
		Double totalResult = 0.0;
		String[] stocks = { "abbn", "cfr", "csgn", "gebn", "givn", "logn", "nesn", "novn", "rog", "scmn", "sgs", "sika",
				"slhn", "sren", "ubsg", "zurn" };
		for (String stock : stocks) {
			totalResult += buyHold(stock);
		}
		return totalResult;
	}

	/*
	 * UC-6: Choose Best Strategy (20-Year)
	 */

	@CrossOrigin(origins = "*")
	@GetMapping("/getBestStrategy")
	public String getBestStrategy() throws ParseException {
		Double a = bestSimpleAveragePortfolio();
		Double b = 0.0;
		Double c = buyHoldPortfolio();
		if (a >= b && a >= c) {
			return "Best Simple Moving Average";
		} else if (b >= a && b >= c) {
			return "Rebalancing";
		} else {
			return "Buy & Hold";
		}
	}

	/*
	 * UC-7: Choose Best Strategy (1-Year)
	 */

	@CrossOrigin(origins = "*")
	@GetMapping("/getBestStrategyOneYear")
	public String getBestStrategyOneYear() throws ParseException {
		Double a = bestSimpleAveragePortfolioOneYear();
		Double b = 0.0;
		Double c = buyHoldPortfolioOneYear();
		if (a >= b && a >= c) {
			return "Best Simple Moving Average";
		} else if (b >= a && b >= c) {
			return "Rebalancing";
		} else {
			return "Buy & Hold";
		}
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/bestSimpleAveragePortfolioOneYear")
	public Double bestSimpleAveragePortfolioOneYear() {
		Double totalResult = 0.0;
		String[] stocks = { "abbn", "cfr", "csgn", "gebn", "givn", "logn", "nesn", "novn", "rog", "scmn", "sgs", "sika",
				"slhn", "sren", "ubsg", "zurn" };
		for (String stock : stocks) {
			totalResult += getBestValueOneYear(stock).getProgression();
		}
		return totalResult;
	}

	/*
	 * Moving Average Methods
	 */

	public static double[] getSMA(double data[], int n) {
		int size = data.length + 1 - n;
		double[] result = new double[size];
		double runningSum = 0.0;
		for (int i = 1; i <= data.length; i++) {
			runningSum += data[i - 1];
			if (i >= n) {
				double movingAverage = runningSum / n;
				result[i - n] = movingAverage;
				runningSum -= data[i - n];
			}
		}
		return result;
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/movingAverageOneYear/{n}/{name}")
	public AverageDTO getAvgOneYear(@PathVariable int n, @PathVariable String name) {
		double[] data = stockRepository.getOneYearData(name);
		int size = data.length + 1 - n;
		double[] result = new double[size - n];
		int currentPosition = 0;
		for (int i = 0; i < n; i++) {
			result[currentPosition] += data[i] / n;
		}
		for (int i = n; i < size - 1; i++) {
			currentPosition++;
			result[currentPosition] = result[currentPosition - 1] + (data[i] / n) - (data[currentPosition - 1] / n);
		}
		return new AverageDTO(stockRepository.getOneYearDates(name), data, result);
	}

	/*
	 * Buy & Hold Methods
	 */

	@CrossOrigin(origins = "*")
	@GetMapping("/buyHold/{name}")
	public Double buyHold(@PathVariable String name) {
		double[] data = stockRepository.getData(name);
		return (data[data.length - 1] / data[0]);
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/buyHoldOneYear/{name}")
	public Double buyHoldOneYear(@PathVariable String name) {
		double[] data = stockRepository.getOneYearData(name);
		return (data[data.length - 1] / data[0]);
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/buyHoldPortfolioOneYear")
	public Double buyHoldPortfolioOneYear() {
		Double totalResult = 0.0;
		String[] stocks = { "abbn", "cfr", "csgn", "gebn", "givn", "logn", "nesn", "novn", "rog", "scmn", "sgs", "sika",
				"slhn", "sren", "ubsg", "zurn" };
		for (String stock : stocks) {
			totalResult += buyHoldOneYear(stock);
		}
		return totalResult;
	}

	/*
	 * Rebalancing Methods
	 */

	@CrossOrigin(origins = "*")
	@GetMapping("/simulateBonds/{name}")
	public Double simulateBonds(@PathVariable String name) throws ParseException {
		double[] data = stockRepository.getDataBetween(name, "2001-06-25", "2022-05-19");
		double[] bonds = stockRepository.getBondsData();
		Date[] dates = stockRepository.getDatesBetween(name, "2001-06-25", "2022-05-19");

		HashMap<String, Double> stockData = new HashMap<String, Double>();
		HashMap<String, Double> bondData = new HashMap<String, Double>();

		for (int i = 0; i < dates.length; i++) {
			stockData.put(dates[i].toString(), data[i]);
			bondData.put(dates[i].toString(), bonds[i]);
		}

		String[] datesToCheck = { "2001-06-25", "2002-06-25", "2003-06-25", "2004-06-25", "2005-06-25", "2006-06-25",
				"2007-06-25", "2008-06-25", "2009-06-25", "2010-06-25", "2011-06-25", "2012-06-25", "2013-06-25",
				"2014-06-25", "2015-06-25", "2016-06-25", "2017-06-25", "2018-06-25", "2019-06-25", "2020-06-25" };
		Double bondsValue = 100.0;
		Double stockValue = 100.0;
		Double initTotal = 200.0;
		Double lastBondsPrice = bondData.get(datesToCheck[0]);
		Double lastStockPrice = stockData.get(datesToCheck[0]);
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat format2 = new SimpleDateFormat("EEEE");
		for (int i = 1; i < datesToCheck.length; i++) {
			if (format2.format(formatter1.parse(datesToCheck[i])).equals("Saturday")) {
				datesToCheck[i] = datesToCheck[i].split("-")[0] + datesToCheck[i].split("-")[1]
						+ (Integer.parseInt(datesToCheck[i].split("-")[2]) - 1);
			} else if (format2.format(formatter1.parse(datesToCheck[i])).equals("Sunday")) {
				datesToCheck[i] = datesToCheck[i].split("-")[0] + datesToCheck[i].split("-")[1]
						+ (Integer.parseInt(datesToCheck[i].split("-")[2]) - 2);

			}
			try {
				double bondsChange = bondData.get(datesToCheck[i]) / lastBondsPrice;
				double stockChange = stockData.get(datesToCheck[i]) / lastStockPrice;
				bondsValue *= bondsChange;
				stockValue *= stockChange;
				lastBondsPrice = bondData.get(datesToCheck[i]);
				lastStockPrice = stockData.get(datesToCheck[i]);
				if (stockValue > bondsValue) {
					double diff = stockValue - bondsValue;
					stockValue -= diff / 2;
					bondsValue += diff / 2;
				} else if (stockValue < bondsValue) {
					double diff = bondsValue - stockValue;
					bondsValue -= diff / 2;
					stockValue += diff / 2;
				}
			} catch (Exception e) {

			}

		}
		return (((bondsValue + stockValue) / initTotal) - 1) * 100;
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/rebalancingStrategyPortfolioOneYear")
	public Double rebalancingStrategyPortfolioOneYear() throws ParseException {
		Double totalResult = 0.0;
		String[] stocks = { "abbn", "cfr", "csgn", "gebn", "givn", "logn", "nesn", "novn", "rog", "scmn", "sgs", "sika",
				"slhn", "sren", "ubsg", "zurn" };
		for (String stock : stocks) {
			totalResult += simulateBondsOneYear(stock);
		}
		return totalResult;
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/simulateBondsOneYear/{name}")
	public Double simulateBondsOneYear(@PathVariable String name) throws ParseException {
		double[] data = stockRepository.getDataBetween(name, "2020-06-25", "2021-05-19");
		double[] bonds = stockRepository.getBondsData();
		Date[] dates = stockRepository.getDatesBetween(name, "2020-06-25", "2021-05-19");
		HashMap<String, Double> stockData = new HashMap<String, Double>();
		HashMap<String, Double> bondData = new HashMap<String, Double>();

		for (int i = 0; i < dates.length; i++) {
			stockData.put(dates[i].toString(), data[i]);
			bondData.put(dates[i].toString(), bonds[i]);
		}

		String[] datesToCheck = { "2020-06-25", "2021-06-25" };
		Double bondsValue = 100.0;
		Double stockValue = 100.0;
		Double initTotal = 200.0;
		Double lastBondsPrice = bondData.get(datesToCheck[0]);
		Double lastStockPrice = stockData.get(datesToCheck[0]);
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat format2 = new SimpleDateFormat("EEEE");
		for (int i = 1; i < datesToCheck.length; i++) {
			if (format2.format(formatter1.parse(datesToCheck[i])).equals("Saturday")) {
				datesToCheck[i] = datesToCheck[i].split("-")[0] + datesToCheck[i].split("-")[1]
						+ (Integer.parseInt(datesToCheck[i].split("-")[2]) - 1);
			} else if (format2.format(formatter1.parse(datesToCheck[i])).equals("Sunday")) {
				datesToCheck[i] = datesToCheck[i].split("-")[0] + datesToCheck[i].split("-")[1]
						+ (Integer.parseInt(datesToCheck[i].split("-")[2]) - 2);

			}
			try {
				double bondsChange = bondData.get(datesToCheck[i]) / lastBondsPrice;
				double stockChange = stockData.get(datesToCheck[i]) / lastStockPrice;
				bondsValue *= bondsChange;
				stockValue *= stockChange;
				lastBondsPrice = bondData.get(datesToCheck[i]);
				lastStockPrice = stockData.get(datesToCheck[i]);
				if (stockValue > bondsValue) {
					double diff = stockValue - bondsValue;
					stockValue -= diff / 2;
					bondsValue += diff / 2;
				} else if (stockValue < bondsValue) {
					double diff = bondsValue - stockValue;
					bondsValue -= diff / 2;
					stockValue += diff / 2;
				}
			} catch (Exception e) {

			}
		}
		return (((bondsValue + stockValue) / initTotal) - 1) * 100;
	}

	public static int getDayNumberOld(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/*
	 * THICK CLIENT REST-APIs
	 */

	@CrossOrigin(origins = "*")
	@GetMapping("/stockData/{name}")
	public DataDTO stockData(@PathVariable String name) {
		return new DataDTO(stockRepository.getStockDates(name), stockRepository.getStockData(name));
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/bondsData")
	public DataDTO bondData() {
		return new DataDTO(stockRepository.getBondDates(), stockRepository.getBondData());
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping("/allData")
	public DataDTO allData() {
		return new DataDTO(stockRepository.getAllDates(), stockRepository.getAllData(), stockRepository.getAllNames());
	}

	// older, slower version of getbestN
	@CrossOrigin(origins = "*")
	@GetMapping("/getBestValue2")
	public ResultDTO getBestValue2(@PathParam("min") int min, @PathParam("max") int max,
			@PathParam("name") String name) {
		double[] data = stockRepository.getData(name);
		double best = 0.0;
		int value = 0;
		Boolean firstBuy = true;
		double startValue = 0.0;
		Boolean firstTime = true;
		for (int i = min; i <= max; i++) {
			int size = data.length + 1 - i;
			double[] result = new double[size - i];
			int currentPosition = 0;
			for (int j = 0; j < i; j++) {
				result[currentPosition] += data[j] / i;
			}
			double total = 0.0;
			double lastPrice = data[0];
			for (int j = i; j < size - 1; j++) {
				currentPosition++;
				result[currentPosition] = result[currentPosition - 1] + (data[j] / i) - (data[currentPosition - 1] / i);
				if (data[currentPosition - 1] < result[currentPosition - 1]
						&& data[currentPosition] > result[currentPosition]) {
					// buy
					lastPrice = data[currentPosition];
					if (firstBuy) {
						firstBuy = false;
						startValue = lastPrice;
					}
				} else if (data[currentPosition - 1] > result[currentPosition - 1]
						&& data[currentPosition] < result[currentPosition] && !firstBuy) {
					// sell
					total += (data[currentPosition] - lastPrice);
					lastPrice = data[currentPosition];
				}
			}
			if (firstTime && i > 1) {
				value = i;
				best = total;
				firstTime = false;
			} else if (total > best) {
				value = i;
				best = total;
			}

		}
		System.out.println("Value " + value);
		System.out.println("best " + best);
		System.out.println("startValue " + startValue);
		System.out.println("progression " + (value / startValue) * 100);
		return new ResultDTO(value, best, ((startValue + value) / startValue) * 100);
	}

}
