package qx.leizige.tools.test;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import lombok.*;
import org.junit.Before;
import org.junit.Test;
import qx.leizige.common.TreeUtils;
import qx.leizige.common.annotations.Children;
import qx.leizige.common.annotations.MenuId;
import qx.leizige.common.annotations.ParentId;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class TreeUtilsTest {

    private List<Menu> menuList;

    @Before
    public void before() {
        menuList = Lists.newArrayListWithExpectedSize(5);
        menuList.add(new Menu(1, 0, "节点1"));
        menuList.add(new Menu(2, 0, "节点2"));
        menuList.add(new Menu(3, 1, "节点1.1"));
        menuList.add(new Menu(4, 1, "节点1.2"));
        menuList.add(new Menu(5, 3, "节点1.1.1"));
    }

    @Test
    public void test() {
        Collection<Menu> menus = TreeUtils.toTree(menuList, Menu.class, (v) -> v.equals(0));
        System.out.println(JSONUtil.toJsonPrettyStr(menus));
    }

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Menu implements Serializable {
        private static final long serialVersionUID = 5561561457068906366L;

        @MenuId
        private Integer menuId;

        @ParentId
        private Integer parentId;

        private String menuName;

        private String url;

        @Children
        private List<Menu> children;


        public Menu(Integer menuId, Integer parentId, String menuName) {
            this.menuId = menuId;
            this.parentId = parentId;
            this.menuName = menuName;
        }
    }
}

