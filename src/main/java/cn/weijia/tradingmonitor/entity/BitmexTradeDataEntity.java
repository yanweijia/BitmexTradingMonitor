
package cn.weijia.tradingmonitor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@Data
@TableName("t_bitmex_trade")
public class BitmexTradeDataEntity {

    private Long foreignNotional;

    private Long grossValue;

    private Double homeNotional;

    private Long price;

    private String side;

    private Long size;

    private String symbol;

    private String tickDirection;

    private Date timestamp;

    @TableId("trd_match_id")
    @TableField("trd_match_id")
    private String trdMatchID;

}
