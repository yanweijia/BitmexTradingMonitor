WebSocket 数据样例
===


## hello 消息
```json
{
  "info": "Welcome to the BitMEX Realtime API.",
  "version": "2020-02-13T19:17:38.000Z",
  "timestamp": "2020-02-20T13:50:03.331Z",
  "docs": "https://www.bitmex.com/app/wsAPI",
  "limit": {
    "remaining": 38
  }
}
```

## 订阅成功消息
```json
{
  "success": true,
  "subscribe": "trade:XBTUSD",
  "request": {
    "op": "subscribe",
    "args": "trade:XBTUSD"
  }
}
```

## 消息
```json
{
  "table": "trade",
  "action": "insert",
  "data": [
    {
      "timestamp": "2020-02-20T13:50:14.079Z",
      "symbol": "XBTUSD",
      "side": "Buy",
      "size": 83,
      "price": 9574,
      "tickDirection": "PlusTick",
      "trdMatchID": "13868622-de4a-2aaa-333e-b37e80766807",
      "grossValue": 866935,
      "homeNotional": 0.00866935,
      "foreignNotional": 83
    },
    {
      "timestamp": "2020-02-20T13:50:14.079Z",
      "symbol": "XBTUSD",
      "side": "Buy",
      "size": 17,
      "price": 9574,
      "tickDirection": "ZeroPlusTick",
      "trdMatchID": "151921d1-1cb5-b65a-a347-9e0aae26fa6d",
      "grossValue": 177565,
      "homeNotional": 0.00177565,
      "foreignNotional": 17
    }
  ]
}
```