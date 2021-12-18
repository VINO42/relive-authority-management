package com.relive.utils;

import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @Author ReLive
 * @Date 2021/3/24-20:13
 */
public class PageInfoUtils {

    public static <T, V> PageInfo<T> convert(PageInfo<V> pageInfo, Function<V, T> mapper) {
        PageInfo<T> tPageInfo = (PageInfo<T>) pageInfo;
        List<T> list = new ArrayList<>();
        pageInfo.getList().stream().forEach(s -> {
            list.add(mapper.apply(s));
        });
        tPageInfo.setList(list);
        return tPageInfo;
    }
}
