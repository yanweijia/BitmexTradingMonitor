
package cn.weijia.tradingmonitor.dto;

import cn.weijia.tradingmonitor.entity.BitmexTradeDataEntity;
import cn.weijia.tradingmonitor.entity.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * Bitmex 中 socket 的消息格式
 */
@Data
public class BitmexMsgDto {

    /**
     * 操作类型
     */
    private String action;
    /**
     * 数据
     */
    private List<BitmexTradeDataEntity> data;
    /**
     * 对应表格数据
     */
    private String table;

}
