package com.uday.finitiativesassignment.utilites;

import com.uday.finitiativesassignment.interfaces.IParseListener;

public class Constants {

    public static boolean logMessageOnOrOff = true;

    public static final String COINS_URL = "https://api.coingecko.com/api/v3/coins/list";
    public static final String WATCHES_URL = "https://api.coingecko.com/api/v3/simple/price?ids=";
    public static final String EVENTS_URL = "https://api.coingecko.com/api/v3/events?from_date=";
    public static final String COINDETAILS_URL = "https://api.coingecko.com/api/v3/coins/";

    public static final int REQUESTCODE_COINS = 101;
    public static final int REQUESTCODE_WATCHERS = 102;
    public static final int REQUESTCODE_EVENTS = 103;
    public static final int REQUESTCODE_COINDETAILS = 104;

}
