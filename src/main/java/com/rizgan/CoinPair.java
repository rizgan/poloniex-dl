package com.rizgan;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CoinPair {

    static String last = null;
    static String lowestAsk = null;
    static String highestBid = null;
    static String percentChange = null;
    static String baseVolume = null;
    static String quoteVolume = null;
    static String high24hr = null;
    static String low24hr = null;
    static URLConnection request = null;
    static URL url;
    static String sURL;
    static JSONObject jsonObject;
    static ArrayList<String> coinPirs;


    static void updateDataFromURL() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Object object;
        sURL = "https://poloniex.com/public?command=returnTicker";
        url = new URL(sURL);
        request = url.openConnection();
        request.setDefaultUseCaches(false);
        request.setUseCaches(false);
        request.connect();
        object = jsonParser.parse(new InputStreamReader((InputStream) request.getContent()));
        jsonObject = (JSONObject) object;

        coinPirs = new ArrayList<>();
        coinPirs.add("USDT_BTC");
        JSONObject coinPair = (JSONObject) jsonObject.get(coinPirs.get(0));
        last = (String) coinPair.get("last");
        lowestAsk = (String) coinPair.get("lowestAsk");
        highestBid = (String) coinPair.get("highestBid");
        percentChange = (String) coinPair.get("percentChange");
        baseVolume = (String) coinPair.get("baseVolume");
        quoteVolume = (String) coinPair.get("quoteVolume");
        high24hr = (String) coinPair.get("high24hr");
        low24hr = (String) coinPair.get("low24hr");
    }

    public static void coinPairDownloder(int fileNameExtension) throws IOException, InterruptedException, ParseException {
//        JSONParser jsonParser = new JSONParser();

        updateDataFromURL();

        try (OutputStream os = new FileOutputStream(new File("sd" + fileNameExtension + ".xlsx"))) {
            Workbook wb = new Workbook(os, "MyApplication", "1.0");
            Worksheet ws = wb.newWorksheet("Sheet 1");

            writeColoumnHeaders(ws, coinPirs);

            for (int i = 1; i < 300; i++)
                writeRowValues(ws, i);

            //Write to the file
            wb.finish();
        }
    }

    //Name of every column
    public static void writeColoumnHeaders(Worksheet ws, ArrayList<String> coinPirs) {
        ws.value(0, 0, "Time");
        ws.value(0, 1, coinPirs.get(0) + "_Last");
        ws.value(0, 2, coinPirs.get(0) + "_LowestAsk");
        ws.value(0, 3, coinPirs.get(0) + "_HighestBid");
        ws.value(0, 4, coinPirs.get(0) + "_PercentChange");
        ws.value(0, 5, coinPirs.get(0) + "_BaseVolume");
        ws.value(0, 6, coinPirs.get(0) + "_QuoteVolume");
        ws.value(0, 7, coinPirs.get(0) + "_High24hr");
        ws.value(0, 8, coinPirs.get(0) + "_Low24hr");
    }

    //Write a value to every row
    public static void writeRowValues(Worksheet ws, int indexOfRow) throws InterruptedException, IOException, ParseException {
        updateDataFromURL();
        ws.value(indexOfRow, 0, System.currentTimeMillis());
        ws.value(indexOfRow, 1, Double.parseDouble(last));
        ws.value(indexOfRow, 2, Double.parseDouble(lowestAsk));
        ws.value(indexOfRow, 3, Double.parseDouble(highestBid));
        ws.value(indexOfRow, 4, Double.parseDouble(percentChange));
        ws.value(indexOfRow, 5, Double.parseDouble(baseVolume));
        ws.value(indexOfRow, 6, Double.parseDouble(quoteVolume));
        ws.value(indexOfRow, 7, Double.parseDouble(high24hr));
        ws.value(indexOfRow, 8, Double.parseDouble(low24hr));
        Thread.sleep(1000);
    }
}