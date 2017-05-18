package com.kakatoto.albumpicker.util.otto;

import com.squareup.otto.Bus;

/**
 * Created by hwoh on 2016-05-16.
 */
public class BusProvider {
    private static  Bus BUS;

    public static Bus getInstance() {
        if(BUS == null)
            BUS = new Bus();
        return BUS;
    }
    public static void detatch(){
        BUS = null;
    }
}
