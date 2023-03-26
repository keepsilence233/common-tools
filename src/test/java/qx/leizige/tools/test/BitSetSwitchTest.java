package qx.leizige.tools.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import qx.leizige.common.BitSetSwitch;
import qx.leizige.common.Switch;
import qx.leizige.common.SwitchDef;

@Slf4j
public class BitSetSwitchTest {


    @Test
    public void test() {
        Switch aSwitch = new BitSetSwitch();
        aSwitch.trueOn(SwitchDef.ENABLE_HTTPS);
        aSwitch.trueOn(SwitchDef.ENABLE_ASYNC);
        log.info("开关{},状态:{}", SwitchDef.ENABLE_HTTPS, aSwitch.status(SwitchDef.ENABLE_HTTPS));
        log.info("开关{},状态:{}", SwitchDef.ENABLE_ASYNC, aSwitch.status(SwitchDef.ENABLE_ASYNC));

        aSwitch.trueOff(SwitchDef.ENABLE_HTTPS);
        aSwitch.trueOff(SwitchDef.ENABLE_ASYNC);
        log.info("开关{},状态:{}", SwitchDef.ENABLE_HTTPS, aSwitch.status(SwitchDef.ENABLE_HTTPS));
        log.info("开关{},状态:{}", SwitchDef.ENABLE_ASYNC, aSwitch.status(SwitchDef.ENABLE_ASYNC));
    }

}
