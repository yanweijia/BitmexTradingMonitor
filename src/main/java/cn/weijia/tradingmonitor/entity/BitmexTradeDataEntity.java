
package cn.weijia.tradingmonitor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@Data
@TableName("t_bitmex_trade")
public class BitmexTradeDataEntity {

    private Double price;

    private String side;

    private Long size;

    private String symbol;

    private Date timestamp;

    @TableId
    private String id;

}
