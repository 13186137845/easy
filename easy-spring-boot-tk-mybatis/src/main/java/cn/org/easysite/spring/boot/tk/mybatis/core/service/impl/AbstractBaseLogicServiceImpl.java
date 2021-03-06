package cn.org.easysite.spring.boot.tk.mybatis.core.service.impl;

import cn.org.easysite.spring.boot.tk.mybatis.core.entity.BaseLogicEntity;
import com.github.pagehelper.PageInfo;
import cn.org.easysite.spring.boot.tk.mybatis.core.service.BaseLogicService;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : yinlin
 * @version : 1.0
 * @date : 2019-01-29 10:17
 * @Description :
 * @Copyright : Copyright (c) 2018
 * @Company : EasySite Technology Chengdu Co. Ltd.
 * @link : cn.org.easysite.spring.boot.tk.mybatis.core.service.impl.AbstractBaseLogicService
 */
@Slf4j
public abstract class AbstractBaseLogicServiceImpl<T extends BaseLogicEntity> extends AbstractBaseServiceImpl<T> implements BaseLogicService<T> {

    /**
     * 创建一个Class的对象来获取泛型的class
     */
    private Class<?> clazz;

    public Class<?> getClazz() {
        if (clazz == null) {
            //获取泛型的Class对象
            if (this.getClass().getGenericSuperclass() instanceof ParameterizedType) {

                clazz = ((Class<?>)
                        (((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]));
            }
        }
        return clazz;
    }

    @Override
    public List<T> select(T param) {
        if (param != null) {
            param.setUsable(true);
        }
        return super.select(param);
    }

    @Override
    public int delete(T param) {
        param.setUsable(false);
        return super.update(param);
    }

    @Override
    public T selectByPk(Serializable pk) {
        T entity = super.selectByPk(pk);
        return entity != null && entity.getUsable() ? entity : null;
    }

    @Override
    public List<T> selectAll() {
        T param = null;
        try {
            param = (T) getClazz().newInstance();
        } catch (InstantiationException e) {
            log.warn(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.warn(e.getMessage(), e);
        }
        return this.select(param);
    }

    @Override
    public List<T> selectByPks(Iterable<? extends Serializable> pks) {
        List<T> entities = super.selectByPks(pks);
        for (int i = entities.size() - 1; i >= 0; i --) {
            if (!entities.get(i).getUsable()) {
                entities.remove(i);
            }
        }
        return entities;
    }

    @Override
    public T selectOne(T param) {
        param.setUsable(true);
        return super.selectOne(param);
    }

    @Override
    public PageInfo<T> selectPage(T param, int pageNum, int pageSize) {
        param.setUsable(true);
        return super.selectPage(param, pageNum, pageSize);
    }

    @Override
    public PageInfo<T> selectPageAndCount(T param, int pageNum,
            int pageSize) {
        param.setUsable(true);
        return super.selectPageAndCount(param, pageNum, pageSize);
    }

}
