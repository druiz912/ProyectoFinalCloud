package com.druiz.bosonit.backempresa.config.security;

import java.util.Arrays;
import java.util.List;

public class Constants {

        public static final String HEADER = "Authorization";
        public static final String PREFIX = "Bearer ";
        public static final String SECRET = "secret";
        public static final String DATEFORMAT = "yyyy-MM-dd";
        public static final Integer DEFAULTCAPACITY = 40;
        public static final String VIRTUALTRAVELMAIL = "virtualtravel56@gmail.com";
        public static final String VIRTUALTRAVEL2 = "vuiwgvrhynvtmxaz";

        public static final List<Float> HOURS = Arrays.asList(8F,12F,16F,20F);
        public static final String ERRORHOURS = "Todos nuestros buses salen desde Vitoria a las siguientes horas: 8:00AM, 12:00PM, 16:00PM, 20:00PM";
        public static final List<String> CITIES = Arrays.asList("Barcelona", "Bilbao", "Madrid", "Valencia");
        public static final String ERRORCITIES = "Actualmente solo disponemos de viajes en bus desde Vitoria hacia: Barcelona, Bilbao, Madrid y Valencia";
}
