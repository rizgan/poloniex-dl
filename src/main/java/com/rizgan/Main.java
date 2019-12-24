package com.rizgan;

import org.json.simple.parser.ParseException;

import java.io.*;

public class Main {


    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        startFetchingData();
    }


    public static void startFetchingData() throws InterruptedException {
        try {
            for (int i = 5; i < 10; i++) {
                CoinPair.coinPairDownloder(i);
            }
        } catch (Exception e) {
            Thread.sleep(2000);
            for (int i = 5; i < 10; i++) {
                try {
                    CoinPair.coinPairDownloder(i);
                } catch (IOException e1) {
                    Thread.sleep(2000);
                    startFetchingData();
                    e1.printStackTrace();
                } catch (ParseException e1) {
                    Thread.sleep(2000);
                    e1.printStackTrace();
                    startFetchingData();

                }
            }
        }
    }
}