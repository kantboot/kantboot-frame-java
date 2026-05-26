package com.kantboot.util.admin.util;

import com.kantboot.util.admin.annotation.AdminColumnAnnotation;
import com.kantboot.util.admin.annotation.AdminColumnSettingAnnotation;
import com.kantboot.util.admin.domain.entity.AdminColumn;
import com.kantboot.util.admin.domain.entity.AdminColumnSetting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class UtilAdminUtil {

    /**
     * 获取对应class的AdminColumn
     */
    public static List<AdminColumn> getColumnsByClass(Class<?> clazz) {

        List<AdminColumn> columns = new ArrayList<>();

        List<Field> fields = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        Class<?> superclass = clazz.getSuperclass();
        Field[] declaredFields1 = superclass.getDeclaredFields();
        List<AdminColumn> superColumns = new ArrayList<>();
        for (Field field : declaredFields) {
            fields.add(field);
        }
        for (Field field : declaredFields1) {
            fields.add(field);
        }

        // 获取clazz的所有字段，包括私有
        for (var field : fields) {
            // 获取字段上的AdminColumnAnnotation注解
            AdminColumnAnnotation adminColumnAnnotation = field.getAnnotation(AdminColumnAnnotation.class);
            if (adminColumnAnnotation != null) {
                // 将注解转换为AdminColumn对象
                AdminColumn adminColumn = new AdminColumn();
                adminColumn.setLabel(adminColumnAnnotation.label());
                adminColumn.setField(adminColumnAnnotation.field());
                adminColumn.setWidth(adminColumnAnnotation.width());
                adminColumn.setType(adminColumnAnnotation.type());
                adminColumn.setIsId(adminColumnAnnotation.isId());
                adminColumn.setIsSearch(adminColumnAnnotation.isSearch());
                adminColumn.setIsEdit(adminColumnAnnotation.isEdit());
                adminColumn.setIsHide(adminColumnAnnotation.isHide());
                adminColumn.setIsI18nCenterKey(adminColumnAnnotation.isI18nCenterKey());
                adminColumn.setIsI18nBottomKey(adminColumnAnnotation.isI18nBottomKey());
                adminColumn.setIsSort(adminColumnAnnotation.isSort());
                adminColumn.setOrderBy(adminColumnAnnotation.orderBy());

                // 设置AdminColumnSetting
                AdminColumnSetting setting = new AdminColumnSetting();
                AdminColumnSettingAnnotation settingAnnotation = adminColumnAnnotation.setting();
                setting.setFormat(settingAnnotation.format());
                setting.setOperatorCode(settingAnnotation.operatorCode());
                setting.setGroupCode(settingAnnotation.groupCode());
                adminColumn.setSetting(setting);

                columns.add(adminColumn);
            }
        }
        return columns;
    }

}
