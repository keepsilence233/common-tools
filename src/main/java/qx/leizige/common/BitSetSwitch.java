package qx.leizige.common;

import java.util.BitSet;
import java.util.stream.Stream;

/**
 * 基于BIT数组实现全局功能开关
 * {@link <a href="https://www.cnblogs.com/throwable/p/15083516.html">...</a>}
 */
public class BitSetSwitch implements Switch {

    private final BitSet bitSet = new BitSet();

    public BitSetSwitch() {
        init();
    }

    private void init() {
        Stream.of(SwitchDef.values()).forEach(item -> bitSet.set(item.getIndex(), item.isDefaultStatus()));
    }

    @Override
    public void trueOn(SwitchDef switchDef) {
        bitSet.set(switchDef.getIndex(), SwitchDef.SwitchConst.ON);
    }

    @Override
    public void trueOff(SwitchDef switchDef) {
        bitSet.clear(switchDef.getIndex());
    }

    @Override
    public boolean status(SwitchDef switchDef) {
        return bitSet.get(switchDef.getIndex());
    }
}
