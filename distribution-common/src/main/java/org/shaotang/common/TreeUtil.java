package org.shaotang.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeUtil {


    public static <T> List<T> convertListToTree(List<T> list, String childrenFieldName, String idFieldName, String parentIdFieldName, Consumer<T> preFun) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        // list 数据前置处理
        if (preFun != null) {
            for (T t : list) {
                preFun.accept(t);
            }
        }


        // 获取id和数据映射
        Map<Object, T> idAndIdentityMap = list.stream()
                .collect(Collectors.toMap(item -> ReflectUtil.invoke(item, StrUtil.genGetter(idFieldName)), Function.identity(), (k1, k2) -> k2));

        // 获取子节点的数据--> parentId在列表中存在
        List<T> childrenList = list.stream()
                .filter(item -> idAndIdentityMap.containsKey(ReflectUtil.invoke(item, StrUtil.genGetter(parentIdFieldName))))
                .collect(Collectors.toList());
        // 获取根节点数据 计算方式：全部数据列表 - 子节点数据列表
        List<T> parentList = CollUtil.subtractToList(list, childrenList);

        if (CollUtil.isEmpty(parentList)) {
            return list;
        }
        // 处理树形数据
        for (T item : list) {
            Object parentId = ReflectUtil.invoke(item, StrUtil.genGetter(parentIdFieldName));
            // list中包含节点的父级，进行树形数据构造
            if (idAndIdentityMap.containsKey(parentId)) {
                T parent = idAndIdentityMap.get(parentId);
                List<T> children = ReflectUtil.invoke(parent, StrUtil.genGetter(childrenFieldName));
                if (CollUtil.isEmpty(children)) {
                    children = new ArrayList<>();
                }
                children.add(item);
                // 设置children
                ReflectUtil.invoke(parent, StrUtil.genSetter(childrenFieldName), children);
            }
        }

        return parentList;
    }

}
