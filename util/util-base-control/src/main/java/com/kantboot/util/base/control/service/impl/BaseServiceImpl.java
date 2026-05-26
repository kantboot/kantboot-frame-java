package com.kantboot.util.base.control.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.util.base.control.service.IBaseService;
import com.kantboot.util.i18n.util.I18nUtil;
import com.kantboot.util.jpa.param.PageParam;
import com.kantboot.util.jpa.result.PageResult;
import com.kantboot.util.jpa.sql.global.entity.ConditionGlobeEntity;
import com.kantboot.util.jpa.sql.global.repository.ZeusJpaRepository;
import com.kantboot.util.jpa.sql.global.util.EasyConditionUtil;
import com.kantboot.util.jpa.sql.global.util.SqlGlobalUtil2;
import com.kantboot.util.rest.exception.BaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Service
@Primary
public class BaseServiceImpl<T extends Serializable, ID>
        implements IBaseService<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * 获取第一个泛型的类型
     */
    private Class<T> getTClass() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        return null;
    }

    /**
     * 获取JpaRepository
     */
    private ZeusJpaRepository<T, ID> getJpaRepository(Class<T> tClass, EntityManager entityManager) {
        return new ZeusJpaRepository<>(
                JpaEntityInformationSupport.getEntityInformation(tClass, entityManager), entityManager);
    }

    @Override
    public List<T> getAll(ConditionGlobeEntity operatorGlobe, Class<T> tClass) {
        Query query = new SqlGlobalUtil2<T>().buildQuery(entityManager, tClass, operatorGlobe);
        return query.getResultList();
    }

    @Override
    public List<T> getAllEasy(Map<String, Object> operatorGlobe, Class<T> tClass) {
        ConditionGlobeEntity conditionGlobeEntity = EasyConditionUtil.getConditionGlobeEntity(operatorGlobe);
        return getAll(conditionGlobeEntity, tClass);
    }

    @Override
    public PageResult getBodyData(PageParam<ConditionGlobeEntity> pageParam, Class<T> tClass) {
        return new SqlGlobalUtil2<T>().buildPageQuery(entityManager,tClass,pageParam);

    }

    @Override
    public PageResult getBodyDataEasy(PageParam<Map<String, Object>> pageParam, Class<T> tClass) {
        ConditionGlobeEntity conditionGlobeEntity = EasyConditionUtil.getConditionGlobeEntity(pageParam.getData());
        PageParam<ConditionGlobeEntity> pageParam1 = BeanUtil.copyProperties(pageParam, PageParam.class);
        pageParam1.setData(conditionGlobeEntity);
        return getBodyData(pageParam1, tClass);
    }

    @Transactional
    @Modifying
    @Override
    public T save(T entity, Class<T> tClass) {
        // 获取对应的ID是否为空
        Object id = getId(entity);
        if(id!=null){
            // 获取对应的实体
            T dbEntity = getById((ID) id, tClass);
            if(dbEntity!=null){
                // 如果不为空，则进行属性拷贝，如果entity中的值为空，则把空值赋值成dbEntity中的值
                JSONObject dbEntityJson = JSON.parseObject(JSON.toJSONString(dbEntity));
                JSONObject entityJson = JSON.parseObject(JSON.toJSONString(entity));
                for (String key : dbEntityJson.keySet()) {
                    if(entityJson.get(key)==null){
                        entityJson.put(key,dbEntityJson.get(key));
                    }
                }
                entity = JSON.parseObject(entityJson.toJSONString(), tClass);
            }
        }

        return getJpaRepository(tClass, entityManager).save(entity);
    }

    @Transactional
    @Modifying
    @Override
    public void saveBatch(List<T> entityList, Class<T> tClass) {
        getJpaRepository(tClass, entityManager).saveAll(entityList);
    }

    @Transactional
    @Modifying
    @Override
    public void remove(T entity, Class<T> tClass) {
        try {
            ID id = (ID) getId(entity);
            Optional<T> byId = getJpaRepository(tClass, entityManager).findById(id);
            if (byId.isPresent()) {
                getJpaRepository(tClass, entityManager).deleteById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            throw BaseException.of("deleteError", "删除失败");
        }
    }

    @Transactional
    @Modifying
    @Override
    public void removeBatch(List<T> entityList, Class<T> tClass) {
        for (T entity : entityList) {
            remove(entity, tClass);
        }
    }

    @Override
    public T getById(ID id, Class<T> tClass) {
        return getJpaRepository(tClass, entityManager).findById(id).orElse(null);
    }

    public Object getId(Object object) {
        List<Field> fieldList = new ArrayList<>();
        // 从Class对象中获取Demo中声明方法对应的Method对象
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            fieldList.add(field);
        }

        try{
            Class<?> superclass = object.getClass().getSuperclass();
            Field[] superclassFields = superclass.getDeclaredFields();
            for (Field field : superclassFields) {
                fieldList.add(field);
            }
        }catch (Exception e){
            // 如果没有父类，则不需要获取父类的字段
        }
        for (Field field : fieldList) {
            // 判断方法是否被加上了@Autowired这个注解
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                try {
                    return field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    @SneakyThrows
    @Transactional  // 确保事务注解在类级别也可见
    @Modifying
    @Override
    public T saveI18n(String languageCode, String attr, String value, ID id, Class<T> tClass) {
        // 1. 根据ID获取实体
        T entity = getById(id, tClass);
        if (entity == null) {
            throw BaseException.of("entityNotFound", "", "en");
        }

        String i18nSaveFieldName = I18nUtil.getFieldFromI18nSave(tClass);
        if (i18nSaveFieldName == null || i18nSaveFieldName.isEmpty()) {
            throw BaseException.of("i18nFieldNotFound", "", "en");
        }

        String setterI18nSave = "set" +
                i18nSaveFieldName.substring(0, 1).toUpperCase() +
                i18nSaveFieldName.substring(1);

        String getterI18nSave = "get" +
                i18nSaveFieldName.substring(0, 1).toUpperCase() +
                i18nSaveFieldName.substring(1);

        Method getter = tClass.getMethod(getterI18nSave);
        Object invoke = getter.invoke(entity, (Object[]) null);
        if (invoke == null) {
            invoke = new HashMap<>();
        }
        Map<String, Map<String, Object>> i18nSave = (Map<String, Map<String, Object>>) invoke;
        if(i18nSave == null){
            i18nSave = new HashMap<>();
        }
        Map<String, Object> stringObjectMap = i18nSave.get(languageCode);
        if (stringObjectMap == null) {
            stringObjectMap = new HashMap<>();
        }
        stringObjectMap.put(attr, value);
        i18nSave.put(languageCode, stringObjectMap);

        Method setter = tClass.getMethod(setterI18nSave, Map.class);
        setter.invoke(entity, i18nSave);


        String i18nQueryFieldName = I18nUtil.getFieldFromI18nQuery(tClass);
        if (i18nQueryFieldName != null && !i18nQueryFieldName.isEmpty()) {
            String setterI18nQuery = "set" +
                    i18nQueryFieldName.substring(0, 1).toUpperCase() +
                    i18nQueryFieldName.substring(1);
            StringBuilder i18nQueryValue = new StringBuilder();
            // 获取所有键值对
            for (Map.Entry<String, Map<String, Object>> entry : i18nSave.entrySet()) {
                String key = entry.getKey();
                i18nQueryValue.append("<kantbootI18n.languageCode.").append(key).append(">");
                Map<String, Object> valueMap = entry.getValue();
                for (Map.Entry<String, Object> valueEntry : valueMap.entrySet()) {
                    String valueKey = valueEntry.getKey();
                    String valueValue = valueEntry.getValue()+"";
                    valueValue = valueValue.toUpperCase();
                    i18nQueryValue.append("<kantbootI18n.attr.").append(valueKey).append(">").append(valueValue).append("</kantbootI18n.attr.").append(valueKey).append(">");
                }
                i18nQueryValue.append("</kantbootI18n.languageCode.").append(key).append(">");
            }
            Method setterI18nQueryMethod = tClass.getMethod(setterI18nQuery, String.class);
            setterI18nQueryMethod.invoke(entity, i18nQueryValue.toString().toString());
        }

        // 2. 保存实体
        return getJpaRepository(tClass, entityManager).save(entity);
    }
}
