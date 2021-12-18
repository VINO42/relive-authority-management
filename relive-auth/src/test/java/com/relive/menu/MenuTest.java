//package com.relive.menu;
//
//import com.relive.entity.Menu;
//import com.relive.utils.JsonUtils;
//import org.junit.Test;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * @author: ReLive
// * @date: 2021/11/14 6:20 下午
// */
//public class MenuTest {
//    @Test
//    public void testTree() {
//        //模拟从数据库查询出来
//        List<Menu> menus = Arrays.asList(
//                new Menu(1L, 0L, "根节点"),
//                new Menu(2L, 1L, "子节点1"),
//                new Menu(3L, 2L, "子节点1.1"),
//                new Menu(4L, 2L, "子节点1.2"),
//                new Menu(5L, 2L, "根节点1.3"),
//                new Menu(6L, 1L, "根节点2"),
//                new Menu(7L, 6L, "根节点2.1"),
//                new Menu(8L, 6L, "根节点2.2"),
//                new Menu(9L, 7L, "根节点2.2.1"),
//                new Menu(10L, 7L, "根节点2.2.2"),
//                new Menu(11L, 1L, "根节点3"),
//                new Menu(12L, 11L, "根节点3.1")
//        );
//
//        //获取父节点
//        List<Menu> collect = menus.stream().filter(m -> m.getParentId() == 0).map(
//                (m) -> {
//                    m.setChildList(getChildrens(m, menus));
//                    return m;
//                }
//        ).collect(Collectors.toList());
//        System.out.println(JsonUtils.objectToJson(collect));
//    }
//
//    /**
//     * 递归查询子节点
//     *
//     * @param root 根节点
//     * @param all  所有节点
//     * @return 根节点信息
//     */
//    private List<Menu> getChildrens(Menu root, List<Menu> all) {
//        List<Menu> children = all.stream().filter(m -> {
//            return Objects.equals(m.getParentId(), root.getId());
//        }).map(
//                (m) -> {
//                    m.setChildList(getChildrens(m, all));
//                    return m;
//                }
//        ).collect(Collectors.toList());
//        return children;
//    }
//}
