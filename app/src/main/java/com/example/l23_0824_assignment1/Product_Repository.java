package com.example.l23_0824_assignment1;

import java.util.ArrayList;

public class Product_Repository {
    public static ArrayList<Product> getAllProducts() {
        ArrayList<Product> list = new ArrayList<>();

        // 1. RØDE PodMic
        list.add(new Product(
                "1",
                "RODE",
                "$108.20",
                "$199.99",
                "Dynamic microphone, Speaker microphone",
                "The PodMic is a broadcast-grade dynamic microphone" +
                        " optimized for podcasting. It has a rich, balanced sound, with an internal pop" +
                        " filter to minimize explosives and internal shock-mounting to reduce vibration." +
                        " Constructed from all-metal, the PodMic is built to last and features an" +
                        " integrated swing mount for easy positioning. Optimized for use with the" +
                        " RØDECaster Pro, it also offers exceptional results with any high-quality" +
                        " microphone interface.",
                R.drawable.mic_stand,
                false
        ));

        // 2. BlackHeadphones
        list.add(new Product(
                "2",
                "SONY Premium Wireless Headphones",
                "$70.99",
                "$70.99",
                "Model: WH-1000XM4, Black",
                "The technology with two noise sensors and two" +
                        " microphones on each ear cup detects ambient" +
                        " noise and sends the data to the HD noise" +
                        " minimization processor QN1. Using a new" +
                        " algorithm, the QN1 then processes and minimizes" +
                        " noise for different acoustic environments in real" +
                        " time. Together with a new Bluetooth Audio SoC",
                R.drawable.blackheadphone,
                false
        ));

        // 3. WhiteHeadphones
        list.add(new Product(
                "3",
                "SONY Premium Wireless Headphones",
                "$70.99",
                "$70.99",
                "Model: WH-1000XM4, Beige",
                "The technology with two noise sensors and two" +
                        " microphones on each ear cup detects ambient" +
                        " noise and sends the data to the HD noise" +
                        " minimization processor QN1. Using a new" +
                        " algorithm, the QN1 then processes and minimizes" +
                        " noise for different acoustic environments in real" +
                        " time. Together with a new Bluetooth Audio SoC",
                R.drawable.whiteheadphone,
                false
        ));

        // 4. Redmi Watch 3
        list.add(new Product(
                "4",
                "Xiaomi Redmi Watch 3",
                "$94.90",
                "$94.90",
                "",
                "@string/Beige_Headphone_desc",
                R.drawable.redmi_watch_3,
                false
        ));





        return list;
    }
}