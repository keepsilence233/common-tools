package qx.leizige.common;

import qx.leizige.common.annotations.Children;
import qx.leizige.common.annotations.MenuId;
import qx.leizige.common.annotations.ParentId;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

/**
 * 基于反射的通用树形结构工具类
 */
public class TreeUtils {

    /**
     * 集合转树结构
     *
     * @param collection 目标集合
     * @param clazz      集合元素类型
     * @param isRootNode 判断是否是根节点
     * @return 转换后的树形结构
     */
    public static <T> Collection<T> toTree(Collection<T> collection, Class<T> clazz, Predicate<Object> isRootNode) {
        Field id = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(MenuId.class)).findFirst()
                .orElseThrow(() -> new RuntimeException("There is no field identifying the annotation [@MenuId]"));
        Field parent = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ParentId.class)).findFirst()
                .orElseThrow(() -> new RuntimeException("There is no field identifying the annotation [@ParentId]"));
        Field children = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Children.class)).findFirst()
                .orElseThrow(() -> new RuntimeException("There is no field identifying the annotation [@Children]"));
        return toTree(collection, id.getName(), parent.getName(), children.getName(), clazz, isRootNode);
    }

    /**
     * 集合转树结构
     *
     * @param collection 目标集合
     * @param id         节点编号字段名称
     * @param parent     父节点编号字段名称
     * @param children   子节点集合属性名称
     * @param clazz      集合元素类型
     * @param isRootNode 判断是否是根节点
     * @return 转换后的树形结构
     */
    public static <T> Collection<T> toTree(Collection<T> collection,
                                           String id, String parent, String children,
                                           Class<T> clazz, Predicate<Object> isRootNode) {
        try {
            if (collection == null || collection.isEmpty()) return Collections.emptyList();// 如果目标集合为空,直接返回一个空树

            // 初始化根节点集合, 支持 Set 和 List
            Collection<T> roots;
            if (collection.getClass().isAssignableFrom(Set.class)) {
                roots = new HashSet<>();
            } else {
                roots = new ArrayList<>();
            }

            // 获取 id 字段, 从当前对象或其父类
            Field idField;
            try {
                idField = clazz.getDeclaredField(id);
            } catch (NoSuchFieldException e1) {
                idField = clazz.getSuperclass().getDeclaredField(id);
            }

            // 获取 parentId 字段, 从当前对象或其父类
            Field parentField;
            try {
                parentField = clazz.getDeclaredField(parent);
            } catch (NoSuchFieldException e1) {
                parentField = clazz.getSuperclass().getDeclaredField(parent);
            }

            // 获取 children 字段, 从当前对象或其父类
            Field childrenField;
            try {
                childrenField = clazz.getDeclaredField(children);
            } catch (NoSuchFieldException e1) {
                childrenField = clazz.getSuperclass().getDeclaredField(children);
            }

            // 设置为可访问
            idField.setAccessible(true);
            parentField.setAccessible(true);
            childrenField.setAccessible(true);

            // 找出所有的根节点
            for (T c : collection) {
                Object parentId = parentField.get(c);
                if (isRootNode.test(parentId)) {
                    roots.add(c);
                }
            }

            // 从目标集合移除所有根节点
            collection.removeAll(roots);

            // 遍历根节点, 依次添加子节点
            for (T root : roots) {
                addChild(root, collection, idField, parentField, childrenField);
            }

            // 关闭可访问
            idField.setAccessible(false);
            parentField.setAccessible(false);
            childrenField.setAccessible(false);

            return roots;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 为目标节点添加孩子节点
     *
     * @param node          目标节点
     * @param collection    目标集合
     * @param idField       ID 字段
     * @param parentField   父节点字段
     * @param childrenField 字节点字段
     */
    private static <T> void addChild(T node, Collection<T> collection, Field idField, Field parentField, Field childrenField) throws IllegalAccessException {
        Object id = idField.get(node);
        Collection<T> children = (Collection<T>) childrenField.get(node);
        // 如果子节点的集合为 null, 初始化孩子集合
        if (children == null) {
            if (collection.getClass().isAssignableFrom(Set.class)) {
                children = new HashSet<>();
            } else {
                children = new ArrayList<>();
            }
        }

        for (T t : collection) {
            Object o = parentField.get(t);
            if (id.equals(o)) {
                // 将当前节点添加到目标节点的孩子节点
                children.add(t);
                // 重设目标节点的孩子节点集合,这里必须重设,因为如果目标节点的孩子节点是null的话,这样是没有地址的,就会造成数据丢失,所以必须重设,如果目标节点所在类的孩子节点初始化为一个空集合,而不是null,则可以不需要这一步,因为java一切皆指针
                childrenField.set(node, children);
                // 递归添加孩子节点
                addChild(t, collection, idField, parentField, childrenField);
            }
        }
    }
}
