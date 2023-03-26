package qx.leizige.common;

public interface Switch {

    void trueOn(SwitchDef switchDef);


    void trueOff(SwitchDef switchDef);


    boolean status(SwitchDef switchDef);
}
