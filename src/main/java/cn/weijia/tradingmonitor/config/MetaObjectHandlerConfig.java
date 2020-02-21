package cn.weijia.tradingmonitor.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * mybatis 插入自动填充
 *
 * @author weijia
 */
//@Component
@Slf4j
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        setValue("dateCreated", new Date(), metaObject);
        setValue("dateUpdated", new Date(), metaObject);
        setValue("createBy", "system", metaObject);
        setValue("updateBy", "system", metaObject);
        setValue("isValid", true, metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setValue("updatedDate", new Date(), metaObject);
        setValue("updateBy", "system", metaObject);
    }

    /**
     * @param fieldName 字段名称
     * @param value     待填充值
     */
    private void setValue(String fieldName, Object value, MetaObject metaObject) {
        //当该字段未被设置过时进行默认填充
        if (null == metaObject.getValue(fieldName)) {
            setFieldValByName(fieldName, value, metaObject);
        }
    }

}
