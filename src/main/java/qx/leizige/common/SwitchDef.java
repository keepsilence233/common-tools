package qx.leizige.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SwitchDef {

    /**
     * 启用HTTPS
     */
    ENABLE_HTTPS(0, SwitchConst.ON, "启用HTTPS"),

    /**
     * 启用ASYNC
     */
    ENABLE_ASYNC(1, SwitchConst.OFF, "启用ASYNC");

    /**
     * 下标
     */
    private final int index;

    /**
     * 默认状态
     */
    private final boolean defaultStatus;

    /**
     * 描述
     */
    private final String description;


    public static class SwitchConst {

        public static final boolean ON = true;
        public static final boolean OFF = false;

    }

}
