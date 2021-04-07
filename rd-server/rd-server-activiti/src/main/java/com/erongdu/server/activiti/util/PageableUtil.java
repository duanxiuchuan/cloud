package com.erongdu.server.activiti.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

public class PageableUtil {
	
	public static int offset(Pageable pageable) {
		return pageable.getPageNumber() == 0 ? 0 : (pageable.getPageNumber() - 1) * pageable.getPageSize();		
	}
	
    /**
     * List 分页
     */
    public static List<?> toPage(int page, int size , List<?> list) {
        int fromIndex = page * size;
        int toIndex = page * size + size;
        if(fromIndex > list.size()){
            return new ArrayList<>();
        } else if(toIndex >= list.size()) {
            return list.subList(fromIndex,list.size());
        } else {
            return list.subList(fromIndex,toIndex);
        }
    }

    /**
     * 自定义分页
     */
    public static Map<String,Object> toPage(Object object, Object totalElements) {
        Map<String,Object> map = new LinkedHashMap<>(2);
        map.put("content",object);
        map.put("totalElements",totalElements);
        return map;
    }

}
